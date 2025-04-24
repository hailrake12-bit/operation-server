package web.servlets;

import web.requests.Request;
import web.responses.Body;
import web.responses.Response;
import web.responses.entities.TestThemes;
import web.responses.entities.Theme;

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

            ArrayList<String> themes = getThemes(connection);

            if (themes.size() == 0) {
                response.setStatus("400");
                response.setDescription("questions DataBase is empty");
                return;
            }

            Random rand = new Random();
            Body chosenThemeOfTest = new Theme(themes.get(rand.nextInt(themes.size())));

            response.setBody(chosenThemeOfTest);

        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            response.setStatus("400");
            response.setDescription("Registration failed");
        }
    }

    public static ArrayList<String> getThemes(Connection connection) throws SQLException {
        String selectThemeSQL = "SELECT DISTINCT theme FROM questions";

        try (PreparedStatement idStatement = connection.prepareStatement(selectThemeSQL)) {

            ArrayList<String> themes = new ArrayList<>();

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
}
