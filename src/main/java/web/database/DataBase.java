package web.database;

public class DataBase {
    private String URL;
    private String user;
    private String password;
    private String name;

    public DataBase(String URL, String user, String password, String name) {
        this.URL = URL;
        this.user = user;
        this.password = password;
        this.name = name;
    }

    public String getURL() {
        return URL;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

}
