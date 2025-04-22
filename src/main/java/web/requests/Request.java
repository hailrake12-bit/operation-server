package web.requests;

public class Request {
    private String method;
    private String url;
    private String[] queryParams;
    private String [] body;

    public Request(String method, String url, String queryParams[], String[] body) {
        this.method = method;
        this.url = url;
        this.body = body;
        this.queryParams = queryParams;
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

    public String[] getQueryParams(){
        return queryParams;
    }

    public void printRequest(){
        System.out.println("method is " + method);
        System.out.println("url is " + url);
        if(queryParams!=null) {
            for(String queryParam : queryParams)
                System.out.println("query Params is " + queryParam);
        }
        if(body!=null){
            for(String bodyPart : body)
                System.out.println("body part is  " + bodyPart);
        }
    }
}
