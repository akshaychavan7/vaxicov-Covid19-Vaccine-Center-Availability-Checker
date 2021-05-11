package com.akshaychavan.vaxicov.utility;

import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

/**
 * Created by Akshay Chavan on 09,May,2021
 * akshaychavan.kkwedu@gmail.com
 */
public class GlobalCode {

    private static GlobalCode mInstance = null;
    int usersCount;
    GoogleSignInAccount accountDetails;
    GoogleSignInClient googleSignInClient;
    TextView tvUsersCount;
    boolean pin_availability = false, district_availability = false;
//    int pin;
//    String state, district;

    /////////////////////////////////////////////////////////////
    public static synchronized GlobalCode getInstance() {
        if (null == mInstance) {
            mInstance = new GlobalCode();
        }
        return mInstance;
    }
    /////////////////////////////////////////////////////////////



    public boolean isPin_availability() {
        return pin_availability;
    }

    public void setPin_availability(boolean pin_availability) {
        this.pin_availability = pin_availability;
    }

    public boolean isDistrict_availability() {
        return district_availability;
    }

    public void setDistrict_availability(boolean district_availability) {
        this.district_availability = district_availability;
    }

    public GoogleSignInAccount getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(GoogleSignInAccount accountDetails) {
        this.accountDetails = accountDetails;
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return googleSignInClient;
    }

    public void setGoogleSignInClient(GoogleSignInClient googleSignInClient) {
        this.googleSignInClient = googleSignInClient;
    }

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    public TextView getTvUsersCount() {
        return tvUsersCount;
    }

    public void setTvUsersCount(TextView tvUsersCount) {
        this.tvUsersCount = tvUsersCount;
    }
}
