package com.intutrack.intudock.Models.Login;

import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("client")
    private String client;

    @SerializedName("slotsClickListenerFlag")
    private boolean slotsClickListenerFlag;

    public String getClient() {
        return client;
    }

    public boolean isSlotsClickListenerFlag() {
        return slotsClickListenerFlag;
    }
}
