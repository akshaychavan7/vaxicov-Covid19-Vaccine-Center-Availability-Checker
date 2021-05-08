package com.akshaychavan.covid19vaccineavailabilitychecker;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshaychavan.covid19vaccineavailabilitychecker.utility.ApiClient;
import com.akshaychavan.covid19vaccineavailabilitychecker.utility.ApiInterface;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import Adapters.AvailabilityDetailsRowAdapter;
import Adapters.AvailabilityDetailsRowDistrictAdapter;
import pojo.CalendarByDistrictPojo;
import pojo.CalendarByPinPojo;
import pojo.Center;
import pojo.CenterDistrict;
import pojo.District;
import pojo.FindCenterByPinPojo;
import pojo.GetDistrictsByStatesPojo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private final String TAG = "MainActivity";

    String findBy = "Pin";          // default -> pin, options-> pin OR district
    NavigationView navigationView;
    Toolbar toolbar;
    String[] languages;
    ArrayAdapter dropdownArrayAdapter;
    AutoCompleteTextView agedropdownTextView;
    EditText etAgeGroup, etPin;
    AutoCompleteTextView etDistrict, etState;
    RadioGroup rgFindBy;
    LinearLayout detailsTable;
    MaterialButton btnSearch, btnNotify;
    ProgressBar progressBar;
    TextView tvNoSlots, toolbarTitle;
    ArrayAdapter<String> statesAdapter, districtsAdapter;
    ArrayList<District> districtArrayList;
    ArrayList<String> districtsArray = new ArrayList<>();
    int selectedDistrictID;

    // Availability Details Adapter
    RecyclerView availabilityDetailsRecylcer;
    RecyclerView.LayoutManager availabilityDetailsLayoutManager;
    RecyclerView.Adapter availabilityDetailsAdapter;
    Date c = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    String today = df.format(c);
    HashMap<String, Integer> states = new HashMap<String, Integer>();
    private DrawerLayout drawerLayout;

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
        setNotifications();
    }

    public void bindVariables() {

        toolbarTitle = findViewById(R.id.toolbar_title);

        // setting toolbar title color gradient
        Paint paint = toolbarTitle.getPaint();
        Shader textShader=new LinearGradient(0, 0, paint.measureText(toolbarTitle.getText().toString()), paint.getTextSize(),
                new int[]{Color.parseColor("#FF6F00"),Color.WHITE, Color.parseColor("#1B5E20")},
                null, Shader.TileMode.CLAMP);
        toolbarTitle.getPaint().setShader(textShader);
        ///////////////////////////////////////

        detailsTable = findViewById(R.id.ll_details_table);
        btnSearch = findViewById(R.id.btn_search);
        btnNotify = findViewById(R.id.btn_notify);

        etAgeGroup = findViewById(R.id.et_agegroup);
        etPin = findViewById(R.id.et_pin);
        etDistrict = findViewById(R.id.et_district);
        etState = findViewById(R.id.et_state);
        rgFindBy = findViewById(R.id.rg_findby);

        tvNoSlots = findViewById(R.id.tv_no_slots);
        progressBar = findViewById(R.id.progressbar);

        // Setting up adapters
        availabilityDetailsRecylcer = findViewById(R.id.rv_availability_details);
        availabilityDetailsRecylcer.setHasFixedSize(true);
        availabilityDetailsLayoutManager = new LinearLayoutManager(this);


        states.put("Andaman and Nicobar Islands", 1);
        states.put("Andhra Pradesh", 2);
        states.put("Arunachal Pradesh", 3);
        states.put("Assam", 4);
        states.put("Bihar", 5);
        states.put("Chandigarh", 6);
        states.put("Chhattisgarh", 7);
        states.put("Dadra and Nagar Haveli", 8);
        states.put("Daman and Diu", 37);
        states.put("Delhi", 9);
        states.put("Goa", 10);
        states.put("Gujarat", 11);
        states.put("Haryana", 12);
        states.put("Himachal Pradesh", 13);
        states.put("Jammu and Kashmir", 14);
        states.put("Jharkhand", 15);
        states.put("Karnataka", 16);
        states.put("Kerala", 17);
        states.put("Ladakh", 18);
        states.put("Lakshadweep", 19);
        states.put("Madhya Pradesh", 20);
        states.put("Maharashtra", 21);
        states.put("Manipur", 22);
        states.put("Meghalaya", 23);
        states.put("Mizoram", 24);
        states.put("Nagaland", 25);
        states.put("Odisha", 26);
        states.put("Puducherry", 27);
        states.put("Punjab", 28);
        states.put("Rajasthan", 29);
        states.put("Sikkim", 30);
        states.put("Tamil Nadu", 31);
        states.put("Telangana", 32);
        states.put("Tripura", 33);
        states.put("Uttar Pradesh", 34);
        states.put("Uttarakhand", 35);
        states.put("West Bengal", 36);

        // setting dropdown for states
        String[] statesArray = states.keySet().toArray(new String[0]);
//        for(String s: statesArray) {
//            Log.e(TAG, s);
//        }
        statesAdapter  = new ArrayAdapter<String>(MainActivity.this, R.layout.dropdown_item, statesArray);
        etState.setAdapter(statesAdapter);
    }       // end bindVariables()

    public void bindEvents() {
        etAgeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        rgFindBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.findbypin:
                        findBy = "pin";
                        etPin.setVisibility(View.VISIBLE);
                        etDistrict.setVisibility(View.GONE);
                        etState.setVisibility(View.GONE);
                        break;
                    case R.id.findbydistrict:
                        findBy = "district";
                        etPin.setVisibility(View.GONE);
                        etDistrict.setVisibility(View.VISIBLE);
                        etState.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (performValidations()) {
                    switch (findBy.toLowerCase()) {
                        case "pin":
                            findCalendarByPin(Integer.parseInt(etPin.getText().toString()), today);
                            break;
                        case "district":
                            findCalendarByDistrict(selectedDistrictID, today);
                            break;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please make sure you input all fields correctly!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        etState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e(TAG, "etstate on item click listener");
                getDistrictsByStates(getStateID(etState.getText().toString()));

            }
        });

        etDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDistrictID = getDistrictID(etDistrict.getText().toString());
            }
        });

    }       // end bindEvents()


    public void setNotifications() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ID")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("textTitle")
                .setContentText("textContent")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(100, builder.build());
        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 7);
