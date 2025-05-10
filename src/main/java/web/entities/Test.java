package web.entities;

import web.responses.Body;

import java.util.ArrayList;
import java.util.List;

public class Test implements Body{
    ArrayList<Question> test;

    public Test(ArrayList<Question> questionsList){
        test = questionsList;
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder();

        if (test.size() > 1) {
            json.append("[\n");
        }

        // Сохраняем все строки в список
        List<String> questionJsons = new ArrayList<>();
        for (Question question : test) {
            questionJsons.add(question.toJson());
        }

        // Соединяем все строки с запятой между ними
        json.append(String.join(",\n", questionJsons));

        if (test.size() > 1) {
            json.append("\n]");
        }

        return json.toString();
    }


}


