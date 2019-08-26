package com.intutrack.intudock.Models.Transaction;

import com.google.gson.annotations.SerializedName;

public class DockObj {

    @SerializedName("_id")
    private String dockId;

    @SerializedName("dockName")
    private String dockName;

    public String getDockId() {
        return dockId;
    }

    public String getDockName() {
        return dockName;
    }
}
