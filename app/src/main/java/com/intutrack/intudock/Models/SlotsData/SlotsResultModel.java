package com.intutrack.intudock.Models.SlotsData;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SlotsResultModel {

    @SerializedName("slots")
    private ArrayList<SlotsModel> slots;

    public ArrayList<SlotsModel> getSlots() {
        return slots;
    }
}
