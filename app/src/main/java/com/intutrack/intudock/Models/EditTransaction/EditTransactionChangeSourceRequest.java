package com.intutrack.intudock.Models.EditTransaction;

import com.google.gson.annotations.SerializedName;

public class EditTransactionChangeSourceRequest {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
}
