package com.intutrack.intudock.Models.Login;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.BaseResponse;

import java.util.ArrayList;

public class LoginResponse extends BaseResponse {

    @SerializedName("result")
    private ArrayList<LoginResult> result = new ArrayList<>();

    public ArrayList<LoginResult> getResult() {
        return result;
    }
}
