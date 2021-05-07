package pojo;

/**
 * Created by Akshay Chavan on 07,May,2021
 * akshaychavan.kkwedu@gmail.com
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalendarSession {

    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("available_capacity")
    @Expose
    private Integer availableCapacity;
    @SerializedName("min_age_limit")
    @Expose
    private Integer minAgeLimit;
    @SerializedName("vaccine")
    @Expose
    private String vaccine;
    @SerializedName("slots")
    @Expose
    private List<String> slots = null;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(Integer availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    public Integer getMinAgeLimit() {
        return minAgeLimit;
    }

    public void setMinAgeLimit(Integer minAgeLimit) {
        this.minAgeLimit = minAgeLimit;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public List<String> getSlots() {
        return slots;
    }

    public void setSlots(List<String> slots) {
        this.slots = slots;
    }

}