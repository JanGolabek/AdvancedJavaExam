package no.kristiania.http;

import no.kristiania.db.*;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

public class HttpServer {

    private final ServerSocket serverSocket;
    private Path rootDirectory;
    private List<QuestionOptions> questionOptions = new ArrayList<>();
    private List<Questionnaire> questionnaire = new ArrayList<>();
    private final HashMap<String, HttpController> controllers = new HashMap<>();

    public HttpServer(int serverPort) throws IOException {
        serverSocket = new ServerSocket(serverPort);

        new Thread(this::handleClients).start();
    }
    
    private void handleClients() {
        try {
            while (true) {
                handleClient();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleClient() throws IOException, SQLException {
        Socket clientSocket = serverSocket.accept();

        HttpMessage httpMessage = new HttpMessage(clientSocket);
        String[] requestLine = httpMessage.startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String fileTarget;
        String query = null;
        if (questionPos != -1) {
            fileTarget = requestTarget.substring(0, questionPos);
            query = requestTarget.substring(questionPos + 1);
        } else {
            fileTarget = requestTarget;
        }
        if (controllers.containsKey(fileTarget)) {
            HttpMessage response = controllers.get(fileTarget).handle(httpMessage);
            response.write(clientSocket);}
        else if (fileTarget.equals("/hello")) {
            String yourName = "world";
            if (query != null) {
                yourName = query.split("=")[1];
            }
            String responseText = "<p>Hello " + yourName + "</p>";


            writeOkResponse(clientSocket, responseText, "text/html");
        }

        else {
            if (rootDirectory != null && Files.exists(rootDirectory.resolve(fileTarget.substring(1)))) {
                String responseText = Files.readString(rootDirectory.resolve(fileTarget.substring(1)));

                String contentType = "text/html; charset=utf-8";
                if (requestTarget.endsWith(".html")) {
                    contentType = "text/html; charset=utf-8";
                }else if (requestTarget.endsWith(".css")) {
                    contentType = "text/css";
                } else {
                    contentType = "text/plain; charset=utf-8";
                }
                writeOkResponse(clientSocket, responseText, "text/html");
                return;
            }


            String responseText = "File not found: " + requestTarget;

            String response = "HTTP/1.1 404 Not found\r\n" +
                    "Content-Length: " + responseText.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    responseText;
            clientSocket.getOutputStream().write(response.getBytes());
        }
    }

    private Map<String, String> parseRequestParameters(String query) {
        Map<String, String> queryMap = new HashMap<>();
        for (String queryParameter : query.split("&")) {
            int equalsPos = queryParameter.indexOf('=');
            String parameterName = queryParameter.substring(0, equalsPos);
            String parameterValue = queryParameter.substring(equalsPos + 1);
            queryMap.put(parameterName, parameterValue);
        }
        return queryMap;
    }

    private void writeOkResponse(Socket clientSocket, String responseText, String contentType) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + responseText.length() + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                responseText;
        clientSocket.getOutputStream().write(response.getBytes());
    }

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) throws IOException {
        // Makes localserver at "http://localhost:1969/index.html" that can show all 3 websites.
        DataSource dataSource = createDataSource();
        QuestionsDao questionsDao = new QuestionsDao(dataSource);
        QuestionOptionsDao questionOptionsDao = new QuestionOptionsDao(dataSource);
        AnswersDao answersDao = new AnswersDao(dataSource);
        HttpServer httpServer = new HttpServer(1969);
        httpServer.setRoot(Paths.get("src/main/resources/"));
        httpServer.addController("/api/newQuestionnaire", new AddQuestionsController(questionsDao));
        httpServer.addController("/api/tasks", new AddOptionsController(questionOptionsDao));
        httpServer.addController("/api/questions", new ListQuestionsController(questionsDao));
        httpServer.addController("/api/questions2", new ListOptionsController(questionOptionsDao));
        httpServer.addController("/api/questionOptions", new ListValuesController(questionsDao));
        httpServer.addController("/api/index", new AddAnswersController(answersDao));
        httpServer.addController("/api/answers", new ListAnswersController(answersDao));
        logger.info("Starting http://localhost:{}/index.html", httpServer.getPort());
    }

    private static DataSource createDataSource() throws IOException {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader("pgr203.properties")) {
            properties.load(reader);
        }
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty(
                "dataSource.url",
                "jdbc:postgresql://localhost:5432/forms"
        ));
        dataSource.setUser(properties.getProperty("dataSource.user", "forms_dbuser"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void setRoot(Path rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public void addController(String path, HttpController controller) {
        controllers.put(path, controller);
    }

    public List<QuestionOptions> getQuestionOptions() {
        return questionOptions;
    }

    public List<Questionnaire> getQuestionaires() {
        return questionnaire;
    }
}
