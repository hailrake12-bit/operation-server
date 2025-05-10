package web.entities;

import web.responses.Body;

import java.util.ArrayList;

public class TestThemes implements Body {
    private ArrayList<Theme> testThemes;

    public TestThemes(ArrayList<Theme> testThemes){
        this.testThemes = testThemes;
    }

    public ArrayList<Theme> getTheme() {
        return testThemes;
    }

    public void setTheme(ArrayList<Theme> theme) {
        this.testThemes = testThemes;;
    }

    @Override
    public String toJson() {
        return "currenly not working";
        //return String.format("{\n\t\"themeOfTest\":\"%s\"\n}", TestTheme);
    }
}
