package com.intutrack.intudock.Models.AssignSlotToTransaction;

import com.google.gson.annotations.SerializedName;

public class AssignSlotToTransactionRequest {

    @SerializedName("dock")
    private AssignSlotToTransactionDock dock;

    public void setDock(AssignSlotToTransactionDock dock) {
        this.dock = dock;
    }
}
