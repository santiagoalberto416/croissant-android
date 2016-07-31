
package com.example.santiago.data.GraphicsConference;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Score {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("bestTime")
    @Expose
    private String bestTime;
    @SerializedName("worstTime")
    @Expose
    private String worstTime;
    @SerializedName("averageTime")
    @Expose
    private String averageTime;

    /**
     * 
     * @return
     *     The user
     */
    public User getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 
     * @return
     *     The points
     */
    public Integer getPoints() {
        return points;
    }

    /**
     * 
     * @param points
     *     The points
     */
    public void setPoints(Integer points) {
        this.points = points;
    }

    /**
     * 
     * @return
     *     The bestTime
     */
    public String getBestTime() {
        return bestTime;
    }

    /**
     * 
     * @param bestTime
     *     The bestTime
     */
    public void setBestTime(String bestTime) {
        this.bestTime = bestTime;
    }

    /**
     * 
     * @return
     *     The worstTime
     */
    public String getWorstTime() {
        return worstTime;
    }

    /**
     * 
     * @param worstTime
     *     The worstTime
     */
    public void setWorstTime(String worstTime) {
        this.worstTime = worstTime;
    }

    /**
     * 
     * @return
     *     The averageTime
     */
    public String getAverageTime() {
        return averageTime;
    }

    /**
     * 
     * @param averageTime
     *     The averageTime
     */
    public void setAverageTime(String averageTime) {
        this.averageTime = averageTime;
    }

}
