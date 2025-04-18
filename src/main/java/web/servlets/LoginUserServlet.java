package web.servlets;

import web.Request;
import web.database.DataBase;
import web.responses.Response;

import java.sql.*;

public class LoginUserServlet extends Servlet {

    public void service(Request request, Response response) throws SQLException {
        String selectSQL = "Select userid, username FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(db.getURL(), db.getUser(), db.getPassword());
             PreparedStatement stmt = connection.prepareStatement(selectSQL)) {

            stmt.setString(1, request.getBody()[0].split(":")[1]);
            stmt.setString(2, request.getBody()[1].split(":")[1]);

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
