package no.kristiania.http;



import no.kristiania.db.Questionnaire;
import no.kristiania.db.QuestionsDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddQuestionsController implements HttpController {
    private final QuestionsDao questionsDao;

    private List<Questionnaire> questionnaire = new ArrayList<>();

    public AddQuestionsController(QuestionsDao questionsDao) {
        this.questionsDao = questionsDao;
    }

    @Override

    public HttpMessage handle(HttpMessage request) throws SQLException {

        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setTitle(queryMap.get("title"));
        questionnaire.setQuestionTxt(queryMap.get("text"));
        questionsDao.save(questionnaire);
        this.questionnaire.add(questionnaire);

        return new HttpMessage("HTTP/1.1 200 OK", "It is done");
    }
}
