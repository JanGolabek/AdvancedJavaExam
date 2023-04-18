package no.kristiania.http;

import no.kristiania.db.QuestionOptions;
import no.kristiania.db.QuestionOptionsDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddOptionsController implements HttpController{
    private final QuestionOptionsDao questionOptionsDao;

    private List<QuestionOptions> questionOptions = new ArrayList<>();

    public AddOptionsController(QuestionOptionsDao questionOptionsDao) {
        this.questionOptionsDao = questionOptionsDao;
    }

    @Override

    public HttpMessage handle(HttpMessage request) throws SQLException {

        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        QuestionOptions questionOptions = new QuestionOptions();
        questionOptions.setQuestionOptionsTitle(queryMap.get("questions"));
        questionOptions.setOptionDescription(queryMap.get("alternative"));
        questionOptionsDao.saveOption(questionOptions);
        this.questionOptions.add(questionOptions);

        return new HttpMessage("HTTP/1.1 200 OK", "It is done");
    }
}

