package web.database;

public class DataBase {
    private static final String URL = "jdbc:postgresql://localhost:5434/operation-server";
    private static final String user = "postgres";
    private static final String password = "password";

    public String getURL() {
        return URL;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
