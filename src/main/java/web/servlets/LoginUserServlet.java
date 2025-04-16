package web.servlets;

import web.Request;
import web.Response;

import java.io.Serializable;
import java.sql.*;

public class LoginUserServlet implements Servlet {
    private static final String URL = "jdbc:postgresql://db:5432/operation-server";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    public void service(Request request, Response response) throws SQLException {
        String selectSQL = "Select userid, username FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(selectSQL)) {

            stmt.setString(1, request.getUser());
            stmt.setString(2, request.getPassword());

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (!resultSet.next()) {
                    response.setStatus("400");
                    response.setDescription("Invalid user name or Password");
                }
            }

        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            response.setStatus("400");
            response.setDescription("Registration failed");
        }
    }
}
