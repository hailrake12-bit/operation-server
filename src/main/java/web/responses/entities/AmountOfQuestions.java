package web.responses.entities;

import web.responses.Body;

public class AmountOfQuestions implements Body {
    int amountOfQuestions;

    public AmountOfQuestions(int amountOfQuestions){
        this.amountOfQuestions = amountOfQuestions;
    }

    @Override
    public String toJson() {
        return String.format("{\n\t\"amountOfQuestions\":\"%s\"\n}", amountOfQuestions);
    }
}
