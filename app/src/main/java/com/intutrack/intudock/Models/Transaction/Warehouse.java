package com.intutrack.intudock.Models.Transaction;

import com.google.gson.annotations.SerializedName;

public class Warehouse {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
