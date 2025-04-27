package web.database;

public class DataBase {
    private static final String URL = "jdbc:postgresql://localhost:5434/operation-server";
    private static final String user = "postgres";
    private static final String password = "password";

    public static String getURL() {
        return URL;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }
}
