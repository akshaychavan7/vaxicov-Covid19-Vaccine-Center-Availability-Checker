package com.akshaychavan.vaxicov.pojo;

/**
 * Created by Akshay Chavan on 13,May,2021
 * akshaychavan.kkwedu@gmail.com
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserLoginResponsePojo {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
