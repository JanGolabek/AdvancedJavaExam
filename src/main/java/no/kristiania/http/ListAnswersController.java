package no.kristiania.http;

import no.kristiania.db.Answers;
import no.kristiania.db.AnswersDao;

import java.sql.SQLException;

public class ListAnswersController implements HttpController {
    private final AnswersDao answersDao;

    public ListAnswersController(AnswersDao answersDao) {
        this.answersDao = answersDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String response = "";
        for (Answers answers : answersDao.listAllAnswers()) {
            response += "<h1>" + "Spørsmål: " + answers.getQuestionTitle() + "</h1> <br>" +
                    "<p>" + "Svar: " + answers.getAlternativeTitle() + "</p> <br>";
        }

        return new HttpMessage("HTTP/1.1 200 OK", response);
    }

}

