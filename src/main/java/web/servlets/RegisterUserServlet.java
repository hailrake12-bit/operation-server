package web.servlets;

import web.requests.Request;
import web.responses.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static web.servlets.RandomThemeServlet.getThemes;

public class RegisterUserServlet extends Servlet {

    public void service(Request request, Response response) throws SQLException{

        String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";
        String username = request.getBody()[0].split(":")[1];

        try (Connection connection = DriverManager.getConnection(db.getURL(), db.getUser(), db.getPassword());
             PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            stmt.setString(1, username);
            stmt.setString(2, request.getBody()[1].split(":")[1]);

            stmt.executeUpdate();

            addGrades(username);

        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            response.setStatus("400");
            response.setDescription("Registration failed");
        }
    }

    private void addGrades(String username) throws SQLException{
        String insertGradesSQL = "INSERT INTO users_grades(username, theme, grade) values (?, ?, 0.0)";

        try (Connection connection = DriverManager.getConnection(db.getURL(), db.getUser(), db.getPassword());
             PreparedStatement stmt = connection.prepareStatement(insertGradesSQL)) {

            ArrayList<String> themes = getThemes(connection);

            for(String theme : themes){
                stmt.setString(1, username);
                stmt.setString(2, theme);
                stmt.executeUpdate();
            }
        }
    }
}
