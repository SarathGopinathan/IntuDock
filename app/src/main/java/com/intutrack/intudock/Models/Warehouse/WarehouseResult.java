package com.intutrack.intudock.Models.Warehouse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WarehouseResult {

    @SerializedName("_id")
    private String warehouseId;

    @SerializedName("warehouseName")
    private String warehouseName;

    @SerializedName("city")
    private String city;

    @SerializedName("dockCount")
    private int totalDocks;

    @SerializedName("activeTransactionCount")
    private int assignedTransactions;

    @SerializedName("inactiveSlotCount")
    private int openTransactions;

    @SerializedName("cities")
    private ArrayList cities;

    public String getWarehouseId() {
        return warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public String getCity() {
        return city;
    }

    public int getTotalDocks() {
        return totalDocks;
    }

    public int getAssignedTransactions() {
        return assignedTransactions;
    }

    public int getOpenTransactions() {
        return openTransactions;
    }

    public ArrayList getCities() {
        return cities;
    }
}
