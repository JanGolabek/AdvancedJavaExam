package no.kristiania.db;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswersDaoTest {
    private final AnswersDao dao = new AnswersDao(TestData.testDataSource());


    @Test
    void shouldRetrieveSavedAnswerFromDatabase() throws SQLException {
        Answers answers = exampleAnswer();
        dao.saveAnswer(answers);
        assertThat(dao.retrieveAnswer(answers.getAnswerId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(answers);
    }

    @Test
    void shouldListAllAnswers() throws SQLException {
        Answers answers = exampleAnswer();
        dao.saveAnswer(answers);
        Answers anotherAnswer = exampleAnswer();
        dao.saveAnswer(anotherAnswer);

        assertThat(dao.listAllAnswers())
                .extracting(Answers::getAnswerId)
                .contains(answers.getAnswerId(), anotherAnswer.getAnswerId());
    }

    private Answers exampleAnswer() {
        Answers answers = new Answers();
        answers.setQuestionTitle(pickOne("Favorittmat", "Favoritt fotballag", "lol", "haha"));
        answers.setAlternativeTitle(pickOne("Taco", "Manchester United", "Barcelona", "Pizza"));
        return answers;
    }
    public static String pickOne(String... alternatives) {
        return alternatives[new Random().nextInt(alternatives.length)];
    }
}
