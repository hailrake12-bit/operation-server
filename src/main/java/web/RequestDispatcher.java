package web;

import web.database.DataBase;
import web.servlets.LoginUserServlet;
import web.servlets.RegisterUserServlet;
import web.servlets.Servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RequestDispatcher {
    private BufferedWriter writer;
    private BufferedReader reader;

    private final Map<String, Servlet> routes = new HashMap<>();

    public RequestDispatcher(BufferedWriter writer, BufferedReader reader) {
        this.writer = writer;
        this.reader = reader;

        routes.put("/register", new RegisterUserServlet());
        routes.put("/login", new LoginUserServlet());
    }

    public void dispatch() throws Exception{

        Request request = parseRequest();
        Response response = new Response();

        Servlet servlet = routes.get(request.getUrl());

        if(servlet == null){
            response.setStatus("404");
            response.setDescription("No such path");
        }

        servlet.service(request, response);

        writer.write("HTTP/1.1 " + response.getStatus() + " " + response.getDescription() + "\r\n\r\n");
        writer.flush();
    }

    public void testDB(){
        String URL = "jdbc:postgresql://localhost:5433/mydb";
        String USER = "postgres";
        String PASSWORD = "postgres";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Успешное подключение к PostgreSQL!");
        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
        }
    }


    private Request parseRequest() throws Exception {
        List<String> lines = new ArrayList<>();
        String line;

        // 1. Чтение всех строк запроса
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            lines.add(line);
        }

        // 2. Чтение тела запроса (POST-данные)
        StringBuilder bodyBuilder = new StringBuilder();
        while (reader.ready()) {
            bodyBuilder.append((char) reader.read());
        }

        // 3. Обработка первой строки (например: "POST /addUser HTTP/1.1")
        String[] requestLine = lines.get(0).split(" ");
        String method = requestLine[0];
        String path = requestLine[1];

        // 4. Обработка тела как JSON (можно использовать библиотеку, но пока просто разберём на строки)
        String bodyRaw = bodyBuilder.toString();
        String[] body = bodyRaw.replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .split(",");

        return new Request(path, method, body[0], body[1]);
    }

}
