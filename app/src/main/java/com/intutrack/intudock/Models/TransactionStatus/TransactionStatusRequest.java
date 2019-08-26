package com.intutrack.intudock.Models.TransactionStatus;

import com.google.gson.annotations.SerializedName;

public class TransactionStatusRequest {

    @SerializedName("id")
    private String transactionId;

    @SerializedName("newStatus")
    private String newStatus;

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}
