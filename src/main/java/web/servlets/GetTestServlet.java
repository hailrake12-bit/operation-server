package web.servlets;

import web.requests.Request;
import web.responses.Body;
import web.responses.Response;
import web.responses.entities.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class GetTestServlet extends Servlet{

    @Override
    public void service(Request request, Response response) throws Exception {
        String selectIdsSQL = "Select questionid FROM questions WHERE theme = ?";
        String selectQuestionSQL = "Select question, answer1, answer2, answer3," +
                " answer4, correct_answer FROM questions WHERE questionid = ?";

        String theme = request.getQueryParams()[0].split("=")[1];
        Integer amount = Integer.parseInt(request.getQueryParams()[1].split("=")[1]);

        try (Connection connection = DriverManager.getConnection(db.getURL(), db.getUser(), db.getPassword());
             PreparedStatement idStatement = connection.prepareStatement(selectIdsSQL)) {

            idStatement.setString(1, theme);
            ArrayList<Integer> questionIds = new ArrayList<Integer>();

            try (ResultSet resultSet = idStatement.executeQuery()) {
                while (resultSet.next()) {
                    questionIds.add(resultSet.getInt("questionid"));
                }
            }

            if (questionIds.size() < amount) {
                response.setStatus("400");
                response.setDescription("Недостаточно вопросов в базе");
                return;
            }

            // Случайные ID
            ArrayList<Integer> selectedIds = new ArrayList<>();
            Random rand = new Random();
            while (selectedIds.size() < amount) {
                int id = questionIds.get(rand.nextInt(questionIds.size()));
                if (!selectedIds.contains(id)) {
                    selectedIds.add(id);
                }
            }

            ArrayList<Body> questions = new ArrayList<>();
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

            response.setBody(questions);

        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            response.setStatus("400");
            response.setDescription("Registration failed");
        }
    }
}
