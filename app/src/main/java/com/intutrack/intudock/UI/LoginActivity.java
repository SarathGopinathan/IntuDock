package com.intutrack.intudock.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.intutrack.intudock.Models.Login.LoginResponse;
import com.intutrack.intudock.Preference.PrefEntities;
import com.intutrack.intudock.Preference.Preferences;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Retrofit.Helper.APIUtility;
import com.intutrack.intudock.Utilities.CommonUtils;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.btn_login);

        email = findViewById(R.id.et_login_email);
        password = findViewById(R.id.et_login_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate())
                    loginAPI(email.getText().toString().trim(), password.getText().toString().trim());

            }
        });
    }

    private boolean validate() {

        if (email.getText().toString().equals("")){
            CommonUtils.alert(this, "Please enter an email");
            return false;
        }
        else if(password.getText().toString().equals("")){

            CommonUtils.alert(this, "Please enter a password");
            return false;
        }
        else if(email.getText().toString().equals("") && password.getText().toString().equals("")){
            CommonUtils.alert(this, "Please enter a email and password");
            return false;
        }

        return true;
    }

    private void loginAPI(final String username, String password) {

        IntuApplication.getApiUtility().Login(LoginActivity.this, true, username, password, new APIUtility.APIResponseListener<LoginResponse>() {
            @Override
            public void onReceiveResponse(LoginResponse response) {

                Preferences.setPreference(getApplicationContext(), PrefEntities.ISLOGIN, true);
                Preferences.setPreference(getApplicationContext(), PrefEntities.EMAIL, response.getResult().get(0).getUser().getUsername());
                Preferences.setPreference(getApplicationContext(), PrefEntities.AUTH_TOKEN, response.getResult().get(0).getToken());
                Preferences.setPreference(getApplicationContext(), PrefEntities.CLIENT, response.getResult().get(0).getUser().getConfig().getClient());
                Preferences.setPreference(getApplicationContext(), PrefEntities.SLOTS_CLICK_LISTENER_FLAG, response.getResult().get(0).getUser().getConfig().isSlotsClickListenerFlag());
                Preferences.setPreference(getApplicationContext(), PrefEntities.USERID, response.getResult().get(0).getUser().getUser_id());

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

            }
        });

    }
}