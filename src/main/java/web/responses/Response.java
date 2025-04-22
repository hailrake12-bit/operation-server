package web.responses;

import java.util.ArrayList;

public class Response {
    String status = "200";
    String description = "OK";
    String content;
    ArrayList<Body> body;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBody(ArrayList<Body> body) {
        this.body = body;
    }

    public ArrayList<Body> getBody() {
        return body;
    }

    public StringBuilder toJson(){
        StringBuilder sb = new StringBuilder();
        if(body.size() > 1) sb.append("[\n\t");
        for (int i = 0; i < body.size(); i++) {
            sb.append(body.get(i).toJson());
            if (i < body.size() - 1) sb.append(",\n");
        }
        if(body.size() > 1) sb.append("\n\t]");
        return sb;
    }
}
