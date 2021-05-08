package pojo;

/**
 * Created by Akshay Chavan on 08,May,2021
 * akshaychavan.kkwedu@gmail.com
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CalendarByDistrictPojo {

    @SerializedName("centers")
    @Expose
    private List<CenterDistrict> centers = null;

    public List<CenterDistrict> getCenters() {
        return centers;
    }

    public void setCenters(List<CenterDistrict> centers) {
        this.centers = centers;
    }

}