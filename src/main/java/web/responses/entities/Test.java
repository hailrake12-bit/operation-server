package web.responses.entities;

import web.responses.Body;

import java.util.ArrayList;

public class Test implements Body{
    ArrayList<Question> test;

    public Test(ArrayList<Question> questionsList){
        test = questionsList;
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder();

        if(test.size()>1) json.append("[\n");
        for(Question question : test){
            json.append(question.toJson());
        }
        if(test.size()>1) json.append("]");

        return json.toString();
    }

}


