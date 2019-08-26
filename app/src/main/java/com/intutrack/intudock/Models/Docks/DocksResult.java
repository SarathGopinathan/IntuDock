package com.intutrack.intudock.Models.Docks;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.SlotsData.SlotsModel;

import java.util.ArrayList;

public class DocksResult {

    @SerializedName("_id")
    private String dockId;

    @SerializedName("dockName")
    private String dockName;

    @SerializedName("type")
    private ArrayList type;

    @SerializedName("bookedSlotCount")
    private String bookedSlots;

    @SerializedName("availableSlotCount")
    private String availableSlots;

    @SerializedName("slotSize")
    private long slotSize;

    @SerializedName("runningHours")
    private RunningHours runningHours;

    @SerializedName("slots")
    private ArrayList<SlotsModel> slots;

    public String getDockId() {
        return dockId;
    }

    public String getDockName() {
        return dockName;
    }

    public ArrayList getType() {
        return type;
    }

    public String getBookedSlots() {
        return bookedSlots;
    }

    public String getAvailableSlots() {
        return availableSlots;
    }

    public ArrayList<SlotsModel> getSlots() {
        return slots;
    }

    public long getSlotSize() {
        return slotSize;
    }

    public RunningHours getRunningHours() {
        return runningHours;
    }

    public void setDockId(String dockId) {
        this.dockId = dockId;
    }

    public void setDockName(String dockName) {
        this.dockName = dockName;
    }

    public void setType(ArrayList type) {
        this.type = type;
    }

    public void setBookedSlots(String bookedSlots) {
        this.bookedSlots = bookedSlots;
    }

    public void setAvailableSlots(String availableSlots) {
        this.availableSlots = availableSlots;
    }

    public void setSlotSize(long slotSize) {
        this.slotSize = slotSize;
    }

    public void setRunningHours(RunningHours runningHours) {
        this.runningHours = runningHours;
    }

    public void setSlots(ArrayList<SlotsModel> slots) {
        this.slots = slots;
    }
}
