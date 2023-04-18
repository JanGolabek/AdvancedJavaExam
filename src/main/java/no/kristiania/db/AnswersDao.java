package no.kristiania.db;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswersDao {
    private final DataSource dataSource;

    public AnswersDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveAnswer(Answers answers) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into answers (question_title, alternative_title) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, answers.getAlternativeTitle());
                statement.setString(2, answers.getQuestionTitle());

                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    answers.setAnswerId(rs.getLong("answer_id"));
                }
            }
        }
    }

    public Answers retrieveAnswer(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from answers where answer_id = ?"
            )) {
                statement.setLong(1, id);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();

                    return mapFromResultSet(rs);
                }
            }
        }
    }

    private Answers mapFromResultSet(ResultSet rs) throws SQLException {
        Answers answers = new Answers();
        answers.setAnswerId(rs.getLong("answer_id"));
        answers.setQuestionTitle(rs.getString("alternative_title"));
        answers.setAlternativeTitle(rs.getString("question_title"));
        return answers;
    }

    public List<Answers> listAllAnswers() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from answers")) {
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Answers> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(readFromResultSet(rs));
                    }
                    return result;
                }
            }
        }
    }

    private Answers readFromResultSet(ResultSet rs) throws SQLException {
        Answers answers = new Answers();
        answers.setAnswerId(rs.getLong("answer_id"));
        answers.setQuestionTitle(rs.getString("question_title"));
        answers.setAlternativeTitle(rs.getString("alternative_title"));
        return answers;
    }
}

