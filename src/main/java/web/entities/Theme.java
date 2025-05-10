package web.entities;

import web.responses.Body;

public class Theme implements Body {
    private String theme;

    public Theme(String theme) {
        this.theme = theme;
    }

    @Override
    public String toJson() {
        return String.format("{\n\t\"theme\":\"%s\"\n}", theme);
    }
}
