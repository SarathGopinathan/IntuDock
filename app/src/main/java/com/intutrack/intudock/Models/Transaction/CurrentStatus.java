package com.intutrack.intudock.Models.Transaction;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CurrentStatus{

    @SerializedName("status")
    private String status;

    @SerializedName("time")
    private long time;

    public String getStatus() {
        return status;
    }

    public long getTime() {
        return time;
    }
}
