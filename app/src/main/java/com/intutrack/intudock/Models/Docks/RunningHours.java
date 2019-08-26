package com.intutrack.intudock.Models.Docks;

import com.google.gson.annotations.SerializedName;

public class RunningHours {

    @SerializedName("start")
    private long start;

    @SerializedName("end")
    private long end;

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
