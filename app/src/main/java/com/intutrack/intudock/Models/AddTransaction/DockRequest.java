package com.intutrack.intudock.Models.AddTransaction;

import com.google.gson.annotations.SerializedName;

public class DockRequest {

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

    public String getId() {
        return id;
    }

    public long getStart() {
        return start;
    }

    public long getDuration() {
        return duration;
    }
}
