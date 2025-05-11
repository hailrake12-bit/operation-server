package web.servlets;

import web.database.DataBase;
import web.requests.Request;
import web.responses.Response;
import web.entities.Book;
import web.entities.Books;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetBooksServlet implements Servlet{

    @Override
    public void service(Request request, Response response) throws Exception {
        String selectBookSQL = "SELECT theme, book, text FROM public.books";

        try(Connection connection = DriverManager.getConnection(DataBase.getURL(), DataBase.getUser(), DataBase.getPassword());
            PreparedStatement statement = connection.prepareStatement(selectBookSQL)){
            Books bookList = new Books(); //recheck possibility of using ArrayList<Book>

            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    Book book = new Book();
                    book.setTheme(resultSet.getString("theme"));
                    book.setName(resultSet.getString("book")); //rename field
                    book.setText(resultSet.getString("text"));
                    bookList.addBook(book);
                }
            }

            response.setBody(bookList);
        }
    }
}

//Refactored