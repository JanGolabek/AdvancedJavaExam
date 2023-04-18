package no.kristiania.http;

import no.kristiania.db.QuestionOptions;
import no.kristiania.db.QuestionOptionsDao;

import java.sql.SQLException;

public class ListOptionsController implements HttpController{

    private final QuestionOptionsDao questionOptionsDao;

    public ListOptionsController(QuestionOptionsDao questionOptionsDao){
        this.questionOptionsDao = questionOptionsDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String response = "";
        int value = 1;
        for (QuestionOptions questionOptions: questionOptionsDao.listAllOptions()) {
            response += "<option value=" + questionOptions.getOptionDescription() + ">" + questionOptions.getOptionDescription() + "</option>";
        }
        return new HttpMessage("HTTP/1.1 200 OK", response);
    }

}

