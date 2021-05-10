package com.akshaychavan.vaxicov;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshaychavan.vaxicov.utility.ApiClient;
import com.akshaychavan.vaxicov.utility.ApiInterface;
import com.akshaychavan.vaxicov.utility.GlobalCode;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.akshaychavan.vaxicov.pojo.CalendarByDistrictPojo;
import com.akshaychavan.vaxicov.pojo.CalendarByPinPojo;
import com.akshaychavan.vaxicov.pojo.Center;
import com.akshaychavan.vaxicov.pojo.CenterDistrict;
import com.akshaychavan.vaxicov.pojo.District;
import com.akshaychavan.vaxicov.pojo.FindCenterByPinPojo;
import com.akshaychavan.vaxicov.pojo.GetDistrictsByStatesPojo;
import com.akshaychavan.vaxicov.pojo.Session;
import com.akshaychavan.vaxicov.pojo.SessionDistrict;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public final String NOTIFICATION_CHANNEL_ID = "Channel1";
    private final String TAG = "MainActivity";
//    final static String KEY = "Availability Status", VALUE_DISTRICT = "Available at District", VALUE_PIN = "Available at area", VALUE_UNAVAILABLE = "None";


    GlobalCode globalCode = GlobalCode.getInstance();
    GoogleSignInAccount accountDetails;
    String findBy = "Pin";          // default -> pin, options-> pin OR district
    NavigationView navigationView;
    Toolbar toolbar;

    AlertDialog notificationsPopup;
    String[] languages;
    ArrayAdapter dropdownArrayAdapter;
    AutoCompleteTextView agedropdownTextView;
    EditText etAgeGroup, etPin;
    AutoCompleteTextView etDistrict, etState;
    RadioGroup rgFindBy;
    LinearLayout detailsTable;
    MaterialButton btnSearch, btnNotify;
    ProgressBar progressBar;
    TextView tvNoSlots, toolbarTitle, tvMadeBy, tvUsername, tvMail, usersCount, navHeaderTitle;
    ImageView profileIcon;
    ArrayAdapter<String> statesAdapter, districtsAdapter;
    ArrayList<District> districtArrayList;
    ArrayList<String> districtsArray = new ArrayList<>();
    int selectedDistrictID;

    // Availability Details Adapter
    RecyclerView availabilityDetailsRecylcer;
    RecyclerView.LayoutManager availabilityDetailsLayoutManager;
    RecyclerView.Adapter availabilityDetailsAdapter;
    /////////////////////////////////

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        bindVariables();
        bindEvents();

        accountDetails = globalCode.getAccountDetails();
        // Setting user info in side panel
        tvUsername.setText(accountDetails.getDisplayName());
        tvMail.setText(accountDetails.getEmail());
        usersCount.setText("Total User: "+globalCode.getUsersCount());
        Glide.with(this)
                .load(accountDetails.getPhotoUrl())
                .into(profileIcon);


    }

    public void bindVariables() {

        tvMadeBy = findViewById(R.id.tv_madeby);
        toolbarTitle = findViewById(R.id.toolbar_title);

        View sideNav = navigationView.getHeaderView(0);
        tvUsername = sideNav.findViewById(R.id.tv_username);
        tvMail = sideNav.findViewById(R.id.tv_user_mail);
        profileIcon = sideNav.findViewById(R.id.iv_profile_icon);
        navHeaderTitle = sideNav.findViewById(R.id.navheader_title);
        usersCount = navigationView.findViewById(R.id.tv_users_count);

        // setting toolbar title color gradient
        Paint paint = toolbarTitle.getPaint();
        Shader textShader = new LinearGradient(0, 0, paint.measureText(toolbarTitle.getText().toString()), paint.getTextSize(),
                new int[]{Color.parseColor("#FF6F00"), Color.WHITE, Color.parseColor("#1B5E20")},
                null, Shader.TileMode.CLAMP);
        toolbarTitle.getPaint().setShader(textShader);

        // also for side nav title
        Paint paint2 = navHeaderTitle.getPaint();
        Shader textShader2 = new LinearGradient(0, 0, paint2.measureText(navHeaderTitle.getText().toString()), paint2.getTextSize(),
                new int[]{Color.parseColor("#FF6F00"), Color.WHITE, Color.parseColor("#1B5E20")},
                null, Shader.TileMode.CLAMP);
        navHeaderTitle.getPaint().setShader(textShader2);
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
        statesAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.dropdown_item, statesArray);
        etState.setAdapter(statesAdapter);
    }       // end bindVariables()

    public void bindEvents() {

        tvMadeBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.linkedin.com/in/akshaychavan7/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

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

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Lengths: " + etPin.getText().length() + " " + etState.getText().length() + " " + etDistrict.getText().length());
                if (etPin.getText().length() > 0 && etState.getText().length() > 0 && etDistrict.getText().length() > 0) {
                    openNotificationPopup();
                } else {
                    Toast.makeText(MainActivity.this, "Make sure you fill all the fields for both find by pin and find by district options to set notifications!", Toast.LENGTH_SHORT).show();
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


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nv_logout:
                        logout();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    break;
                }

                return true;
            }
        });

    }       // end bindEvents()


