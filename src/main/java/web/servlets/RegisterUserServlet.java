package web.servlets;

import web.database.DataBase;
import web.entities.Book;
import web.entities.Theme;
import web.requests.Request;
import web.responses.Response;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import static web.servlets.RandomThemeServlet.getThemes;

public class RegisterUserServlet implements Servlet {

    public void service(Request request, Response response) throws SQLException{
        String insertUserDataSQL = "INSERT INTO users (username, password) VALUES (?, ?)";

        String username = request.getBody()[0].split(":")[1];
        String password =  request.getBody()[1].split(":")[1];

        try (Connection connection = DriverManager.getConnection(DataBase.getURL(), DataBase.getUser(), DataBase.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(insertUserDataSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();

            addGrades(connection, username);
            addBooks(connection, username);

        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            response.setStatus("400");
            response.setDescription("Registration failed");
        }
    }

    private void addGrades(Connection connection, String username) throws SQLException{
        String insertGradesSQL = "INSERT INTO users_grades(username, theme, grade) values (?, ?, 0.0)";

        try (PreparedStatement stmt = connection.prepareStatement(insertGradesSQL)) {
            ArrayList<String> themes = getThemes(connection); //change to ArrayList<Theme>

            for(String theme : themes){
                stmt.setString(1, username);
                stmt.setString(2, theme);
                stmt.executeUpdate();
            }
        }
    }

    private void addBooks(Connection connection, String username) throws SQLException{
        String insertBooksSQL = "INSERT INTO users_books(username, theme, book_name) values(?, ?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(insertBooksSQL)){
            ArrayList<Book> books = getBookNames(connection);

            for(Book book : books){
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, book.getTheme());
                preparedStatement.setString(3, book.getName());
                preparedStatement.executeUpdate();
            }
        }
    }

    private ArrayList<Book> getBookNames(Connection connection) throws SQLException{
        String getBookNames = "SELECT theme, name FROM books";

        try(PreparedStatement preparedStatement = connection.prepareStatement(getBookNames)){
            ArrayList<Book> books = new ArrayList<>();

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    Book book = new Book();
                    book.setTheme(resultSet.getString("theme"));
                    book.setName(resultSet.getString("name"));
                    books.add(book);
                }
            }

            return books;
        }
    }
}
