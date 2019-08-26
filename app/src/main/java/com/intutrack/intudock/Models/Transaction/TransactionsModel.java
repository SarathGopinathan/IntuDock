package com.intutrack.intudock.Models.Transaction;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.BaseResponse;
import com.intutrack.intudock.Models.Docks.DocksResult;

import java.util.ArrayList;

public class TransactionsModel extends BaseResponse {

    @SerializedName("result")
    private ArrayList<TransactionsResult> result;

    public ArrayList<TransactionsResult> getResult() {
        return result;
    }

}
