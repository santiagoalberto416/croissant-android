
package com.example.santiago.data.GraphicsConference;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerOption {

    @SerializedName("idAnswer")
    @Expose
    private Integer idAnswer;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("dateanswer")
    @Expose
    private String dateanswer;

    /**
     * 
     * @return
     *     The idAnswer
     */
    public Integer getIdAnswer() {
        return idAnswer;
    }

    /**
     * 
     * @param idAnswer
     *     The idAnswer
     */
    public void setIdAnswer(Integer idAnswer) {
        this.idAnswer = idAnswer;
    }

    /**
     * 
     * @return
     *     The answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * 
     * @param answer
     *     The answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * 
     * @return
     *     The dateanswer
     */
    public String getDateanswer() {
        return dateanswer;
    }

    /**
     * 
     * @param dateanswer
     *     The dateanswer
     */
    public void setDateanswer(String dateanswer) {
        this.dateanswer = dateanswer;
    }

}
