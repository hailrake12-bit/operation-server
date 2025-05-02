package web.responses.entities;

import web.responses.Body;

public class Book implements Body {
    String name;
    String theme;
    String text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toJson() {
        if (text!=null) {
            return "{\n" +
                    "  \"Book\": {\n" +
                    "    \"name\": \"" + name + "\",\n" +
                    "    \"theme\": \"" + theme + "\",\n" +
                    "    \"text\": \"" + text + "\"\n" +
                    "  }\n" +
                    "}";
        } else{
            return "{\n" +
                    " \"bookName\":\"" + name + "\" \n" +
                    "}\n";
        }
    }

}
