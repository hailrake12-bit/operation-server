package web;

public class Request {
    private String method;
    private String url;
    private String user;
    private String password;
    private String[] body;

    public Request(String url, String method, String user, String password) {
        this.method = method;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void printRequest(){
        System.out.println("method is " + method);
        String newUrl = url.replace(" ", "space");
        System.out.println("url is " + newUrl);
        System.out.println("user is" + user);
        System.out.println("password is" + password);
    }
}
