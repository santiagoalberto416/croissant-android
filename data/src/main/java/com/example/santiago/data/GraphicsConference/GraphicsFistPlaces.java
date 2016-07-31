
package com.example.santiago.data.GraphicsConference;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GraphicsFistPlaces {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("countQuestions")
    @Expose
    private Integer countQuestions;
    @SerializedName("ask")
    @Expose
    private Ask ask;
    @SerializedName("scores")
    @Expose
    private List<Score> scores = new ArrayList<Score>();

    /**
     * 
     * @return
     *     The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The countQuestions
     */
    public Integer getCountQuestions() {
        return countQuestions;
    }

    /**
     * 
     * @param countQuestions
     *     The countQuestions
     */
    public void setCountQuestions(Integer countQuestions) {
        this.countQuestions = countQuestions;
    }

    /**
     * 
     * @return
     *     The ask
     */
    public Ask getAsk() {
        return ask;
    }

    /**
     * 
     * @param ask
     *     The ask
     */
    public void setAsk(Ask ask) {
        this.ask = ask;
    }

    /**
     * 
     * @return
     *     The scores
     */
    public List<Score> getScores() {
        return scores;
    }

    /**
     * 
     * @param scores
     *     The scores
     */
    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

}
