package web.servlets;

import web.database.DataBase;
import web.requests.Request;
import web.responses.Response;
import web.entities.Grade;
import web.entities.Grades;

import java.sql.*;

public class GetGradesServlet implements Servlet{
    @Override
    public void service(Request request, Response response) throws Exception {
        String selectGradesSQL = "SELECT grade, theme FROM users_grades WHERE username = ?";

        String username = request.getParam("username");
        try (Connection connection = DriverManager.getConnection(DataBase.getURL(), DataBase.getUser(), DataBase.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(selectGradesSQL)) {

            Grades grades = new Grades();
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
