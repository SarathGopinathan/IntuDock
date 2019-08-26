package com.intutrack.intudock.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.intutrack.intudock.Preference.Preferences;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Utilities.Constants;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {

    public static Context mContext;
    // variable to track event time
    public long mLastClickTime = 0;
    long startTime, elapsedTime;
    private ProgressDialog dialog;
    boolean isActive = false;

    BroadcastReceiver logoutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (isActive) {

                logout();

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(logoutReceiver, new IntentFilter(Constants.ACTION_LOGOUT));
    }

    protected void showProgressDialog(String message) {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = ProgressDialog.show(this, getString(R.string.app_name), message);
    }

    protected void showProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = ProgressDialog.show(this, getString(R.string.app_name), getString(R.string.prg_dial_pleaseWait));
    }

    protected void hideProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    //This function is cancel progress dialog
    public void cancelProgressDialog() {
        dialog.dismiss();
    }


    // Avoid Multiple click
    public boolean issingleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }

    public void hideKeyboard() {

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm.isAcceptingText()) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }

//    public void ClearPrefs() {
//        unsubscribeToFCM();
//        Preferences.removePreference(getApplicationContext(), PrefEntities.EMAIL);
//        Preferences.removePreference(getApplicationContext(), PrefEntities.ISLOGIN);
//        Preferences.removePreference(getApplicationContext(), PrefEntities.PHONE);
//        Preferences.removePreference(getApplicationContext(), PrefEntities.AUTH_TOKEN);
//        Preferences.removePreference(getApplicationContext(), PrefEntities.CHAT);
//        Preferences.removePreference(getApplicationContext(), PrefEntities.USERID);
//        Preferences.removePreference(getApplicationContext(), PrefEntities.CLIENT);
//        Preferences.removePreference(getApplicationContext(), PrefEntities.SUBCLIENT);
//    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    void logout() {

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_token_expire);

        Button ok = dialog.findViewById(R.id.btn_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Preferences.removeAllPreference(mContext);
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.setCancelable(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

}
