package web.servlets;

import web.database.DataBase;
import web.requests.Request;
import web.responses.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GetCurrentBookByThemeServlet extends Servlet {

    @Override
    public void service(Request request, Response response) throws Exception {
        String theme = request.getQueryParams()[0].split("=")[1];
        String username = request.getQueryParams()[1].split("=")[1];

        String getCurrentBookSQL = "SELECT * FROM public.users_grades WHERE username = ? and theme = ?";

        try(Connection connection = DriverManager.getConnection(DataBase.getURL(),DataBase.getUser(), DataBase.getPassword());
        PreparedStatement statement = connection.prepareStatement(getCurrentBookSQL)){
            ArrayList<String> bookNames = getBooksListByTheme(connection, theme);

            System.out.println(bookNames);

            statement.setString(1, username);
            statement.setString(2, theme);
            String currentBook = null;
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

            System.out.println(currentBook);
        }
    }

    private ArrayList<String> getBooksListByTheme(Connection connection, String theme) throws Exception{
        String getBooksNamesSQL = "SELECT book FROM public.books WHERE theme = ?";
        ArrayList<String> bookNames = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement(getBooksNamesSQL)){
            statement.setString(1, theme);

            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    bookNames.add(resultSet.getString("book"));
                }
            }

            return bookNames;
        }
    }
}
