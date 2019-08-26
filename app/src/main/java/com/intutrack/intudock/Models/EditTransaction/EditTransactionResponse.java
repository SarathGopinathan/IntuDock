package com.intutrack.intudock.Models.EditTransaction;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.BaseResponse;

import java.util.ArrayList;

public class EditTransactionResponse extends BaseResponse {

    @SerializedName("result")
    private ArrayList<EditTransactionResultResponse> result;

    public ArrayList<EditTransactionResultResponse> getResult() {
        return result;
    }
}
