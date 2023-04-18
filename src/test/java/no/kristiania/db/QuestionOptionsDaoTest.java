package no.kristiania.db;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionOptionsDaoTest {
    private QuestionOptionsDao dao = new QuestionOptionsDao(TestData.testDataSource());

    @Test
    void shouldRetrieveSavedAlternative() throws SQLException {
        QuestionOptions questionOptions = exampleOption();
        dao.saveOption(questionOptions);
        assertThat(dao.retrieveOptions(questionOptions.getOptionId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(questionOptions);
    }

    @Test
    void shouldListAllAlternatives() throws SQLException {
        QuestionOptions questionOptions = exampleOption();
        dao.saveOption(questionOptions);
        QuestionOptions anotherOption = exampleOption();
        dao.saveOption(anotherOption);

        assertThat(dao.listAllOptions())
                .extracting(QuestionOptions::getOptionId)
                .contains(questionOptions.getOptionId(), anotherOption.getOptionId());
    }

    public static QuestionOptions exampleOption() {
        QuestionOptions questionOptions = new QuestionOptions();
        questionOptions.setQuestionOptionsTitle(TestData.pickOne("Pizza", "Taco", "Spaghetti", "Gabagool"));
        questionOptions.setOptionDescription(TestData.pickOne("Hva liker du?", "Hva mat like du ikke?", "Favorittmat?"));
        return questionOptions;
    }
}
