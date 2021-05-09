package com.akshaychavan.vaxicov.pojo;

/**
 * Created by Akshay Chavan on 07,May,2021
 * akshaychavan.kkwedu@gmail.com
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CalendarByPinPojo {

    @SerializedName("centers")
    @Expose
    private List<Center> centers = null;

    public List<Center> getCenters() {
        return centers;
    }

    public void setCenters(List<Center> centers) {
        this.centers = centers;
    }

}
