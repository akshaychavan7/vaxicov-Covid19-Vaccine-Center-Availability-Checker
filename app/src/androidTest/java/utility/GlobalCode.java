package utility;

import android.app.Dialog;
import android.graphics.Color;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Akshay Chavan on 10,February,2021
 * akshay.chavan@finiq.com
 * FinIQ Consulting India
 */
public class GlobalCode {

    private static GlobalCode mInstance = null;
    Dialog loaderDialog;

    private final String TAG = "GlobalCode";

    Toolbar toolbar;
    TabLayout assetTypeTabLayout;


    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    // setting colors
    public final int[] colors = {
            Color.parseColor("#0075C2"),
            Color.parseColor("#5499C7"),
            Color.parseColor("#0D85D5"),
            Color.parseColor("#0D5F97"),
            Color.parseColor("#34A5F3"),
            Color.parseColor("#01426E"),
            Color.parseColor("#7BA6C3"),
            Color.parseColor("#001D30")
    };

    // Piechart colors according to asset type ==> Shares - Red, Bonds - Green, Funds - Blue, Others - Yellow

    public final int[] assetTypeColors = {
            Color.parseColor("#F44336"),        // Red
            Color.parseColor("#4CAF50"),        // Green
            Color.parseColor("#0D85D5"),        // Blue
            Color.parseColor("#FFC107")         // Others
    };

    public String selectedFragment = "Dashboard";
    public String selectedAssetTypeTab;
    public ArrayList<Integer> pieChartColors = new ArrayList<Integer>();

    public GlobalCode() {

        // setting piechart colors array
        for (int c : colors) {
            pieChartColors.add(c);
        }
    }

    public static synchronized GlobalCode getInstance() {
        if (null == mInstance) {
            mInstance = new GlobalCode();
        }
        return mInstance;
    }

    public TabLayout getAssetTypeTabLayout() {
        return assetTypeTabLayout;
    }

    public void setAssetTypeTabLayout(TabLayout assetTypeTabLayout) {
        this.assetTypeTabLayout = assetTypeTabLayout;
    }

    public String getSelectedAssetTypeTab() {
        return selectedAssetTypeTab;
    }

    public void setSelectedAssetTypeTab(String selectedAssetTypeTab) {
//        Log.e(TAG, "Asset Type set to >>"+selectedAssetTypeTab);
        this.selectedAssetTypeTab = selectedAssetTypeTab;
    }

    public String todaysDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String todayDate = df.format(c);

        return todayDate;
    }

    // Input: 3/16/2021 1:14:14 PM
    // Output: 16-Mar-2021 1:14:14 PM
    public String formatDate(String date) {       // Converting string to date for sorting
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        String dateTime[] = date.split(" ");
        String[] dp = dateTime[0].split("/");

        return dp[1] + "-" + months[Integer.parseInt(dp[0]) - 1] + "-" + dp[2] + " " + dateTime[1] + " " + dateTime[2];
    }       // end formatDate

    // Input: 3/16/2021 1:14:14 PM
    // Output: 16-Mar-2021
    public String formatDateShort(String date) {       // Converting string to date for sorting
//        Log.e("GloabalCode", "Input Date>>" + date);
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        String dateTime[] = date.split(" ");
        String[] dp = dateTime[0].split("/");

        return dp[1] + "-" + months[Integer.parseInt(dp[0]) - 1] + "-" + dp[2];
    }       // end formatDate

    // Input: 2020-07-10T16:46:59.053+05:30
    // Output: 10-Jul-2020
    public String formatDateShortV2(String date) {       // Converting string to date for sorting
//        Log.e("GloabalCode", "Input Date>>" + date);
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        String dateTime[] = date.split("T");
        String[] dp = dateTime[0].split("-");
//        Log.e("GlobalCode","dp>>"+dp.length);
        return dp[2] + "-" + months[Integer.parseInt(dp[1]) - 1] + "-" + dp[0];
    }       // end formatDate


}
