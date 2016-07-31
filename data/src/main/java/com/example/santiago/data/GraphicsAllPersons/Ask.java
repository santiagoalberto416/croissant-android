
package com.example.santiago.data.GraphicsAllPersons;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ask {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("correctAnswer")
    @Expose
    private Integer correctAnswer;
    @SerializedName("totalCorrectPersons")
    @Expose
    private Integer totalCorrectPersons;
    @SerializedName("totalWrongPersons")
    @Expose
    private Integer totalWrongPersons;
    @SerializedName("answerOptions")
    @Expose
    private List<AnswerOption> answerOptions = new ArrayList<AnswerOption>();

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * 
     * @param question
     *     The question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * 
     * @return
     *     The date
     */
    public String getDate() {
        return date;
    }

    /**
     * 
     * @param date
     *     The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 
     * @return
     *     The correctAnswer
     */
    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * 
     * @param correctAnswer
     *     The correctAnswer
     */
    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * 
     * @return
     *     The totalCorrectPersons
     */
    public Integer getTotalCorrectPersons() {
        return totalCorrectPersons;
    }

    /**
     * 
     * @param totalCorrectPersons
     *     The totalCorrectPersons
     */
    public void setTotalCorrectPersons(Integer totalCorrectPersons) {
        this.totalCorrectPersons = totalCorrectPersons;
    }

    /**
     * 
     * @return
     *     The totalWrongPersons
     */
    public Integer getTotalWrongPersons() {
        return totalWrongPersons;
    }

    /**
     * 
     * @param totalWrongPersons
     *     The totalWrongPersons
     */
    public void setTotalWrongPersons(Integer totalWrongPersons) {
        this.totalWrongPersons = totalWrongPersons;
    }

    /**
     * 
     * @return
     *     The answerOptions
     */
    public List<AnswerOption> getAnswerOptions() {
        return answerOptions;
    }

    /**
     * 
     * @param answerOptions
     *     The answerOptions
     */
    public void setAnswerOptions(List<AnswerOption> answerOptions) {
        this.answerOptions = answerOptions;
    }

}
