package web;

public class Request {
    private String method;
    private String url;
    private String[] body;

    public Request(String url, String method, String body[]) {
        this.method = method;
        this.url = url;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String[] getBody() {
        return body;
    }

    public void printRequest(){
        System.out.println("method is " + method);
        String newUrl = url.replace(" ", "space");
        System.out.println("url is " + newUrl);
    }
}
