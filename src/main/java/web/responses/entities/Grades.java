package web.responses.entities;

import web.responses.Body;

public class Grades implements Body {


    @Override
    public String toJson() {
        return String.format("{\n\t \"question\":\"%s\",\n\t \"answer1\":\"%s\", \n\t \"answer2\":\"%s\", \n\t \"answer3\":\"%s\",\n\t \"answer4\":\"%s\", \n\t \"correctAnswer\":\"%s\" \n}",
                question, answer1, answer2, answer3, answer4, correctAnswer);
    }
}
