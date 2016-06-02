package com.croissant.croissant;

import java.util.List;

/**
 * Created by Santiago on 11/03/2016.
 */
public class Question {

    private int id;
    private String question;
    private String dateOfQuestion;
    private String user;
    private List<Answer> answers;

    //methods
    public int getId() {return this.id;}
    public String getQuestion() {return this.question;}
    public String getDateOfQuestion() {return this.dateOfQuestion;}
    public String getUser() {return this.user;}
    public void setQuestion(String question) {
        this.question = question;
    }

    //constructor
    public Question()
    {
        this.id = 0;
        this.question = "";
        this.dateOfQuestion = "";
        this.user = "";
    }

    public Question(int id,String question, String dateOfQuestion, String user)
    {
        this.id = id;
        this.question = question;
        this.dateOfQuestion = dateOfQuestion;
        this.user = user;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
