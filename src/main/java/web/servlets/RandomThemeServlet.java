package web.servlets;

import web.requests.Request;
import web.responses.Body;
import web.responses.Response;
import web.responses.entities.ThemeOfTest;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class RandomThemeServlet extends Servlet{

    @Override
    public void service(Request request, Response response) throws Exception {
        String selectThemeSQL = "SELECT DISTINCT theme FROM questions";

//        String theme = request.getBody()[0].split(":")[1];
//        Integer amount = Integer.parseInt(request.getBody()[1].split(":")[1]);

        try (Connection connection = DriverManager.getConnection(db.getURL(), db.getUser(), db.getPassword());
             PreparedStatement idStatement = connection.prepareStatement(selectThemeSQL)) {

            ArrayList<String> themes = new ArrayList<String>();

            try (ResultSet resultSet = idStatement.executeQuery()) {
                while (resultSet.next()) {
                    themes.add(resultSet.getString("theme"));
                }
            }

            if (themes.size() == 0) {
                response.setStatus("400");
                response.setDescription("questions DataBase is empty");
                return;
            }

            Random rand = new Random();
            ArrayList<Body> chosenThemeList = new ArrayList<>();
            ThemeOfTest chosenThemeOfTest = new ThemeOfTest();
            chosenThemeOfTest.setTheme(themes.get(rand.nextInt(themes.size())));
            chosenThemeList.add(chosenThemeOfTest);

            response.setBody(chosenThemeList);

        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            response.setStatus("400");
            response.setDescription("Registration failed");
        }
    }
}
