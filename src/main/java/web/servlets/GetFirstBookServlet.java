package web.servlets;

import web.database.DataBase;
import web.requests.Request;
import web.responses.Response;
import web.responses.entities.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetFirstBookServlet extends Servlet{

    @Override
    public void service(Request request, Response response) throws Exception {
        String theme = request.getQueryParams()[0].split("=")[1];
        Book book = new Book();

        String selectBookSQL = "SELECT text FROM public.books WHERE theme = ? AND book = 'book1'";

        try(Connection connection = DriverManager.getConnection(DataBase.getURL(), DataBase.getUser(), DataBase.getPassword());
            PreparedStatement statement = connection.prepareStatement(selectBookSQL)){

            statement.setString(1, theme);


            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    book.setText(resultSet.getString(1));
                }
            }

            response.setBody(book);
        }
    }
}
