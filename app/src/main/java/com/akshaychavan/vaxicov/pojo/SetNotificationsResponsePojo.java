package com.akshaychavan.vaxicov.pojo;

/**
 * Created by Akshay Chavan on 13,May,2021
 * akshaychavan.kkwedu@gmail.com
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetNotificationsResponsePojo {

    @SerializedName("notificationId")
    @Expose
    private String notificationId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("center")
    @Expose
    private Integer center;
    @SerializedName("minAgeLimit")
    @Expose
    private Integer minAgeLimit;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("vaccine")
    @Expose
    private String vaccine;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("notificationTriggeredDate")
    @Expose
    private Object notificationTriggeredDate;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Integer getCenter() {
        return center;
    }

    public void setCenter(Integer center) {
        this.center = center;
    }

    public Integer getMinAgeLimit() {
        return minAgeLimit;
    }

    public void setMinAgeLimit(Integer minAgeLimit) {
        this.minAgeLimit = minAgeLimit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getNotificationTriggeredDate() {
        return notificationTriggeredDate;
    }

    public void setNotificationTriggeredDate(Object notificationTriggeredDate) {
        this.notificationTriggeredDate = notificationTriggeredDate;
    }

}
