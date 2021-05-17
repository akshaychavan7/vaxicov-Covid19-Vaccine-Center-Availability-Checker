package com.akshaychavan.vaxicov;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.akshaychavan.vaxicov.utility.GlobalCode;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedprefs";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String LOGIN_STATE = "login";
    public static final String GOOGLE_SIGN_IN_ACC = "google_sign_in_account";
    private static final int RC_SIGN_IN = 0;
    final String KEY_ACTION = "action", KEY_NAME = "name", KEY_EMAIL = "email", KEY_IMAGE_URL = "photourl",
            ADD_RECORD_TO_SHEET_URL = "https://script.google.com/macros/s/AKfycbxUZPIwV3QjQ078UKHDuQ0TD3Ww0dq_O87Pg7iRZeE79REAf5gSc-ZGwPXs7w-MYpBx/execXXXXX";
    private final String TAG = "LoginActivity";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct;
    String personName, personGivenName, personFamilyName, personEmail, personId;
    Uri personPhoto;
    boolean loginState = false;     // default login state to be false

    EditText etName, etEmail;
    MaterialButton signinButton;
    TextView titleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        ////////////////////////////////////////////////////////////////////////
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        checkLoginState();      // check login state, if already logged in then directly open main activity
        bindVariables();
        bindEvents();
        loadLoginDetails();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.e(TAG, "onActivityResult>>" + requestCode);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    public void bindVariables() {
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        signinButton = findViewById(R.id.signin);
        titleText = findViewById(R.id.tv_title);
        // setting toolbar title color gradient
//        Paint paint = titleText.getPaint();
//        Shader textShader = new LinearGradient(0, 0, paint.measureText(titleText.getText().toString()), paint.getTextSize(),
//                new int[]{Color.parseColor("#FF6F00"), Color.WHITE, Color.parseColor("#1B5E20")},
//                null, Shader.TileMode.CLAMP);
//        titleText.getPaint().setShader(textShader);
        ///////////////////////////////////////
    }

    public void bindEvents() {
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Configure sign-in to request the user's ID, email address, and basic
                // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

//                getUsersCount();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();


                // Build a GoogleSignInClient with the options specified by gso.
                mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                GlobalCode.getInstance().setGoogleSignInClient(mGoogleSignInClient);

                signIn();

            }
        });
    }


    private void checkLoginState() {

        // if already logged in then go to MainActivity
        if (sharedPreferences.getBoolean(LOGIN_STATE, false)) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();


            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
            GlobalCode.getInstance().setGoogleSignInClient(mGoogleSignInClient);

            signIn();
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        //           GoogleSignInAccount account = completedTask.getResult(ApiException.class);
        getUserAccountInfo();
    }


    public void getUserAccountInfo() {

        if (acct != null) {
//            if (personEmail == null || !personEmail.equalsIgnoreCase(acct.getEmail())) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();

            loginState = true;      // login success
//            }
            addRecordToSheet();

            GlobalCode.getInstance().setAccountDetails(acct);


            // save login details before moving to next activity
            saveLoginDetails();

            // move to next activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Log.e(TAG, "Google Account Details is NULL");
        }
    }


    private void addRecordToSheet() {
//        final ProgressDialog loading = ProgressDialog.show(this, "Signing in...", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_RECORD_TO_SHEET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        loading.dismiss();
//                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                        GlobalCode.getInstance().setUsersCount(Integer.parseInt(response));
                        GlobalCode.getInstance().getTvUsersCount().setText("Users count: " + response);
                        GlobalCode.getInstance().getUserCountProgressBar().setVisibility(View.GONE);
                        GlobalCode.getInstance().getTvUsersCount().setVisibility(View.VISIBLE);
                        Log.e(TAG, "Response>>" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Error>> " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_ACTION, "addItem");
                params.put(KEY_NAME, personName);
                params.put(KEY_EMAIL, personEmail);

                if (personPhoto != null) {
//                    Log.e(TAG, "photourl:" + "" + personPhoto.toString());
                    params.put(KEY_IMAGE_URL, "" + personPhoto.toString());
                } else {
                    params.put(KEY_IMAGE_URL, "No Image");
                }
                return params;
            }

        };

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }

    // Saving user login data with Shared Preferences
    public void saveLoginDetails() {
        editor.putString(USER_NAME, personName);
        editor.putString(USER_EMAIL, personEmail);
        editor.putBoolean(LOGIN_STATE, loginState);

        editor.apply();
    }

    // Fetching user's previous login details if previously logged in
    public void loadLoginDetails() {
        etName.setText(sharedPreferences.getString(USER_NAME, ""));
        etEmail.setText(sharedPreferences.getString(USER_EMAIL, ""));
        loginState = sharedPreferences.getBoolean(LOGIN_STATE, false);


    }


}
