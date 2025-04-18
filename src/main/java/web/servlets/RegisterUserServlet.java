package web.servlets;

import web.Request;
import web.responses.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterUserServlet implements Servlet {

    private static final String URL = "jdbc:postgresql://localhost:5434/operation-server";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    public void service(Request request, Response response) throws SQLException{

        String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            stmt.setString(1, request.getBody()[0].split(":")[1]);
            stmt.setString(2, request.getBody()[1].split(":")[1]);

            stmt.executeUpdate();


        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            response.setStatus("400");
            response.setDescription("Registration failed");
        }
    }

}
