package com.intutrack.intudock.Models.Login;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.BaseResponse;

public class LoginResult extends BaseResponse {

    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private User user;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
