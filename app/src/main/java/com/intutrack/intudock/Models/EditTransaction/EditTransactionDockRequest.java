package com.intutrack.intudock.Models.EditTransaction;

import com.google.gson.annotations.SerializedName;

public class EditTransactionDockRequest {

    @SerializedName("id")
    private String id;

    @SerializedName("start")
    private long start;

    @SerializedName("duration")
    private long duration;

    public void setId(String id) {
        this.id = id;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
