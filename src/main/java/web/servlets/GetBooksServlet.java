package web.servlets;

import web.database.DataBase;
import web.requests.Request;
import web.responses.Response;
import web.responses.entities.Book;
import web.responses.entities.Books;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GetBooksServlet extends Servlet{

    @Override
    public void service(Request request, Response response) throws Exception {
        Book book;
        Books bookList = new Books();

        String selectBookSQL = "SELECT * FROM public.books ";

        try(Connection connection = DriverManager.getConnection(DataBase.getURL(), DataBase.getUser(), DataBase.getPassword());
            PreparedStatement statement = connection.prepareStatement(selectBookSQL)){

            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    book = new Book();
                    book.setTheme(resultSet.getString(1));
                    book.setName(resultSet.getString(2));
                    book.setText(resultSet.getString(3));
                    bookList.addBook(book);
                }
            }

            response.setBody(bookList);
        }
    }
}
