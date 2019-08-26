package com.intutrack.intudock.Models.Login;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("token")
    String token;

    public LoginRequest(String token) {
        this.token = token;
    }
}
