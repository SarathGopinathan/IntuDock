package com.intutrack.intudock.Models.Warehouse;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.BaseResponse;

import java.util.ArrayList;

public class WarehouseModel extends BaseResponse {

    @SerializedName("result")
    private ArrayList<WarehouseResult> result;

    public ArrayList<WarehouseResult> getResult() {
        return result;
    }
}
