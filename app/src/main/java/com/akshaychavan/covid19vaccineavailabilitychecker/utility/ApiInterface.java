package com.akshaychavan.covid19vaccineavailabilitychecker.utility;

/**
 * Created by Akshay Chavan on 06,May,2021
 * akshaychavan.kkwedu@gmail.com
 */


import pojo.FindCenterByPinPojo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("appointment/sessions/public/findByPin")
    Call<FindCenterByPinPojo> findCenterByPin(@Query("pincode") int pincode, @Query("date") String date);


}
