package com.akshaychavan.vaxicov;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.akshaychavan.vaxicov.pojo.District;
import com.akshaychavan.vaxicov.pojo.GetDistrictsByStatesPojo;
import com.akshaychavan.vaxicov.pojo.VisitingCounterPojo;
import com.akshaychavan.vaxicov.utility.ApiClient;
import com.akshaychavan.vaxicov.utility.ApiInterface;
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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;
    final String KEY_ACTION = "action", KEY_NAME = "name", KEY_EMAIL = "email", KEY_IMAGE_URL = "photourl",
            ADD_RECORD_TO_SHEET_URL = "https://script.google.com/macros/s/AKfycbwuso3FoCOpCBhTLcqzApL7DzsigI95VbsBrc99gF08vigewxVi_x2rVAFFOBTzSJ_A/exec";
    private final String TAG = "LoginActivity";
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct;
    String personName, personGivenName, personFamilyName, personEmail, personId;
    Uri personPhoto;

    MaterialButton signinButton;
    TextView titleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        bindVariables();
        bindEvents();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult>>" + requestCode);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    public void bindVariables() {
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


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Log.e(TAG, "Starting  startActivityForResult");
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
            getUserAccountInfo();
        } catch (ApiException e) {
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            if (e.getStatusCode() == 12501) {
                signIn();
            }
//            updateUI(null);
        }
    }


    public void getUserAccountInfo() {

        if (acct != null) {
            if (personEmail == null || !personEmail.equalsIgnoreCase(acct.getEmail())) {
                personName = acct.getDisplayName();
                personGivenName = acct.getGivenName();
                personFamilyName = acct.getFamilyName();
                personEmail = acct.getEmail();
                personId = acct.getId();
                personPhoto = acct.getPhotoUrl();
                addRecordToSheet();
                getUsersCount();
            }

            GlobalCode.getInstance().setAccountDetails(acct);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
//            Log.e(TAG, "Name: " + personName + " Email: " + personEmail);
//            tvUsername.setText(personName.toString());
//            tvEmail.setText(personEmail.toString());
//
//            Glide.with(this)
//                    .load(personPhoto)
//                    .into(ivProfileIcon);
        } else {
            Log.e(TAG, "else");
        }
    }


    private void addRecordToSheet() {
        final ProgressDialog loading = ProgressDialog.show(this, "Signing in...", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_RECORD_TO_SHEET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
//                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
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

                if(personPhoto!=null) {
                    Log.e(TAG, "photourl:" + "" + personPhoto.toString());
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


    public void getUsersCount() {
        ApiInterface apiInterface = ApiClient.getClientCounter().create(ApiInterface.class);

//        Log.e(TAG, ApiClient.getClient().baseUrl().toString());

        Call<VisitingCounterPojo> call = apiInterface.getUsersCount();


        call.enqueue(new Callback<VisitingCounterPojo>() {
            @Override
            public void onResponse(Call<VisitingCounterPojo> call, retrofit2.Response<VisitingCounterPojo> response) {
//                    Log.e(TAG, "Response Code -> " + response.code());
                if (response.isSuccessful()) {
                    GlobalCode.getInstance().setUsersCount(response.body().getValue());
                } else {
                    Log.e(TAG, "Failed URL>>" + response.raw().request().url());
//                    Toast.makeText(MainActivity.this, "Response Error >> " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VisitingCounterPojo> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Something went wrong!\n>>" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Something went wrong >>" + t.getMessage());
            }
        });
    }

}