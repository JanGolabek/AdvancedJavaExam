package no.kristiania.http;

import no.kristiania.db.Questionnaire;
import no.kristiania.db.QuestionsDao;

import java.sql.SQLException;

public class ListQuestionsController implements HttpController {
    private final QuestionsDao questionsDao;

    public ListQuestionsController(QuestionsDao questionsDao){
        this.questionsDao = questionsDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException{
        String response = "";
        for (Questionnaire questionnaire : questionsDao.listAll()){
            response += "<option value=" + questionnaire.getTitle() + ">" + questionnaire.getTitle() + "</option>";
        }

        return new HttpMessage("HTTP/1.1 200 OK", response);
    }

}


