package web.responses.entities;

import web.responses.Body;

public class Grade implements Body {
    private double grade;

    public Grade(double grade){
        this.grade = grade;
    }

    public Grade(Grade oldGrade, Grade newGrade){
        this.grade = (oldGrade.getGrade() + newGrade.getGrade()) / 2.0 ;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public boolean isEmpty(){
        return (grade==0.0);
    }

    @Override
    public String toJson() {
        return "";
    }
}
