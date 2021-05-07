package com.akshaychavan.covid19vaccineavailabilitychecker;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.akshaychavan.covid19vaccineavailabilitychecker.utility.ApiClient;
import com.akshaychavan.covid19vaccineavailabilitychecker.utility.ApiInterface;
import com.google.android.material.navigation.NavigationView;

import pojo.CalendarByPinPojo;
import pojo.FindCenterByPinPojo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Setting navigation bar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        bindVariables();
        bindEvents();

        findCenterByPin();
        findCalendarByPin();
    }

    public void bindVariables() {

    }       // end bindVariables()

    public void bindEvents() {

    }       // end bindEvents()


    public void findCenterByPin() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<FindCenterByPinPojo> call = apiInterface.findCenterByPin(110001, "08-05-2021");
        call.enqueue(new Callback<FindCenterByPinPojo>() {
            @Override
            public void onResponse(Call<FindCenterByPinPojo> call, Response<FindCenterByPinPojo> response) {
//                    Log.e(TAG, "Response Code -> " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "Success " + response.body().getSessions().get(0).getAddress());
                } else {
                    Log.e(TAG, "URL>>"+response.raw().request().url());
                    Toast.makeText(MainActivity.this, "Response Error >> "+ response.code() +" "+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FindCenterByPinPojo> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong!\n>>" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Something went wrong >>" + t.getMessage());
            }
        });

    }       // end findCenterByPin()

    public void findCalendarByPin() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Log.e(TAG, ApiClient.getClient().baseUrl().toString());

        Call<CalendarByPinPojo> call = apiInterface.findCalendarByPin(422003, "07-05-2021");


        call.enqueue(new Callback<CalendarByPinPojo>() {
            @Override
            public void onResponse(Call<CalendarByPinPojo> call, Response<CalendarByPinPojo> response) {
//                    Log.e(TAG, "Response Code -> " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "Success " + response.body().getCenters().get(0).getName());
                } else {
                    Log.e(TAG, "URL>>"+response.raw().request().url());
                    Toast.makeText(MainActivity.this, "Response Error >> "+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CalendarByPinPojo> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong!\n>>" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Something went wrong >>" + t.getMessage());
            }
        });

    }       // end findCalendarByPin()

}