package no.kristiania.db;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionOptionsDao {
    private final DataSource dataSource;

    public QuestionOptionsDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void saveOption(QuestionOptions questionOptions) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into alternatives (alternative_text, question_text) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, questionOptions.getOptionDescription());
                statement.setString(2, questionOptions.getQuestionOptionsTitle());

                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    questionOptions.setOptionId(rs.getLong("alternative_id"));
                }
            }
        }
    }

    public QuestionOptions retrieveOptions(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from alternatives where alternative_id = ?"
            )) {
                statement.setLong(1, id);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();

                    return mapFromResultSet(rs);
                }
            }
        }
    }

    private QuestionOptions mapFromResultSet(ResultSet rs) throws SQLException {
        QuestionOptions questionOptions = new QuestionOptions();
        questionOptions.setOptionId(rs.getLong("alternative_id"));
        questionOptions.setOptionDescription(rs.getString("alternative_text"));
        questionOptions.setQuestionOptionsTitle(rs.getString("question_text"));
        return questionOptions;
    }

    public List<QuestionOptions> listAllOptions() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from alternatives")) {
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<QuestionOptions> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(readFromResultSet(rs));
                    }
                    return result;
                }
            }
        }
    }

    private QuestionOptions readFromResultSet(ResultSet rs) throws SQLException {
        QuestionOptions questionOptions = new QuestionOptions();
        questionOptions.setOptionId(rs.getLong("alternative_id"));
        questionOptions.setOptionDescription(rs.getString("alternative_text"));
        questionOptions.setQuestionOptionsTitle(rs.getString("question_text"));
        return questionOptions;
    }
}

