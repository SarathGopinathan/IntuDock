package com.intutrack.intudock.UI;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.intutrack.intudock.Models.CancelTransaction.CancelTransactionResponse;
import com.intutrack.intudock.Models.TransactionStatus.TransactionStatusRequest;
import com.intutrack.intudock.Models.TransactionStatus.TransactionsStatusModel;
import com.intutrack.intudock.Preference.Preferences;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Retrofit.Helper.APIUtility;
import com.intutrack.intudock.Utilities.CommonUtils;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TransactionDetailsActivity extends BaseActivity {

    private Spinner currentStatus;
    private EditText transactionId, transporter, date, source, destination, driverNumber, vehicleNumber, eta;
    private Button reschedule, cancelAssignment;
    private TextView transactionType, dockName, slot, duration, timer;
    private ArrayList currentStatusList;
    private LinearLayout linearLayoutTimer;
    private RelativeLayout relativeLayoutBack;
    private String cominFrom, selectedTransactionType, selectedTransactionId, selectedLrNumber, selectedTransporter, selectedDate, selectedSource, selectedDestination, selectedDriver,
            selectedVehicle, selectedDockName, selectedDockId, selectedSlot, selectedDuration, selectedCurrentStatus, selectedCurrentStatusTime,
            selectedWarehouseId, selectedWarehouseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        transactionType = findViewById(R.id.tv_transaction_type);
        currentStatus = findViewById(R.id.spinner_current_status);

        transactionId = findViewById(R.id.et_id);
        transporter = findViewById(R.id.et_transporter);
        date = findViewById(R.id.et_date);
        source = findViewById(R.id.et_source);
        destination = findViewById(R.id.et_destiniation);
        driverNumber = findViewById(R.id.et_driver_number);
        vehicleNumber = findViewById(R.id.et_vehicle);
        eta = findViewById(R.id.et_eta);

        dockName = findViewById(R.id.tv_dock_name);
        slot = findViewById(R.id.tv_slot);
        duration = findViewById(R.id.tv_duration);
        timer = findViewById(R.id.tv_timer);


        linearLayoutTimer = findViewById(R.id.ll_timer);

        relativeLayoutBack = findViewById(R.id.rl_back);

        reschedule = findViewById(R.id.btn_reschedule);
        cancelAssignment = findViewById(R.id.btn_cancel_assignment);

        try {
            cominFrom = getIntent().getExtras().getString("from");
            selectedWarehouseId = getIntent().getExtras().getString("selectedWarehouseId");
            selectedWarehouseName = getIntent().getExtras().getString("selectedWarehouseName");
            selectedTransactionType = getIntent().getExtras().getString("transactionType");
            selectedTransactionId = getIntent().getExtras().getString("transactionId");
            selectedLrNumber = getIntent().getExtras().getString("lrNumber");
            selectedTransporter = getIntent().getExtras().getString("transporter");
            selectedDate = getIntent().getExtras().getString("selectedDate");
            selectedSource = getIntent().getExtras().getString("source");
            selectedDestination = getIntent().getExtras().getString("destination");
            selectedDriver = getIntent().getExtras().getString("driver");
            selectedVehicle = getIntent().getExtras().getString("vehicle");
            selectedCurrentStatus = getIntent().getExtras().getString("currentStatus");
            selectedCurrentStatusTime = getIntent().getExtras().getString("currentStatusTime");
            selectedDockName = getIntent().getExtras().getString("dockName");
            selectedDockId = getIntent().getExtras().getString("dockId");
            selectedSlot = getIntent().getExtras().getString("slot");
            selectedDuration = getIntent().getExtras().getString("slotDuration");

            Log.e("TransaDetailsAct","transa ID: " + selectedTransactionId);

        }catch (Exception e){
            cominFrom = "";
            selectedTransactionType = "";
            selectedTransactionId = "";
            selectedLrNumber = "";
            selectedTransporter = "";
            selectedDate = "";
            selectedSource = "";
            selectedDestination = "";
            selectedDriver = "";
            selectedVehicle = "";
            selectedDockName = "";
            selectedSlot = "";
            selectedDuration = "";
        }

        relativeLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        transactionId.setFocusableInTouchMode(false);
        transporter.setFocusableInTouchMode(false);
        date.setFocusableInTouchMode(false);
        source.setFocusableInTouchMode(false);
        destination.setFocusableInTouchMode(false);
        driverNumber.setFocusableInTouchMode(false);
        vehicleNumber.setFocusableInTouchMode(false);
        eta.setFocusableInTouchMode(false);

        transactionType.setText(selectedTransactionType);
        transactionId.setText(selectedLrNumber);
        transporter.setText(selectedTransporter);
        date.setText(selectedDate);
        source.setText(selectedSource);
        destination.setText(selectedDestination);
        driverNumber.setText(selectedDriver);
        vehicleNumber.setText(selectedVehicle);

        currentStatusList = new ArrayList();

        if(selectedCurrentStatus.equals("IN TRANSIT")){
            currentStatusList.add("IN TRANSIT");
            currentStatusList.add("AT GATE");
            currentStatusList.add("AT DOCK");
            currentStatusList.add("COMPLETED");
            currentStatusList.add("REJECTED");
        }
        else if(selectedCurrentStatus.equals("AT GATE")){
            currentStatusList.add("AT GATE");
            currentStatusList.add("AT DOCK");
            currentStatusList.add("COMPLETED");
            currentStatusList.add("REJECTED");
        }
        else if(selectedCurrentStatus.equals("AT DOCK")){
            currentStatusList.add("AT DOCK");
            currentStatusList.add("COMPLETED");
            currentStatusList.add("REJECTED");
        }
        else if(selectedCurrentStatus.equals("COMPLETED")){
            currentStatusList.add("COMPLETED");
        }

        ArrayAdapter<String> spinnerCurrentStatusArrayAdapter = new ArrayAdapter<String>(this,   R.layout.item_spinner_current_status, currentStatusList);
        spinnerCurrentStatusArrayAdapter.setDropDownViewResource(R.layout.item_spinner); // The drop down view
        currentStatus.setAdapter(spinnerCurrentStatusArrayAdapter);

        currentStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentStatus.getSelectedItemPosition() == 0){

                }else{

                    TransactionStatusRequest request = new TransactionStatusRequest();

                    request.setTransactionId(selectedTransactionId);
                    request.setNewStatus(currentStatus.getSelectedItem().toString());

                    IntuApplication.getApiUtility().TransactionStatusChange(TransactionDetailsActivity.this, true, request, new APIUtility.APIResponseListener<TransactionsStatusModel>() {
                        @Override
                        public void onReceiveResponse(TransactionsStatusModel response) {

                            final ArrayList tempStatusList = new ArrayList();

                            for(int i = 0; i < response.getResult().get(0).getCurrentStatus().size(); i++)
                                tempStatusList.add(response.getResult().get(0).getCurrentStatus().get(i).getStatus());

                            currentStatusList.clear();
                            currentStatusList = tempStatusList;

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TransactionDetailsActivity.this,   R.layout.item_spinner_status_content, currentStatusList);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.item_status_spinner); // The drop down view
                            currentStatus.setAdapter(spinnerArrayAdapter);

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(selectedCurrentStatus.equals("AT DOCK")){
            linearLayoutTimer.setVisibility(GONE);

//            Timer updateTimer = new Timer();
//            updateTimer.schedule(new TimerTask() {
//                public void run() {
//                    try {
//
//                        long millis = System.currentTimeMillis() - Long.parseLong(selectedCurrentStatusTime);
//                        long hours = millis/(1000 * 60 * 60);
//                        long mins = (millis/(1000*60)) % 60;
//                        long seconds = (millis/(1000)) % 60;
//                        timer.setText(hours + ":" + mins + ":" + seconds);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }, 0, 1000);
        }
        else{
            linearLayoutTimer.setVisibility(GONE);
        }

        dockName.setText(selectedDockName);
        slot.setText(CommonUtils.convertTimestampToTime(Long.parseLong(selectedSlot)));
        duration.setText(selectedDuration  + " mins");

        reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TransactionDetailsActivity.this, AddTransactionActivity.class);
                intent.putExtra("from","TransactionDetailsActivity");
                intent.putExtra("transactionType", selectedTransactionType);
                intent.putExtra("transactionId", selectedTransactionId);
                intent.putExtra("lrNumber", selectedLrNumber);
                intent.putExtra("transporter", selectedTransporter);
                intent.putExtra("selectedDate", selectedDate);
                intent.putExtra("source", selectedSource);
                intent.putExtra("destination", selectedDestination);
                intent.putExtra("driver", selectedDriver);
                intent.putExtra("vehicle", selectedVehicle);
                intent.putExtra("currentStatus", selectedCurrentStatus);
                intent.putExtra("currentStatusTime", selectedCurrentStatusTime);
                intent.putExtra("dockName", selectedDockName);
                intent.putExtra("dockId", selectedDockId);
                intent.putExtra("slot", selectedSlot);
                intent.putExtra("slotDuration", selectedDuration);
                intent.putExtra("selectedWarehouseId", selectedWarehouseId);
                intent.putExtra("selectedWarehouseName", selectedWarehouseName);
                startActivity(intent);
                finish();

            }
        });

        cancelAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(TransactionDetailsActivity.this);

                dialog.setContentView(R.layout.dialog_cancel_assignment);

                Button yes = dialog.findViewById(R.id.btn_yes);
                Button no = dialog.findViewById(R.id.btn_no);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        IntuApplication.getApiUtility().CancelTransaction(TransactionDetailsActivity.this, true, selectedTransactionId, new APIUtility.APIResponseListener<CancelTransactionResponse>() {
                            @Override
                            public void onReceiveResponse(CancelTransactionResponse response) {
                                finish();
                            }
                        });
                        dialog.dismiss();

                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });

        if (cominFrom.equals("SlotsAdapterBookedExpired")){
            reschedule.setVisibility(GONE);
            cancelAssignment.setVisibility(GONE);
        }else{
            reschedule.setVisibility(VISIBLE);
            cancelAssignment.setVisibility(VISIBLE);
        }

    }
}
