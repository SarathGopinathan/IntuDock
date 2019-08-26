package com.intutrack.intudock.Models.TransactionStatus;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.Transaction.CurrentStatus;

import java.util.ArrayList;

public class TransactionsStatusResultModel {

    @SerializedName("message")
    private String message;

    @SerializedName("statusListDropdown")
    private ArrayList<CurrentStatus> currentStatus;

    public String getMessage() {
        return message;
    }

    public ArrayList<CurrentStatus> getCurrentStatus() {
        return currentStatus;
    }
}
