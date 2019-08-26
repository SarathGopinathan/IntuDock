package com.intutrack.intudock.Models.Transaction;

import com.google.gson.annotations.SerializedName;

public class Dock {

    @SerializedName("start")
    private long start;

    @SerializedName("obj")
    private DockObj obj;

    public long getStart() {
        return start;
    }

    public DockObj getObj() {
        return obj;
    }
}
