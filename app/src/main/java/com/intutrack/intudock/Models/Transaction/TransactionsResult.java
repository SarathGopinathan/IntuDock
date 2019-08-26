package com.intutrack.intudock.Models.Transaction;

import androidx.versionedparcelable.ParcelField;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TransactionsResult {

    @SerializedName("_id")
    private String transactionId;

    @SerializedName("lrNumber")
    private String lrNumber;

    @SerializedName("type")
    private String type;

    @SerializedName("dock")
    private Dock dockDetails;

    @SerializedName("createdAt")
    private long date;

    @SerializedName("srcWarehouse")
    private Warehouse srcWarehouse;

    @SerializedName("destWarehouse")
    private Warehouse destWarehouse;

    @SerializedName("vehicleNumber")
    private String vehicleNumber;

    @SerializedName("tel")
    private ArrayList phoneNumber;

    @SerializedName("transporter")
    private String transporterName;

    @SerializedName("assigned")
    private boolean assigned;

    @SerializedName("statusListDropdown")
    private ArrayList<CurrentStatus> currentStatus;

    public String getTransactionId() {
        return transactionId;
    }

    public String getLrNumber() {
        return lrNumber;
    }

    public String getType() {
        return type;
    }

    public Dock getDockDetails() {
        return dockDetails;
    }

    public long getDate() {
        return date;
    }

    public Warehouse getSrcWarehouse() {
        return srcWarehouse;
    }

    public Warehouse getDestWarehouse() {
        return destWarehouse;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public ArrayList getPhoneNumber() {
        return phoneNumber;
    }

    public String getTransporterName() {
        return transporterName;
    }

    public ArrayList<CurrentStatus> getCurrentStatus() {
        return currentStatus;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setLrNumber(String lrNumber) {
        this.lrNumber = lrNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDockDetails(Dock dockDetails) {
        this.dockDetails = dockDetails;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setSrcWarehouse(Warehouse srcWarehouse) {
        this.srcWarehouse = srcWarehouse;
    }

    public void setDestWarehouse(Warehouse destWarehouse) {
        this.destWarehouse = destWarehouse;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setPhoneNumber(ArrayList phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTransporterName(String transporterName) {
        this.transporterName = transporterName;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public void setCurrentStatus(ArrayList<CurrentStatus> currentStatus) {
        this.currentStatus = currentStatus;
    }
}
