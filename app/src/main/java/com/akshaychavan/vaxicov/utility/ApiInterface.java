package com.akshaychavan.vaxicov.utility;

/**
 * Created by Akshay Chavan on 06,May,2021
 * akshaychavan.kkwedu@gmail.com
 */


import com.akshaychavan.vaxicov.pojo.CalendarByDistrictPojo;
import com.akshaychavan.vaxicov.pojo.CalendarByPinPojo;
import com.akshaychavan.vaxicov.pojo.FindCenterByPinPojo;
import com.akshaychavan.vaxicov.pojo.GetDistrictsByStatesPojo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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
}
