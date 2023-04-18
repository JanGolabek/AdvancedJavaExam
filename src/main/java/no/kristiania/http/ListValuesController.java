package no.kristiania.http;

import no.kristiania.db.Questionnaire;
import no.kristiania.db.QuestionsDao;

import java.sql.SQLException;

public class ListValuesController implements HttpController{
    private final QuestionsDao questionsDao;

    public ListValuesController(QuestionsDao questionsDao) {
        this.questionsDao = questionsDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String responseText = "";

        int value = 1;
        for (Questionnaire questionnaire : questionsDao.listAll()) {
            responseText += "<option value=" + (value++) + ">" + questionnaire.getTitle() + "</option>";
        }
        return new HttpMessage("HTTP/1.1 200 OK", responseText);
    }
}

