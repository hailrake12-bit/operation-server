package web.servlets;

import web.database.DataBase;
import web.requests.Request;
import web.responses.Response;
import web.responses.entities.Book;

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

        String getCurrentBookSQL = "SELECT * FROM public.users_grades WHERE username = ? and theme = ?";

        try(Connection connection = DriverManager.getConnection(DataBase.getURL(),DataBase.getUser(), DataBase.getPassword());
        PreparedStatement statement = connection.prepareStatement(getCurrentBookSQL)){
            ArrayList<String> bookNames = getBooksListByTheme(connection, theme);

            statement.setString(1, username);
            statement.setString(2, theme);
            String currentBook = bookNames.get(bookNames.size()-1);
            try(ResultSet resultSet = statement.executeQuery()){

                if (resultSet.next()) {
                    for(String bookName : bookNames){
                        if(!resultSet.getBoolean(bookName)) {
                            currentBook = bookName;
                            break;
                        }
                    }
                }
            }
            updateBook(connection, currentBook, username, theme);

            Book book = new Book();
            book.setName(currentBook);
            response.setBody(book);
        }
    }

    private ArrayList<String> getBooksListByTheme(Connection connection, String theme) throws Exception{
        String getBooksNamesSQL = "SELECT book FROM public.books WHERE theme = ?";
        ArrayList<String> bookNames = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement(getBooksNamesSQL)){
            statement.setString(1, theme);

            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    String bookName = resultSet.getString("book");
                    bookNames.add(bookName);
                }
            }

            return bookNames;
        }
    }

    private void updateBook(Connection connection, String bookName, String username, String theme) throws Exception{
        String updateBookSQL = "UPDATE public.users_grades SET " + bookName + " = TRUE WHERE username = ? AND theme = ?";

        try(PreparedStatement statement = connection.prepareStatement(updateBookSQL)){
         statement.setString(1, username);
         statement.setString(2, theme);
         statement.executeUpdate();
        }
    }
}
