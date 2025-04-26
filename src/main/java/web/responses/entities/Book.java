package web.responses.entities;

import web.responses.Body;

public class Book implements Body {
    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toJson() {
        return "{\n\t \"text\" : \"" + text + "\"\n}";
    }
}
