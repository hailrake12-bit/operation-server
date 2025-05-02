package web.servlets;

import web.requests.Request;
import web.responses.Body;
import web.responses.Response;
import web.responses.entities.Grade;
import web.responses.entities.Grades;
import web.responses.entities.Theme;

import java.sql.*;
import java.util.ArrayList;
import java.util.zip.CheckedOutputStream;

public class GetGradesServlet extends Servlet{
    @Override
    public void service(Request request, Response response) throws Exception {
        String selectGradesSQL = "SELECT grade, theme FROM users_grades WHERE username = ?";

        Grades grades = new Grades();

        String username = request.getQueryParams()[0].split("=")[1];
        try (Connection connection = DriverManager.getConnection(db.getURL(), db.getUser(), db.getPassword());
             PreparedStatement stmt = connection.prepareStatement(selectGradesSQL)) {

            stmt.setString(1, username);

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    String theme = resultSet.getString("theme");
                    Double grade = resultSet.getDouble("grade");
                    grades.put(theme, grade);
                }
            }


            response.setBody(grades);
        }
    }


    public static Grade getCurrentGrade(Connection connection, String username, String theme) throws SQLException {
        String getGradeSQL = "SELECT grade FROM users_grades WHERE username = ? AND theme = ?";

        try (PreparedStatement stmt = connection.prepareStatement(getGradeSQL)) {

            Grade grade = new Grade(0.0);
            stmt.setString(1, username);
            stmt.setString(2, theme);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    grade.setGrade(resultSet.getInt(1));
                }
            }

            return grade;
        }
    }
}
