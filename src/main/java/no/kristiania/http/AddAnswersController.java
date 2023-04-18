package no.kristiania.http;

import no.kristiania.db.Answers;
import no.kristiania.db.AnswersDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddAnswersController implements HttpController{
    private final AnswersDao answersDao;

    private List<Answers> answers = new ArrayList<>();

    public AddAnswersController(AnswersDao answersDao) {
        this.answersDao = answersDao;
    }

    @Override

    public HttpMessage handle(HttpMessage request) throws SQLException {

        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Answers answers = new Answers();
        answers.setAlternativeTitle(queryMap.get("questions"));
        answers.setQuestionTitle(queryMap.get("questions2"));
        answersDao.saveAnswer(answers);
        this.answers.add(answers);

        return new HttpMessage("HTTP/1.1 200 OK", "It is done");
    }
}

