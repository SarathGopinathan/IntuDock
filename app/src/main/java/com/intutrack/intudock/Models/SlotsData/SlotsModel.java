package com.intutrack.intudock.Models.SlotsData;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.Transaction.TransactionsResult;

import java.util.ArrayList;

public class SlotsModel {

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("booked")
    private boolean booked;

    @SerializedName("bookable")
    private boolean bookable;

    @SerializedName("start")
    private long start;

    @SerializedName("duration")
    private long duration;

    @SerializedName("transaction")
    private TransactionsResult transactionResult;

    public String getTransactionId() {
        return transactionId;
    }

    public boolean isBooked() {
        return booked;
    }

    public long getStart() {
        return start;
    }

    public long getDuration() {
        return duration;
    }

    public TransactionsResult getTransactionResult() {
        return transactionResult;
    }

    public boolean isBookable() {
        return bookable;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public void setBookable(boolean bookable) {
        this.bookable = bookable;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setTransactionResult(TransactionsResult transactionResult) {
        this.transactionResult = transactionResult;
    }
}
