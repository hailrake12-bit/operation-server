package web.servlets;

import web.database.DataBase;
import web.requests.Request;
import web.responses.Response;
import web.entities.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GetCurrentBookByThemeServlet extends Servlet {

    @Override
    public void service(Request request, Response response) throws Exception {
        String theme = request.getParam("theme");
        String username = request.getParam("username");

        String getCurrentBookSQL = "SELECT book_name, is_read " +
                                    "FROM public.users_books " +
                                    "WHERE username = ? AND theme = ? " +
                                    "ORDER BY book_name";

        try (Connection connection = DriverManager.getConnection(DataBase.getURL(), DataBase.getUser(), DataBase.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(getCurrentBookSQL)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, theme);

            String currentBook = "book7";

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    if (!resultSet.getBoolean("is_read")) {
                        currentBook = resultSet.getString("book_name");
                        break;
                    }
                }
            }

            updateBook(connection, currentBook, username, theme);

            Book book = new Book();
            book.setName(currentBook);
            response.setBody(book);
        }
    }


//    private ArrayList<String> getBooksListByTheme(Connection connection, String theme) throws Exception{
//        String getBooksNamesSQL = "SELECT book FROM public.books WHERE theme = ?";
//        ArrayList<String> bookNames = new ArrayList<>();
//
//        try(PreparedStatement statement = connection.prepareStatement(getBooksNamesSQL)){
//            statement.setString(1, theme);
//
//            try(ResultSet resultSet = statement.executeQuery()){
//                while(resultSet.next()){
//                    String bookName = resultSet.getString("book");
//                    bookNames.add(bookName);
//                }
//            }
//
//            return bookNames;
//        }
//    }

    private void updateBook(Connection connection, String bookName, String username, String theme) throws Exception{
        String updateBookSQL = "UPDATE public.users_books " +
                                "SET is_read = TRUE " +
                                "WHERE username = ? AND theme = ? AND book_name = ?";

        try(PreparedStatement statement = connection.prepareStatement(updateBookSQL)){
         statement.setString(1, username);
         statement.setString(2, theme);
         statement.setString(3, bookName);
         statement.executeUpdate();
        }
    }
}
