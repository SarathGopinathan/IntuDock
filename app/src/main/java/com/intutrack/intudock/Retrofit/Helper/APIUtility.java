package com.intutrack.intudock.Retrofit.Helper;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import com.intutrack.intudock.Preference.PrefEntities;
import com.intutrack.intudock.Preference.Preferences;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Retrofit.APIService;
import com.intutrack.intudock.Utilities.CommonUtils;
import com.intutrack.intudock.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIUtility {
    private static final String TAG = "APIUtility";
    private static APIService mApiService = null;

    public APIUtility(Context context) {
        APIServiceGenerator.addHeader("Content-Type", "application/json");
        mApiService = APIServiceGenerator.createService(APIService.class, "Bearer " + Preferences.getPreference(context, PrefEntities.AUTH_TOKEN));

    }

    public void showDialog(Context context, boolean isDialog) {
        if (isDialog) {
            ProcessDialog.start(context);
        }
    }

    public void dismissDialog(boolean isDialog) {
        if (isDialog) {
            ProcessDialog.dismiss();
        }
    }

    public void Login(final Context context, final boolean isDialog, String username, String password, final APIResponseListener<LoginResponse> listener) {
        showDialog(context, isDialog);
        mApiService = APIServiceGenerator.createService(APIService.class, username, password);
        mApiService.Login(new LoginRequest("")).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                dismissDialog(isDialog);
                Log.e("TAG",response.code()+"");
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        listener.onReceiveResponse(response.body());
                        Preferences.setPreference(context, PrefEntities.AUTH_TOKEN, response.body().getResult().get(0).getToken());
                        mApiService = APIServiceGenerator.createService(APIService.class, "Bearer " + Preferences.getPreference(context, PrefEntities.AUTH_TOKEN));
                    } else {
                        CommonUtils.alert(context, response.body().getMessage());
                    }
                } else {
                    if (response.code() == 403){

                        try {
                            displayErrorMessage(response.errorBody().string(), context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.ACTION_LOGOUT));
                    }
                    else
                        CommonUtils.alert(context, context.getString(R.string.error));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                dismissDialog(isDialog);
                CommonUtils.alert(context, context.getString(R.string.error));
//                CommonUtils.alert(context, t.getMessage());
            }
        });
    }

    public void WarehouseData(final Context context, final boolean isDialog, String date, final APIResponseListener<WarehouseModel> listener) {
        showDialog(context, isDialog);

        String url = "https://6mwou5x4fk.execute-api.ap-south-1.amazonaws.com/dev/warehouses/pagedata?date=" + date;

        mApiService.WarehouseData(url).enqueue(new Callback<WarehouseModel>() {
            @Override
            public void onResponse(Call<WarehouseModel> call, Response<WarehouseModel> response) {
                dismissDialog(isDialog);
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        listener.onReceiveResponse(response.body());
                    } else {
                        CommonUtils.alert(context, response.body().getMessage());
                    }
                } else {
                    if (response.code() == 403)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.ACTION_LOGOUT));
                    else{
                        try {
                            displayErrorMessage(response.errorBody().string(), context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<WarehouseModel> call, Throwable t) {
                dismissDialog(isDialog);
                CommonUtils.alert(context, context.getString(R.string.error));
//                CommonUtils.alert(context, t.getMessage());
            }
        });
    }

    public void DockData(final Context context, final boolean isDialog,String warehouseId, String date, final APIResponseListener<DocksModel> listener) {
        showDialog(context, isDialog);

        String url = "https://6mwou5x4fk.execute-api.ap-south-1.amazonaws.com/dev/docks/pagedata?warehouseId="+ warehouseId + "&date=" + date;

        mApiService.DockData(url).enqueue(new Callback<DocksModel>() {
            @Override
            public void onResponse(Call<DocksModel> call, Response<DocksModel> response) {
                dismissDialog(isDialog);
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        listener.onReceiveResponse(response.body());
                    } else {
                        CommonUtils.alert(context, response.body().getMessage());
                    }
                } else {
                    if (response.code() == 403)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.ACTION_LOGOUT));
                    else{
                        try {
                            displayErrorMessage(response.errorBody().string(), context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DocksModel> call, Throwable t) {
                dismissDialog(isDialog);
                CommonUtils.alert(context, context.getString(R.string.error));
//                CommonUtils.alert(context, t.getMessage());
            }
        });
    }

    public void TransactionsData(final Context context, final boolean isDialog,String warehouseId, final APIResponseListener<TransactionsModel> listener) {

        showDialog(context, isDialog);

        String url = "https://6mwou5x4fk.execute-api.ap-south-1.amazonaws.com/dev/transactions?warehouseId="+ warehouseId;

        mApiService.TransactionData(url).enqueue(new Callback<TransactionsModel>() {
            @Override
            public void onResponse(Call<TransactionsModel> call, Response<TransactionsModel> response) {
                dismissDialog(isDialog);
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        listener.onReceiveResponse(response.body());
                    } else {
                        CommonUtils.alert(context, response.body().getMessage());
                    }
                } else {
                    if (response.code() == 403)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.ACTION_LOGOUT));
                    else{
                        try {
                            displayErrorMessage(response.errorBody().string(), context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionsModel> call, Throwable t) {
                dismissDialog(isDialog);
                CommonUtils.alert(context, context.getString(R.string.error));
//                CommonUtils.alert(context, t.getMessage());
            }
        });
    }

    public void TransactionStatusChange(final Context context, final boolean isDialog, TransactionStatusRequest request, final APIResponseListener<TransactionsStatusModel> listener) {

        showDialog(context, isDialog);

        mApiService.TransactionData(request).enqueue(new Callback<TransactionsStatusModel>() {
            @Override
            public void onResponse(Call<TransactionsStatusModel> call, Response<TransactionsStatusModel> response) {
                dismissDialog(isDialog);
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        listener.onReceiveResponse(response.body());
                    } else {
                        CommonUtils.alert(context, response.body().getMessage());
                    }
                } else {
                    if (response.code() == 403)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.ACTION_LOGOUT));
                    else{
                        try {
                            displayErrorMessage(response.errorBody().string(), context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionsStatusModel> call, Throwable t) {
                dismissDialog(isDialog);
                CommonUtils.alert(context, context.getString(R.string.error));
//                CommonUtils.alert(context, t.getMessage());
            }
        });
    }

    public void SlotData(final Context context, final boolean isDialog,String dockId, String date, final APIResponseListener<SlotModel> listener) {

        showDialog(context, isDialog);

        String url = "https://6mwou5x4fk.execute-api.ap-south-1.amazonaws.com/dev/docks/slots?id=" + dockId + "&date=" + date;

        mApiService.SlotData(url).enqueue(new Callback<SlotModel>() {
            @Override
            public void onResponse(Call<SlotModel> call, Response<SlotModel> response) {
                dismissDialog(isDialog);
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        listener.onReceiveResponse(response.body());
                    } else {
                        CommonUtils.alert(context, response.body().getMessage());
                    }
                } else {
                    if (response.code() == 403)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.ACTION_LOGOUT));
                    else{
                        try {
                            displayErrorMessage(response.errorBody().string(), context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SlotModel> call, Throwable t) {
                dismissDialog(isDialog);
                CommonUtils.alert(context, context.getString(R.string.error));
//                CommonUtils.alert(context, t.getMessage());
            }
        });
    }

    public void AssignSlotToTransaction(final Context context, final boolean isDialog, String transactionId,
                                        AssignSlotToTransactionRequest request, final APIResponseListener<AssignSlotToTransactionModel> listener) {

        showDialog(context, isDialog);

        String url = "https://6mwou5x4fk.execute-api.ap-south-1.amazonaws.com/dev/transactions/assign/" + transactionId;

        mApiService.AssignSlotToTransaction(url, request).enqueue(new Callback<AssignSlotToTransactionModel>() {
            @Override
            public void onResponse(Call<AssignSlotToTransactionModel> call, Response<AssignSlotToTransactionModel> response) {
                dismissDialog(isDialog);
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        listener.onReceiveResponse(response.body());
                    } else {
                        CommonUtils.alert(context, response.body().getMessage());
                    }
                } else {
                    if (response.code() == 403)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.ACTION_LOGOUT));
                    else{
                        try {
                            displayErrorMessage(response.errorBody().string(), context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AssignSlotToTransactionModel> call, Throwable t) {
                dismissDialog(isDialog);
                CommonUtils.alert(context, context.getString(R.string.error));
//                CommonUtils.alert(context, t.getMessage());
            }
        });
    }

    public void CancelTransaction(final Context context, final boolean isDialog,String transactionId, final APIResponseListener<CancelTransactionResponse> listener) {

        showDialog(context, isDialog);

        String url = "https://6mwou5x4fk.execute-api.ap-south-1.amazonaws.com/dev/transactions/unassign/" + transactionId;

        mApiService.CancelTransaction(url).enqueue(new Callback<CancelTransactionResponse>() {
            @Override
            public void onResponse(Call<CancelTransactionResponse> call, Response<CancelTransactionResponse> response) {
                dismissDialog(isDialog);
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        listener.onReceiveResponse(response.body());
                    } else {
                        CommonUtils.alert(context, response.body().getMessage());
                    }
                } else {
                    if (response.code() == 403)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.ACTION_LOGOUT));
                    else{
                        try {
                            displayErrorMessage(response.errorBody().string(), context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CancelTransactionResponse> call, Throwable t) {
                dismissDialog(isDialog);
                CommonUtils.alert(context, context.getString(R.string.error));
//                CommonUtils.alert(context, t.getMessage());
            }
        });
    }

    public void AddTransaction(final Context context, final boolean isDialog, AddTransactionRequest request, final APIResponseListener<AddTransactionResponse> listener) {

        showDialog(context, isDialog);

        mApiService.AddTransaction(request).enqueue(new Callback<AddTransactionResponse>() {
            @Override
            public void onResponse(Call<AddTransactionResponse> call, Response<AddTransactionResponse> response) {
                dismissDialog(isDialog);
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        listener.onReceiveResponse(response.body());
                    } else {
                        CommonUtils.alert(context, response.body().getMessage());
                    }
                } else {
                    if (response.code() == 403)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.ACTION_LOGOUT));
                    else{
                        try {
                            displayErrorMessage(response.errorBody().string(), context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AddTransactionResponse> call, Throwable t) {
                dismissDialog(isDialog);
                Log.e("onFailure: ", t.getMessage());
                CommonUtils.alert(context, context.getString(R.string.error));
//                CommonUtils.alert(context, t.getMessage());
            }
        });
    }

    public void EditTransaction(final Context context, final boolean isDialog, EditTransactionRequest request, final APIResponseListener<EditTransactionResponse> listener) {

        showDialog(context, isDialog);

        mApiService.EditTransaction(request).enqueue(new Callback<EditTransactionResponse>() {
            @Override
            public void onResponse(Call<EditTransactionResponse> call, Response<EditTransactionResponse> response) {
                dismissDialog(isDialog);
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        listener.onReceiveResponse(response.body());
                    } else {
                        CommonUtils.alert(context, response.body().getMessage());
                    }
                } else {
                    if (response.code() == 403)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.ACTION_LOGOUT));
                    else{
                        try {
                            displayErrorMessage(response.errorBody().string(), context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EditTransactionResponse> call, Throwable t) {
                dismissDialog(isDialog);
                CommonUtils.alert(context, context.getString(R.string.error));
//                CommonUtils.alert(context, t.getMessage());
            }
        });
    }

    public interface APIResponseListener<T> {
        void onReceiveResponse(T response);
        /* void onNetworkFailed();*/
    }

    private void displayErrorMessage(String errorBody, Context context) {

        try {
            if (errorBody != null) {
                JSONObject object = new JSONObject(errorBody);
                CommonUtils.alert(context, object.getString("message"));
            } else {
                CommonUtils.alert(context, context.getString(R.string.error));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            CommonUtils.alert(context, context.getString(R.string.error));
        }

    }

}