package web;

import web.requests.Request;
import web.responses.Response;
import web.servlets.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestDispatcher {
    private final BufferedWriter writer;
    private final BufferedReader reader;
    
    private final Map<String, Map<String, Servlet>> routes = new HashMap<>();

    public RequestDispatcher(BufferedWriter writer, BufferedReader reader) {
        this.writer = writer;
        this.reader = reader;

        Map<String, Servlet> registerRoutes = new HashMap<>();
        registerRoutes.put("POST", new RegisterUserServlet());
        routes.put("/register", registerRoutes);

        Map<String, Servlet> loginRoutes = new HashMap<>();
        loginRoutes.put("POST", new LoginUserServlet());
        routes.put("/login", loginRoutes);

        Map<String, Servlet> gradesRoutes = new HashMap<>();
        gradesRoutes.put("GET", new GetGradesServlet());
        gradesRoutes.put("PATCH", new UpdateGradeServlet());
        routes.put("/grades", gradesRoutes);

        Map<String, Servlet> testRoutes = new HashMap<>();
        testRoutes.put("GET", new GetTestServlet());
        routes.put("/tests", testRoutes);

        Map<String, Servlet> bookRoutes = new HashMap<>();
        bookRoutes.put("GET", new GetBooksServlet());
        routes.put("/books", bookRoutes);

        Map<String, Servlet> userBookRoutes = new HashMap<>();
        userBookRoutes.put("GET", new GetCurrentBookByThemeServlet());
        routes.put("/books/user", userBookRoutes);

        Map<String, Servlet> themeRoutes = new HashMap<>();
        themeRoutes.put("GET", new RandomThemeServlet());
        routes.put("/tests/themes", themeRoutes);

        Map<String, Servlet> amountOfQuestionsRoutes = new HashMap<>();
        amountOfQuestionsRoutes.put("GET", new RandomAmountServlet());
        routes.put("/tests/amount", amountOfQuestionsRoutes);
    }

    public void dispatch() throws Exception{
        readRequest(reader);

        Request request = parseRequest();
        Response response = new Response();

        //request.printRequest();

        Map<String,Servlet> methodRoutes = routes.get(request.getUrl());

        if(methodRoutes == null){
            response.setStatus("404");
            response.setDescription("No such path");

            sendResponse(response);

            return;
        }

        Servlet servlet = methodRoutes.get(request.getMethod());

        if (servlet == null) {
            response.setStatus("405");
            response.setDescription("Method Not Allowed");

            sendResponse(response);

            return;
        }

        servlet.service(request, response);
        sendResponse(response);

    }

    private void sendResponse(Response response) throws IOException {
        writer.write("HTTP/1.1 " + response.getStatus() + " " + response.getDescription() + "\r\n\r\n");
        if(response.getBody()!=null){
            writer.write(response.writeBody());
        }
        writer.flush();
    }

    private Request parseRequest() throws Exception {
        List<String> request = new ArrayList<>();
        StringBuilder bodyBuilder = new StringBuilder();

        String method = null;
        String path = null;
        String[] queryParams = null;

        String[] requestParts;
        String[] requestLine;

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            request.add(line);
        }
        while (reader.ready()) {
            bodyBuilder.append((char) reader.read());
        }

        if(!request.isEmpty()){
            requestLine = request.get(0).split(" ");
            method = requestLine[0];
            path = requestLine[1].split("\\?")[0];

            requestParts = requestLine[1].split("\\?", 2);

            if (requestParts.length == 2) {
                queryParams = requestParts[1].split("&");
            }
        }

        //make working with stream
        String bodyRaw = bodyBuilder.toString();
        String[] body = bodyRaw.replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .replace(" ", "")
                .replace("\r\n", "")
                .split(",");

        return new Request(method, path, queryParams, body);
    }

    public static void readRequest(BufferedReader reader) throws Exception{
        String line;
        while((line = reader.readLine())!=null){
            System.out.println(line);
        }
    }
}
