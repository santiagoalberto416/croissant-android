
package com.example.santiago.data.Event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Theme {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("primary")
    @Expose
    private String primary;
    @SerializedName("primary_dark")
    @Expose
    private String primaryDark;
    @SerializedName("primary_light")
    @Expose
    private String primaryLight;
    @SerializedName("ascent")
    @Expose
    private String ascent;
    @SerializedName("icons")
    @Expose
    private String icons;

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
     *     The primary
     */
    public String getPrimary() {
        return primary;
    }

    /**
     * 
     * @param primary
     *     The primary
     */
    public void setPrimary(String primary) {
        this.primary = primary;
    }

    /**
     * 
     * @return
     *     The primaryDark
     */
    public String getPrimaryDark() {
        return primaryDark;
    }

    /**
     * 
     * @param primaryDark
     *     The primary_dark
     */
    public void setPrimaryDark(String primaryDark) {
        this.primaryDark = primaryDark;
    }

    /**
     * 
     * @return
     *     The primaryLight
     */
    public String getPrimaryLight() {
        return primaryLight;
    }

    /**
     * 
     * @param primaryLight
     *     The primary_light
     */
    public void setPrimaryLight(String primaryLight) {
        this.primaryLight = primaryLight;
    }

    /**
     * 
     * @return
     *     The ascent
     */
    public String getAscent() {
        return ascent;
    }

    /**
     * 
     * @param ascent
     *     The ascent
     */
    public void setAscent(String ascent) {
        this.ascent = ascent;
    }

    /**
     * 
     * @return
     *     The icons
     */
    public String getIcons() {
        return icons;
    }

    /**
     * 
     * @param icons
     *     The icons
     */
    public void setIcons(String icons) {
        this.icons = icons;
    }

}
