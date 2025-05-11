package web.servlets;

import web.database.DataBase;
import web.requests.Request;
import web.responses.Response;

import java.sql.*;

public class LoginUserServlet implements Servlet {

    public void service(Request request, Response response) throws SQLException {
        String selectSQL = "Select username FROM users WHERE username = ? AND password = ?";
        String username = request.getBody()[0].split(":")[1];
        String password = request.getBody()[1].split(":")[1];

        try (Connection connection = DriverManager.getConnection(DataBase.getURL(), DataBase.getUser(), DataBase.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
