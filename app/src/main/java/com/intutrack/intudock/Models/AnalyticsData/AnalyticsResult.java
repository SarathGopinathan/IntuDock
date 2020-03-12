package com.intutrack.intudock.Models.AnalyticsData;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AnalyticsResult {

    @SerializedName("totalTransactions")
    private String totalTransactions;

    @SerializedName("completedTransactions")
    private String completedTransactions;

    @SerializedName("completedTransactionPercent")
    private String completedTransactionPercent;

    @SerializedName("upcomingTransactions")
    private String upcomingTransactions;

    @SerializedName("upcomingTransactionsPercent")
    private String upcomingTransactionsPercent;

    @SerializedName("totalDocks")
    private String totalDocks;

    @SerializedName("occupied")
    private String occupied;

    @SerializedName("unoccupied")
    private String unoccupied;

    @SerializedName("mostUsed")
    private String mostUsed;

    @SerializedName("leastUsed")
    private String leastUsed;

    @SerializedName("totalUtilization")
    private String totalUtilization;

    @SerializedName("peakhour")
    private String peakhour;

    @SerializedName("peakhourTransactions")
    private String peakhourTransactions;

    @SerializedName("offpeakhour")
    private String offpeakhour;

    @SerializedName("offpeakhourTransactions")
    private String offpeakhourTransactions;

    @SerializedName("avgLoadingTime")
    private String avgLoadingTime;

    @SerializedName("avgLoadingTimePercent")
    private String avgLoadingTimePercent;

    @SerializedName("avgUnloadingTime")
    private String avgUnloadingTime;

    @SerializedName("avgUnloadingTimePercent")
    private String avgUnloadingTimePercent;

    @SerializedName("avgActivityTime")
    private String avgActivityTime;

    @SerializedName("avgActivityTimePercent")
    private String avgActivityTimePercent;

    @SerializedName("graphData")
    private ArrayList graphData;

    public String getTotalTransactions() {
        return totalTransactions;
    }

    public String getCompletedTransactions() {
        return completedTransactions;
    }

    public String getCompletedTransactionPercent() {
        return completedTransactionPercent;
    }

    public String getUpcomingTransactions() {
        return upcomingTransactions;
    }

    public String getUpcomingTransactionsPercent() {
        return upcomingTransactionsPercent;
    }

    public String getTotalDocks() {
        return totalDocks;
    }

    public String getOccupied() {
        return occupied;
    }

    public String getUnoccupied() {
        return unoccupied;
    }

    public String getMostUsed() {
        return mostUsed;
    }

    public String getLeastUsed() {
        return leastUsed;
    }

    public String getTotalUtilization() {
        return totalUtilization;
    }

    public String getPeakhour() {
        return peakhour;
    }

    public String getPeakhourTransactions() {
        return peakhourTransactions;
    }

    public String getOffpeakhour() {
        return offpeakhour;
    }

    public String getOffpeakhourTransactions() {
        return offpeakhourTransactions;
    }

    public String getAvgLoadingTime() {
        return avgLoadingTime;
    }

    public String getAvgLoadingTimePercent() {
        return avgLoadingTimePercent;
    }

    public String getAvgUnloadingTime() {
        return avgUnloadingTime;
    }

    public String getAvgUnloadingTimePercent() {
        return avgUnloadingTimePercent;
    }

    public String getAvgActivityTime() {
        return avgActivityTime;
    }

    public String getAvgActivityTimePercent() {
        return avgActivityTimePercent;
    }

    public ArrayList getGraphData() {
        return graphData;
    }

}
