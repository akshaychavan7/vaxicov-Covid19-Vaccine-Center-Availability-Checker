package com.akshaychavan.vaxicov;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.akshaychavan.vaxicov.utility.GlobalCode;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;
    private final String TAG = "LoginActivity";
    GoogleSignInClient mGoogleSignInClient;

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
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
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
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();


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

        }
    }

}