//        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
//        intent.setAction("MY_NOTIFICATION_MESSAGE");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000, pendingIntent);
    }


    public boolean performValidations() {
        switch (findBy.toLowerCase()) {
            case "pin":
                if (etPin.getText().length() == 0) {
                    return false;
                }
                break;
            case "district":
                if (etState.getText().length() == 0 || etDistrict.getText().length() == 0) {
                    return false;
                }
                break;
        }
        return true;
    }


    public void showPopup(View v) {
        PopupMenu agePopup = new PopupMenu(MainActivity.this, v);
        agePopup.setOnMenuItemClickListener(this);
        agePopup.inflate(R.menu.agegroup_menu);
        agePopup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.agegroup1:
                etAgeGroup.setText(item.getTitle());
                return true;
            case R.id.agegroup2:
                etAgeGroup.setText(item.getTitle());
                return true;
            case R.id.agegroup3:
                etAgeGroup.setText(item.getTitle());
                return true;
            default:
                return false;
        }
    }


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
                    Log.e(TAG, "URL>>" + response.raw().request().url());
                    Toast.makeText(MainActivity.this, "Response Error >> " + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FindCenterByPinPojo> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong!\n>>" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Something went wrong >>" + t.getMessage());
            }
        });

    }       // end findCenterByPin()

    public void findCalendarByPin(int pin, String date) {

        progressBar.setVisibility(View.VISIBLE);
        tvNoSlots.setVisibility(View.GONE);


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Log.e(TAG, ApiClient.getClient().baseUrl().toString());

        Call<CalendarByPinPojo> call = apiInterface.findCalendarByPin(pin, date);


        call.enqueue(new Callback<CalendarByPinPojo>() {
            @Override
            public void onResponse(Call<CalendarByPinPojo> call, Response<CalendarByPinPojo> response) {
//                    Log.e(TAG, "Response Code -> " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "URL>>" + response.raw().request().url());

                    progressBar.setVisibility(View.GONE);

                    ArrayList<Center> availabilityDetailsList = (ArrayList<Center>) response.body().getCenters();

                    // check if slots are available or not
                    if (availabilityDetailsList.size() == 0)       // no slots available
                    {
                        tvNoSlots.setText("No slots available :(");
                        detailsTable.setVisibility(View.GONE);
                        tvNoSlots.setVisibility(View.VISIBLE);
                    } else {
                        // Passing data to Adapter
                        availabilityDetailsAdapter = new AvailabilityDetailsRowAdapter(availabilityDetailsList, MainActivity.this);
                        availabilityDetailsRecylcer.setLayoutManager(availabilityDetailsLayoutManager);
                        availabilityDetailsRecylcer.setAdapter(availabilityDetailsAdapter);

                        // if details fetched successfully then show the details table
                        detailsTable.setVisibility(View.VISIBLE);
                    }
                } else {
                    // using no slots textview to display error message
                    progressBar.setVisibility(View.GONE);
                    tvNoSlots.setVisibility(View.VISIBLE);
                    tvNoSlots.setText("Something went wrong!\nPlease make sure your internet in on and you input fields correctly.");

                    Log.e(TAG, "URL>>" + response.raw().request().url());
//                    Toast.makeText(MainActivity.this, "Response Error >> " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CalendarByPinPojo> call, Throwable t) {
                // using no slots textview to display error message
                progressBar.setVisibility(View.GONE);
                tvNoSlots.setVisibility(View.VISIBLE);
                tvNoSlots.setText("Something went wrong!\nPlease make sure your internet in on and you input fields correctly.");
//                Toast.makeText(MainActivity.this, "Something went wrong!\n>>" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Something went wrong >>" + t.getMessage());
            }
        });

    }       // end findCalendarByPin()

    public void findCalendarByDistrict(int district_id, String date) {

        progressBar.setVisibility(View.VISIBLE);
        tvNoSlots.setVisibility(View.GONE);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Log.e(TAG, ApiClient.getClient().baseUrl().toString());

        Call<CalendarByDistrictPojo> call = apiInterface.findCalendarByDistrict(district_id, date);


        call.enqueue(new Callback<CalendarByDistrictPojo>() {
            @Override
            public void onResponse(Call<CalendarByDistrictPojo> call, Response<CalendarByDistrictPojo> response) {
//                    Log.e(TAG, "Response Code -> " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "URL>>" + response.raw().request().url());

                    progressBar.setVisibility(View.GONE);

                    ArrayList<CenterDistrict> availabilityDetailsList = (ArrayList<CenterDistrict>) response.body().getCenters();

                    // check if slots are available or not
                    if (availabilityDetailsList.size() == 0)       // no slots available
                    {
                        tvNoSlots.setText("No slots available :(");
                        detailsTable.setVisibility(View.GONE);
                        tvNoSlots.setVisibility(View.VISIBLE);
                    } else {
                        // Passing data to Adapter
                        availabilityDetailsAdapter = new AvailabilityDetailsRowDistrictAdapter(availabilityDetailsList, MainActivity.this);
                        availabilityDetailsRecylcer.setLayoutManager(availabilityDetailsLayoutManager);
                        availabilityDetailsRecylcer.setAdapter(availabilityDetailsAdapter);

                        // if details fetched successfully then show the details table
                        detailsTable.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e(TAG, "URL>>" + response.raw().request().url());
                    Toast.makeText(MainActivity.this, "Response Error >> " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CalendarByDistrictPojo> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong!\n>>" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Something went wrong >>" + t.getMessage());
            }
        });

    }       // end findCalendarByPin()


    public void getDistrictsByStates(int state_id) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Log.e(TAG, ApiClient.getClient().baseUrl().toString());

        Call<GetDistrictsByStatesPojo> call = apiInterface.getDistrictsByState(state_id);


        call.enqueue(new Callback<GetDistrictsByStatesPojo>() {
            @Override
            public void onResponse(Call<GetDistrictsByStatesPojo> call, Response<GetDistrictsByStatesPojo> response) {
//                    Log.e(TAG, "Response Code -> " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "URL>>" + response.raw().request().url());
                    districtArrayList = (ArrayList<District>) response.body().getDistricts();


                    // setting adapter for dropdown items
                    for(District d: districtArrayList) {
                        districtsArray.add(d.getDistrictName());
                    }
                    districtsAdapter  = new ArrayAdapter<String>(MainActivity.this, R.layout.dropdown_item, districtsArray.toArray(new String[0]));
                    etDistrict.setAdapter(districtsAdapter);
                } else {
                    Log.e(TAG, "Failed URL>>" + response.raw().request().url());
//                    Toast.makeText(MainActivity.this, "Response Error >> " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetDistrictsByStatesPojo> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Something went wrong!\n>>" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Something went wrong >>" + t.getMessage());
            }
        });
    }


    public int getStateID(String state) {
        return states.get(state);
    }

    public int getDistrictID(String district) {
        for(District d: districtArrayList) {
            if(district.equalsIgnoreCase(d.getDistrictName())) {
                return d.getDistrictId();
            }
        }
        return -1;
    }

}