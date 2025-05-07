package web.requests;

public class Request {
    private String method;
    private String url;
    private String[] params;
    private String [] body;

    public Request(String method, String url, String params[], String[] body) {
        this.method = method;
        this.url = url;
        this.body = body;
        this.params = params;
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

    public String[] getParams(){
        return params;
    }

    public String getParam(String name){
        if (name == null) {
            throw new IllegalArgumentException("Parameter name cannot be null");
        }

        for(String param : params){
            String key = param.split("=")[0];
            String value = param.split("=")[1];

            if (name.equals(key)) return value;
        }
        return null;
    }

    public void printRequest(){
        System.out.println("method is " + method);
        System.out.println("url is " + url);
        if(params !=null) {
            for(String queryParam : params)
                System.out.println("query Params is " + queryParam);
        }
        if(body!=null){
            for(String bodyPart : body)
                System.out.println("body part is  " + bodyPart);
        }
    }
}
