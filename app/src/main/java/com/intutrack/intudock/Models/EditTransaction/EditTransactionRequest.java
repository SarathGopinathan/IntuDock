package com.intutrack.intudock.Models.EditTransaction;

import com.google.gson.annotations.SerializedName;

public class EditTransactionRequest {

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("change")
    private EditTransactionChangeRequest change;

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setChange(EditTransactionChangeRequest change) {
        this.change = change;
    }
}