//    public void sendNotification() {
//
//
//        //Get an instance of NotificationManager//
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                        .setSmallIcon(R.mipmap.flag1)
//                        .setContentTitle("My notification")
//                        .setContentText("Hello World!")
//                        .setAutoCancel(true);
//
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
//        managerCompat.notify(1, mBuilder.build());
//
//
//    }


    public void setNotifications() {

        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 7);
//        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);


        // find appointments
        findCalendarByPin(Integer.parseInt(etPin.getText().toString()), today);
        findCalendarByDistrict(selectedDistrictID, today);

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


    public void openNotificationPopup() {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View view = li.inflate(R.layout.set_notifications_popup_layout, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.CustomDialog);
        alertDialogBuilder.setView(view);
//        alertDialogBuilder.create();
//        alertDialogBuilder.show();

        notificationsPopup = alertDialogBuilder.create();
        notificationsPopup.show();


        // binding popup variables and events

        Button set = view.findViewById(R.id.set);
        Button cancel = view.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationsPopup.dismiss();
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNotifications();
                notificationsPopup.dismiss();
                Toast.makeText(MainActivity.this, "Notifier set successfully!\nNow you can close the app but do not remove it from active processes.", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void findCenterByPin() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<FindCenterByPinPojo> call = apiInterface.findCenterByPin(110001, "08-05-2021");
        call.enqueue(new Callback<FindCenterByPinPojo>() {
            @Override
            public void onResponse(Call<FindCenterByPinPojo> call, Response<FindCenterByPinPojo> response) {
//                    Log.e(TAG, "Response Code -> " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "Success ");
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
                    Log.e(TAG, "PIN URL>>" + response.raw().request().url());

                    progressBar.setVisibility(View.GONE);

                    ArrayList<Center> availabilityDetailsList = (ArrayList<Center>) response.body().getCenters();

                    for (Center c : availabilityDetailsList) {
                        for (Session s : c.getSessions()) {
                            Log.e(TAG, "Cnt:" + s.getAvailableCapacity());
                            if (s.getAvailableCapacity() > 0) {
                                globalCode.setPin_availability(true);
                                break;
                            }
                        }
                    }

                    // check if slots are available or not
                    if (availabilityDetailsList.size() == 0)       // no slots available
                    {
                        globalCode.setPin_availability(false);
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
                    Log.e(TAG, "District URL>>" + response.raw().request().url());

                    progressBar.setVisibility(View.GONE);

                    ArrayList<CenterDistrict> availabilityDetailsList = (ArrayList<CenterDistrict>) response.body().getCenters();

                    for (CenterDistrict c : availabilityDetailsList) {
                        for (SessionDistrict s : c.getSessions()) {
                            if (s.getAvailableCapacity() > 0) {
                                globalCode.setDistrict_availability(true);
                                break;
                            }
                        }
                    }

                    // check if slots are available or not
                    if (availabilityDetailsList.size() == 0)       // no slots available
                    {
                        globalCode.setDistrict_availability(false);
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
                    for (District d : districtArrayList) {
                        districtsArray.add(d.getDistrictName());
                    }
                    districtsAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.dropdown_item, districtsArray.toArray(new String[0]));
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
        for (District d : districtArrayList) {
            if (district.equalsIgnoreCase(d.getDistrictName())) {
                return d.getDistrictId();
            }
        }
        return -1;
    }


    private void logout() {
        GoogleSignInClient mGoogleSignInClient = globalCode.getGoogleSignInClient();

        mGoogleSignInClient.signOut().addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "You have successfully logged out!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}