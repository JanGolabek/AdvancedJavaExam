package no.kristiania.db;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionsDao {

    private final DataSource dataSource;

    public QuestionsDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Questionnaire questionnaire) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into questions (question_title, question_description) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, questionnaire.getTitle());
                statement.setString(2, questionnaire.getQuestionTxt());

                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    questionnaire.setQuestionId(rs.getLong("question_id"));
                }
            }
        }
    }

    public Questionnaire retrieve(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from questions where question_id = ?"
            )) {
                statement.setLong(1, id);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();

                    return mapFromResultSet(rs);
                }
            }
        }
    }

    private Questionnaire mapFromResultSet(ResultSet rs) throws SQLException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestionId(rs.getLong("question_id"));
        questionnaire.setTitle(rs.getString("question_title"));
        questionnaire.setQuestionTxt(rs.getString("question_description"));
        return questionnaire;
    }


    public List<Questionnaire> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from questions")) {
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Questionnaire> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(readFromResultSet(rs));
                    }
                    return result;
                }
            }
        }
    }

    private Questionnaire readFromResultSet(ResultSet rs) throws SQLException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestionId(rs.getLong("question_id"));
        questionnaire.setTitle(rs.getString("question_title"));
        questionnaire.setQuestionTxt(rs.getString("question_description"));
        return questionnaire;
    }


}



