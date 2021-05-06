package com.akshaychavan.covid19vaccineavailabilitychecker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.akshaychavan.covid19vaccineavailabilitychecker.utility.ApiClient;
import com.akshaychavan.covid19vaccineavailabilitychecker.utility.ApiInterface;

import org.json.JSONException;

import pojo.FindCenterByPinPojo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindVariables();
        bindEvents();

        findCenterbyPin();
    }

    public void bindVariables() {

    }       // end bindVariables()

    public void bindEvents() {

    }       // end bindEvents()


    public void findCenterbyPin() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<FindCenterByPinPojo> call = apiInterface.findCenterByPin(110001, "07-05-2021");
        call.enqueue(new Callback<FindCenterByPinPojo>() {
            @Override
            public void onResponse(Call<FindCenterByPinPojo> call, Response<FindCenterByPinPojo> response) {
//                    Log.e(TAG, "Response Code -> " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "Success " + response.body().getSessions().get(0).getAddress());
                } else {
                    Toast.makeText(MainActivity.this, "Response Error >> ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FindCenterByPinPojo> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong!\n>>" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Something went wrong >>" + t.getMessage());
            }
        });

    }       // end findCenterbyPin()

}