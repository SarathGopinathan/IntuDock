package com.intutrack.intudock.Models.AddTransaction;

import com.google.gson.annotations.SerializedName;

public class WarehouseRequest {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
