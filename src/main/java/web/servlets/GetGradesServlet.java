package web.servlets;

import web.requests.Request;
import web.responses.Response;

import java.sql.*;
import java.util.ArrayList;

public class GetGradesServlet extends Servlet{
    @Override
    public void service(Request request, Response response) throws Exception {
        String selectGradesSQL = "SELECT grade, theme FROM user_grades WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(db.getURL(), db.getUser(), db.getPassword());
             PreparedStatement stmt = connection.prepareStatement(selectGradesSQL)) {

            ArrayList<> themes = new ArrayList<>();

            try (ResultSet resultSet = idStatement.executeQuery()) {
                while (resultSet.next()) {
                    themes.add(resultSet.getString("theme"));
                }
            }

            if (themes.size() == 0) {
                return null;
            }

            return themes;
        }
    }


    public static double getCurrentGrade(String username, String theme) throws SQLException {
        String getGradeSQL = "SELECT grade FROM users_grades WHERE username = ? AND theme = ?";

        try (Connection connection = DriverManager.getConnection(db.getURL(), db.getUser(), db.getPassword());
        PreparedStatement stmt = connection.prepareStatement(getGradeSQL)) {

            double grade = 0.0;
            stmt.setString(1, username);
            stmt.setString(2, theme);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    grade = resultSet.getInt(1);
                }
            }

            return grade;
        }
    }
}
