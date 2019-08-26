package com.intutrack.intudock.Models.AssignSlotToTransaction;

import com.google.gson.annotations.SerializedName;

public class AssignSlotToTransactionDock {

    @SerializedName("id")
    private String dockId;

    @SerializedName("start")
    private long start;

    @SerializedName("duration")
    private long duration;

    public void setDockId(String dockId) {
        this.dockId = dockId;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
