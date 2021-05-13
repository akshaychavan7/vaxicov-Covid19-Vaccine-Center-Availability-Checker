package com.akshaychavan.vaxicov.utility;

/**
 * Created by Akshay Chavan on 06,May,2021
 * akshaychavan.kkwedu@gmail.com
 */


import com.akshaychavan.vaxicov.pojo.CalendarByDistrictPojo;
import com.akshaychavan.vaxicov.pojo.CalendarByPinPojo;
import com.akshaychavan.vaxicov.pojo.FindCenterByPinPojo;
import com.akshaychavan.vaxicov.pojo.GetDistrictsByStatesPojo;
import com.akshaychavan.vaxicov.pojo.SetNotificationsResponsePojo;
import com.akshaychavan.vaxicov.pojo.UserLoginResponsePojo;
import com.akshaychavan.vaxicov.pojo.UserRegistrationResponsePojo;
import com.akshaychavan.vaxicov.pojo.VisitingCounterPojo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.OPTIONS;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

//    @Headers({"Content-Type: application/json; charset=utf-8", "Accept-Language: hi_IN", "User-Agent: PostmanRuntime/7.28.0", "Accept:*/*", "Accept-Encoding:gzip, deflate, br", "Connection:keep-alive"})
    @Headers("user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
    @GET("appointment/sessions/public/findByPin")
    Call<FindCenterByPinPojo> findCenterByPin(@Query("pincode") int pincode, @Query("date") String date);


    @Headers("user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
    @GET("appointment/sessions/public/calendarByPin")
    Call<CalendarByPinPojo> findCalendarByPin(@Query("pincode") int pincode, @Query("date") String date);

    @Headers("user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
    @GET("appointment/sessions/public/calendarByDistrict")
    Call<CalendarByDistrictPojo> findCalendarByDistrict(@Query("district_id") int district_id, @Query("date") String date);

    @Headers("user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
    @GET("admin/location/districts/{state_id}")
    Call<GetDistrictsByStatesPojo> getDistrictsByState(@Path("state_id") int state_id);

    @Headers("user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
    @GET("vaxicovtest")
    Call<VisitingCounterPojo> getUsersCount();

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("auth/login")
    Call<UserLoginResponsePojo> loginUser(@Body String requestParams);

    @Headers({
            "Content-Type: application/json; charset=utf-8",
            "Accept: */*", "Accept-Encoding: en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7", "Access-Control-Request-Headers: content-type",
            "Access-Control-Request-Method: POST", "Connection: keep-alive", "Host: vaccine-notifier-covid-india.herokuapp.com",
            "Origin: https://vaccine-notifier.amitwani.dev", "Referer: https://vaccine-notifier.amitwani.dev/", "ec-Fetch-Dest: empty",
            "Sec-Fetch-Mode: cors", "Sec-Fetch-Site: cross-site", "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36"
    })
    @POST("auth/register")
    Call<UserRegistrationResponsePojo> registerUser(@Body String requestParams);

    @Headers({"Content-Type: application/json"})
    @POST("notifications/user/{USER_ID}")
    Call<SetNotificationsResponsePojo> setNotifications(@Path("USER_ID") String user_id, @Body String requestParams, @Header("Authorization") String authorization);
}
