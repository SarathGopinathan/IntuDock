package com.intutrack.intudock.Models.Login;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.Login.Config;

public class User {

    @SerializedName("username")
    private String username;

    @SerializedName("_id")
    private String user_id;

    @SerializedName("config")
    private Config config;

    public String getUsername() {
        return username;
    }

    public Config getConfig() {
        return config;
    }

    public String getUser_id() {
        return user_id;
    }
}
