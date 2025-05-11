package web.servlets;

import web.database.DataBase;
import web.requests.Request;
import web.responses.Body;
import web.responses.Response;
import web.entities.Question;
import web.entities.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class GetTestServlet implements Servlet{

    @Override
    public void service(Request request, Response response) throws Exception {
        String selectIdsSQL = "Select questionid FROM questions WHERE theme = ?";
        String selectQuestionSQL = "Select question, answer1, answer2, answer3, answer4, correct_answer " +
                                    "FROM questions WHERE questionid = ?";

        String theme = request.getParam("theme");
        int amount = Integer.parseInt(request.getParam("amount"));

        try (Connection connection = DriverManager.getConnection(DataBase.getURL(), DataBase.getUser(), DataBase.getPassword());
             PreparedStatement idStatement = connection.prepareStatement(selectIdsSQL)) {

            idStatement.setString(1, theme);

            ArrayList<Integer> questionIds = new ArrayList<>();
            try (ResultSet resultSet = idStatement.executeQuery()) {
                while (resultSet.next()) {
                    questionIds.add(resultSet.getInt("question_id"));
                }
            }

            if (questionIds.size() < amount) {
                response.setStatus("400");
                response.setDescription("Недостаточно вопросов в базе");
                return;
            }


            ArrayList<Integer> selectedIds = new ArrayList<>();
            Random rand = new Random();
            while (selectedIds.size() < amount) {
                int id = questionIds.get(rand.nextInt(questionIds.size()));
                if (!selectedIds.contains(id)) {
                    selectedIds.add(id);
                }
            }

            ArrayList<Question> questions = new ArrayList<>();
            try (PreparedStatement questionStatement = connection.prepareStatement(selectQuestionSQL)) {
                for (int id : selectedIds) {
                    questionStatement.setInt(1, id);
                    try (ResultSet rs = questionStatement.executeQuery()) {
                        if (rs.next()) {
                            questions.add(new Question(
                                    rs.getString("question"),
                                    rs.getString("answer1"),
                                    rs.getString("answer2"),
                                    rs.getString("answer3"),
                                    rs.getString("answer4"),
                                    rs.getString("correct_answer")
                            ));
                        }
                    }
                }
            }

            Body test = new Test(questions);

            response.setBody(test);

        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            response.setStatus("400");
            response.setDescription("Registration failed");
        }
    }
}
