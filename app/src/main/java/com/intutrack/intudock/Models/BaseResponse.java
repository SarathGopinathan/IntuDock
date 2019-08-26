package com.intutrack.intudock.Models;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("res")
    private int res;

    @SerializedName("message")
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
