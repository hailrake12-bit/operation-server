package web.servlets;

import web.requests.Request;
import web.responses.Body;
import web.responses.Response;
import web.responses.entities.AmountOfQuestions;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class CollectTestInfoServlet extends Servlet {

    @Override
    public void service(Request request, Response response) throws Exception {
        String username = request.getQueryParams()[0].split("=")[1];

        String theme = request.getBody()[0].split(":")[1];
        int CorrectAnswers = Integer.parseInt(request.getBody()[1].split(":")[1]);
        int IncorrectAnswers = Integer.parseInt(request.getBody()[1].split(":")[1]);


        String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(db.getURL(), db.getUser(), db.getPassword());
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
