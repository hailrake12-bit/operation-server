package web.responses.entities;

import web.responses.Body;

public class Question implements Body{
    String question;
    String answer1;
    String answer2;
    String answer3;
    String answer4;
    String correctAnswer;

    public Question(String question, String answer1, String answer2, String answer3,
                    String answer4, String correct_answer) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correctAnswer = correct_answer;
    }

    public String toJson(){
        return String.format("\t{\n\t \"question\":\"%s\",\n\t \"answer1\":\"%s\", \n\t \"answer2\":\"%s\", \n\t \"answer3\":\"%s\",\n\t \"answer4\":\"%s\", \n\t \"correctAnswer\":\"%s\" \n\t}",
                question, answer1, answer2, answer3, answer4, correctAnswer);
    }
}
