package web.responses.entities;

import web.responses.Body;

import java.util.HashMap;
import java.util.Map;

public class Grades implements Body {
    private Map<String, Double> grades;

    public Grades(){
        grades = new HashMap<>();
    }

    public Grades(Map<String, Double> grades) {
        this.grades = grades;
    }

    public void put(String theme, Double grade){
        grades.put(theme, grade);
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder();

        json.append("{\n");

        for(String key : grades.keySet()){
            json.append("\t\""+ key + "\":\"" + grades.get(key) + "\"\n");
        }

        json.append("}");

        return json.toString();
    }
}
