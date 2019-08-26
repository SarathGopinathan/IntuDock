package com.intutrack.intudock.Retrofit;
import com.intutrack.intudock.Models.AddTransaction.AddTransactionRequest;
import com.intutrack.intudock.Models.AddTransaction.AddTransactionResponse;
import com.intutrack.intudock.Models.AssignSlotToTransaction.AssignSlotToTransactionModel;
import com.intutrack.intudock.Models.AssignSlotToTransaction.AssignSlotToTransactionRequest;
import com.intutrack.intudock.Models.CancelTransaction.CancelTransactionResponse;
import com.intutrack.intudock.Models.Docks.DocksModel;
import com.intutrack.intudock.Models.EditTransaction.EditTransactionRequest;
import com.intutrack.intudock.Models.EditTransaction.EditTransactionResponse;
import com.intutrack.intudock.Models.Login.LoginRequest;
import com.intutrack.intudock.Models.Login.LoginResponse;
import com.intutrack.intudock.Models.SlotsData.SlotModel;
import com.intutrack.intudock.Models.Transaction.TransactionsModel;
import com.intutrack.intudock.Models.TransactionStatus.TransactionStatusRequest;
import com.intutrack.intudock.Models.TransactionStatus.TransactionsStatusModel;
import com.intutrack.intudock.Models.Warehouse.WarehouseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface APIService {

    @POST("login")
    Call<LoginResponse> Login(@Body LoginRequest request);

    @GET
    Call<WarehouseModel> WarehouseData(@Url String url);

    @GET
    Call<DocksModel> DockData(@Url String url);

    @GET
    Call<TransactionsModel> TransactionData(@Url String url);

    @PUT("transactions/changestatus")
    Call<TransactionsStatusModel> TransactionData(@Body TransactionStatusRequest request);

    @GET
    Call<SlotModel> SlotData(@Url String url);

    @PUT
    Call<AssignSlotToTransactionModel> AssignSlotToTransaction(@Url String url, @Body AssignSlotToTransactionRequest request);

    @PUT
    Call<CancelTransactionResponse> CancelTransaction(@Url String url);

    @POST("transactions/create")
    Call<AddTransactionResponse> AddTransaction(@Body AddTransactionRequest request);

    @PUT("transactions/edit")
    Call<EditTransactionResponse> EditTransaction(@Body EditTransactionRequest request);

}
