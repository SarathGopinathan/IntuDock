package com.intutrack.intudock.Models.AddTransaction;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddTransactionRequest {

    @SerializedName("lrNumber")
    private String lrNumber;

    @SerializedName("vehicleNumber")
    private String vehicleNumber;

    @SerializedName("tel")
    private ArrayList phoneNumber;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private String status;

    @SerializedName("transporter")
    private String transporter;

    @SerializedName("srcWarehouse")
    private WarehouseRequest srcWarehouse;

    @SerializedName("destWarehouse")
    private WarehouseRequest destWarehouse;

    @SerializedName("dock")
    private DockRequest dock;

    public void setLrNumber(String lrNumber) {
        this.lrNumber = lrNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setPhoneNumber(ArrayList phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }

    public void setSrcWarehouse(WarehouseRequest srcWarehouse) {
        this.srcWarehouse = srcWarehouse;
    }

    public void setDestWarehouse(WarehouseRequest destWarehouse) {
        this.destWarehouse = destWarehouse;
    }

    public void setDock(DockRequest dock) {
        this.dock = dock;
    }
}
