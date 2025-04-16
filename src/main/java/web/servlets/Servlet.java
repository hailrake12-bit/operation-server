package web.servlets;

import web.Request;
import web.Response;
import web.database.DataBase;

public interface Servlet {
    void service(Request req, Response rep) throws Exception;
}
