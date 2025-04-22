package web.responses.entities;

import web.responses.Body;

public class ThemeOfTest implements Body {
    private String themeOfTest;

    public String getTheme() {
        return themeOfTest;
    }

    public void setTheme(String theme) {
        this.themeOfTest = theme;
    }

    @Override
    public String toJson() {
        return String.format("{\n\t\"themeOfTest\":\"%s\"\n}", themeOfTest);
    }
}
