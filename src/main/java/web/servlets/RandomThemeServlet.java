package web.servlets;

import web.requests.Request;
import web.responses.Body;
import web.responses.Response;
import web.responses.entities.Grade;
import web.responses.entities.Theme;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static web.servlets.GetGradesServlet.getCurrentGrade;

public class RandomThemeServlet extends Servlet{

    @Override
    public void service(Request request, Response response) throws Exception {
        String username = request.getParams()[0].split("=")[1];

        try (Connection connection = DriverManager.getConnection(db.getURL(), db.getUser(), db.getPassword())) {
            ArrayList<String> themes = getThemes(connection);

            if (themes.size() == 0) {
                response.setStatus("400");
                response.setDescription("questions DataBase is empty");
                return;
            }
            Random rand = new Random();
            Body chosenThemeOfTest = getSuitableTheme(connection, username, themes);

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

    private Body getSuitableTheme(Connection connection,String username, ArrayList<String> themes) throws SQLException{
        HashMap<String, Double> grades = new HashMap<>();
        Grade lastGrade, newGrade = new Grade(0);

        double randSum = 0;
        for(String theme : themes){
            lastGrade = new Grade(randSum);
            newGrade = getCurrentGrade(connection, username, theme);
            randSum = 54 - newGrade.getGrade() + lastGrade.getGrade();

            grades.put(theme, randSum);
        }

        Random random = new Random();
        int keyNum = random.nextInt((int) randSum);

        System.out.println(grades);
        System.out.println(keyNum);

        Body suitableTheme =  new Theme(themes.get(random.nextInt(themes.size())));
        for(String theme : themes){
            if(keyNum < grades.get(theme)) {
                suitableTheme = new Theme(theme);
                break;
            }
        }

        return suitableTheme;
    }
}
