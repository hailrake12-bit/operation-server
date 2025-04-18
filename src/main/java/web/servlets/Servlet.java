package web.servlets;

import web.Request;
import web.responses.Response;

public interface Servlet {
    void service(Request request, Response response) throws Exception;
}
