package web.servlets;

import web.requests.Request;
import web.database.DataBase;
import web.responses.Response;

public abstract class Servlet {
    protected DataBase db = new DataBase();

    public abstract void service(Request request, Response response) throws Exception;
}
