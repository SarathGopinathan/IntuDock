package com.intutrack.intudock.Models.AddTransaction;

import com.google.gson.annotations.SerializedName;

public class AddTransactionResultResponse {

    @SerializedName("assignmentErrorObject")
    private AddTransactionResultDockResponse dock;

    public AddTransactionResultDockResponse getDock() {
        return dock;
    }
}
