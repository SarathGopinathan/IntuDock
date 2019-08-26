package com.intutrack.intudock.Models.SlotsData;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.BaseResponse;

import java.util.ArrayList;

public class SlotModel extends BaseResponse {

    @SerializedName("result")
    private ArrayList<SlotsResultModel> result;

    public ArrayList<SlotsResultModel> getResult() {
        return result;
    }
}
