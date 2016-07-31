
package com.example.santiago.data.GraphicsAllPersons;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Graphics {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("asks")
    @Expose
    private List<Ask> asks = new ArrayList<Ask>();

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
     *     The asks
     */
    public List<Ask> getAsks() {
        return asks;
    }

    /**
     * 
     * @param asks
     *     The asks
     */
    public void setAsks(List<Ask> asks) {
        this.asks = asks;
    }

}
