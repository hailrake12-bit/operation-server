package web.servlets;

import web.requests.Request;
import web.responses.Response;

import java.sql.*;

import static web.servlets.GetGradesServlet.getCurrentGrade;

public class UpdateGradeServlet extends Servlet {

    @Override
    public void service(Request request, Response response) throws Exception {
        String username = request.getQueryParams()[0].split("=")[1];

        String theme = request.getBody()[0].split(":")[1];
        int incorrectAnswers = Integer.parseInt(request.getBody()[1].split(":")[1]);
        int correctAnswers = Integer.parseInt(request.getBody()[2].split(":")[1]);


        String insertGradeSQL = """
        INSERT INTO users_grades (theme, grade, username)
        VALUES (?, ?, ?)
        ON CONFLICT (username, theme) DO UPDATE
        SET grade = EXCLUDED.grade;
        """;

        try (Connection connection = DriverManager.getConnection(db.getURL(), db.getUser(), db.getPassword());
             PreparedStatement stmt = connection.prepareStatement(insertGradeSQL)) {
            System.out.println(theme);
            stmt.setString(1, theme);

            double lastGrade = incorrectAnswers * 54.0 / correctAnswers;
            double currentGrade = getCurrentGrade(username, theme);
            System.out.println(currentGrade);
            double newGrade;
            if(currentGrade != 0) newGrade = (lastGrade + currentGrade) / 2.0;
            else newGrade = lastGrade;

            System.out.println(newGrade);
            System.out.println(username);
            stmt.setDouble(2, newGrade);
            stmt.setString(3, username);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            response.setStatus("400");
            response.setDescription("Registration failed");
        }
    }
}
