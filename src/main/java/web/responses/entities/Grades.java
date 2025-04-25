package web.responses.entities;

import web.responses.Body;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


        List<String> gradesJson = new ArrayList<>();
        for(String key : grades.keySet()){
            gradesJson.add("\t{\"theme\":\"" + key + "\",\"grade\":\"" + grades.get(key)+ "\"}");
        }

        json.append("{\n");
        json.append("\"grades\":");
        if(gradesJson.size()>1) json.append("[\n");
        json.append(String.join(",\n", gradesJson));
        if(gradesJson.size()>1) json.append("\n\t]");
        json.append("\n}");

        return json.toString();
    }
}
