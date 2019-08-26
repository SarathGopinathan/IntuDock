package com.intutrack.intudock.Models.AddTransaction;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.BaseResponse;

import java.util.ArrayList;

public class AddTransactionResponse extends BaseResponse {

    @SerializedName("result")
    private ArrayList<AddTransactionResultResponse> result;

    public ArrayList<AddTransactionResultResponse> getResult() {
        return result;
    }
}
