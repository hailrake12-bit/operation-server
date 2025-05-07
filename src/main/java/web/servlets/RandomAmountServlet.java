package web.servlets;

import web.requests.Request;
import web.responses.Body;
import web.responses.Response;
import web.responses.entities.AmountOfQuestions;

import java.sql.*;
import java.util.Random;

public class RandomAmountServlet extends Servlet{

    @Override
    public void service(Request request, Response response) throws Exception {
        String theme = request.getParams()[0].split("=")[1];
        String selectAmountSQL = "SELECT COUNT(*) FROM questions WHERE theme = ?";

        try (Connection connection = DriverManager.getConnection(db.getURL(), db.getUser(), db.getPassword());
             PreparedStatement amountStatement = connection.prepareStatement(selectAmountSQL)) {

            amountStatement.setString(1, theme);

            int amount = 0;
            try (ResultSet resultSet = amountStatement.executeQuery()) {
                if (resultSet.next()) {
                    amount = resultSet.getInt(1); // Получаем значение из первой колонки (COUNT(*))
                }
            }

            if (amount == 0) {
                response.setStatus("400");
                response.setDescription("questions DataBase is empty");
                return;
            }

            Random rand = new Random();
            int randAmount = rand.nextInt(amount-1) + 1;

            Body amountOfQuestions = new AmountOfQuestions(randAmount);

            response.setBody(amountOfQuestions);

        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            response.setStatus("400");
            response.setDescription("Registration failed");
        }
    }

}
