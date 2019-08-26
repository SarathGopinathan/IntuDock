package com.intutrack.intudock.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.intutrack.intudock.Preference.PrefEntities;
import com.intutrack.intudock.Preference.Preferences;
import com.intutrack.intudock.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(Preferences.getPreference_boolean(SplashScreenActivity.this, PrefEntities.ISLOGIN)){

                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();

                }else{

                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();

                }

            }
        },SPLASH_TIME_OUT);

    }

}
