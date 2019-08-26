package com.intutrack.intudock.Models.EditTransaction;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EditTransactionChangeRequest {

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
    private EditTransactionChangeSourceRequest srcWarehouse;

    @SerializedName("dock")
    private EditTransactionDockRequest dock;

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

    public void setDock(EditTransactionDockRequest dock) {
        this.dock = dock;
    }

    public void setSrcWarehouse(EditTransactionChangeSourceRequest srcWarehouse) {
        this.srcWarehouse = srcWarehouse;
    }
}
