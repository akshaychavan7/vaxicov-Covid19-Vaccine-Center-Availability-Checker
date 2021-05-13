package com.akshaychavan.vaxicov.pojo;

/**
 * Created by Akshay Chavan on 13,May,2021
 * akshaychavan.kkwedu@gmail.com
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserRegistrationResponsePojo {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("httpStatus")
    @Expose
    private String httpStatus;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

}
