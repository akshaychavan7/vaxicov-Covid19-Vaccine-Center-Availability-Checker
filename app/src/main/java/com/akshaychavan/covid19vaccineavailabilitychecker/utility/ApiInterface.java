package com.akshaychavan.covid19vaccineavailabilitychecker.utility;

/**
 * Created by Akshay Chavan on 06,May,2021
 * akshaychavan.kkwedu@gmail.com
 */


import pojo.CalendarByPinPojo;
import pojo.FindCenterByPinPojo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {

//    @Headers({"Content-Type: application/json; charset=utf-8", "Accept-Language: hi_IN", "User-Agent: PostmanRuntime/7.28.0", "Accept:*/*", "Accept-Encoding:gzip, deflate, br", "Connection:keep-alive"})
    @Headers("user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
    @GET("appointment/sessions/public/findByPin")
    Call<FindCenterByPinPojo> findCenterByPin(@Query("pincode") int pincode, @Query("date") String date);


    @Headers("user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
    @GET("appointment/sessions/public/calendarByPin")
    Call<CalendarByPinPojo> findCalendarByPin(@Query("pincode") int pincode, @Query("date") String date);

}
