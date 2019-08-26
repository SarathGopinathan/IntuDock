package com.intutrack.intudock.Models.TransactionStatus;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.BaseResponse;

import java.util.ArrayList;

public class TransactionsStatusModel extends BaseResponse {

    @SerializedName("result")
    private ArrayList<TransactionsStatusResultModel> result;

    public ArrayList<TransactionsStatusResultModel> getResult() {
        return result;
    }
}
