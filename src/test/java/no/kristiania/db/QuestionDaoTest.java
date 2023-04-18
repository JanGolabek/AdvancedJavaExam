package no.kristiania.db;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;


public class QuestionDaoTest {
    private final QuestionsDao dao = new QuestionsDao(TestData.testDataSource());


    @Test
    void shouldRetrieveSavedQuestionnaireFromDatabase() throws SQLException {
        Questionnaire questionnaire = exampleQuestionnaire();
        dao.save(questionnaire);
        assertThat(dao.retrieve(questionnaire.getQuestionId()))
                .usingRecursiveComparison()
                .isEqualTo(questionnaire);
    }

    @Test
    void shouldListAllQuestions() throws SQLException {
        Questionnaire questionnaire = exampleQuestionnaire();
        dao.save(questionnaire);
        Questionnaire anotherQuestionnaire = exampleQuestionnaire();
        dao.save(anotherQuestionnaire);

        assertThat(dao.listAll())
                .extracting(Questionnaire::getQuestionId)
                .contains(questionnaire.getQuestionId(), anotherQuestionnaire.getQuestionId());
    }

    private Questionnaire exampleQuestionnaire() {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setTitle(pickOne("Favorittmat", "Favoritt fotballag"));
        questionnaire.setQuestionTxt(pickOne("Hva er din favorittmat?", "Hva er fotballag heier du på?"));
        return questionnaire;
    }
    public static String pickOne(String... alternatives) {
        return alternatives[new Random().nextInt(alternatives.length)];
    }
}