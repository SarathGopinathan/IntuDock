package com.intutrack.intudock.Models.EditTransaction;

import com.google.gson.annotations.SerializedName;
import com.intutrack.intudock.Models.AddTransaction.AddTransactionResultDockResponse;

public class EditTransactionResultResponse {

    @SerializedName("assignmentErrorObject")
    private EditTransactionResultDockResponse dock;

    public EditTransactionResultDockResponse getDock() {
        return dock;
    }
}
