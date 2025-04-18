package web;

import web.database.DataBaseInitializer;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(4445);
        DataBaseInitializer.Initialize();
        server.start();
    }
}