package web.servlets;

import web.database.DataBase;
import web.requests.Request;
import web.responses.Response;
import web.entities.Grade;

import java.sql.*;

import static web.servlets.GetGradesServlet.getCurrentGrade;

public class UpdateGradeServlet implements Servlet {

    @Override
    public void service(Request request, Response response) throws Exception {
        String username = request.getParam("username");

        String theme = request.getBody()[0].split(":")[1];
        int incorrectAnswers = Integer.parseInt(request.getBody()[1].split(":")[1]);
        int questionsAmount = Integer.parseInt(request.getBody()[2].split(":")[1]);

        String insertGradeSQL = """
        INSERT INTO users_grades (theme, grade, username)
        VALUES (?, ?, ?)
        ON CONFLICT (username, theme) DO UPDATE
        SET grade = EXCLUDED.grade;
        """;

        try (Connection connection = DriverManager.getConnection(DataBase.getURL(), DataBase.getUser(), DataBase.getPassword());
             PreparedStatement stmt = connection.prepareStatement(insertGradeSQL)) {
            stmt.setString(1, theme);

            Grade lastGrade = new Grade((questionsAmount-incorrectAnswers) * 54.0 / questionsAmount);
            Grade currentGrade = getCurrentGrade(connection, username, theme);
            Grade newGrade;
            if(!currentGrade.isEmpty()) newGrade = new Grade(lastGrade,currentGrade);
            else newGrade = lastGrade;

            stmt.setDouble(2, newGrade.getGrade());
            stmt.setString(3, username);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            response.setStatus("400");
            response.setDescription("Registration failed");
        }
    }
}
