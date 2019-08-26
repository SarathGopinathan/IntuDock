package com.intutrack.intudock.Models.Docks;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.BaseResponse;

import java.util.ArrayList;

public class DocksModel extends BaseResponse {

    @SerializedName("result")
    private ArrayList<DocksResult> result;

    public ArrayList<DocksResult> getResult() {
        return result;
    }

    public void setResult(ArrayList<DocksResult> result) {
        this.result = result;
    }
}
