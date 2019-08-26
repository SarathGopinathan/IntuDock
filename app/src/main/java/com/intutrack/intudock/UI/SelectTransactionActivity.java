package com.intutrack.intudock.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.intutrack.intudock.Models.AssignSlotToTransaction.AssignSlotToTransactionDock;
import com.intutrack.intudock.Models.AssignSlotToTransaction.AssignSlotToTransactionModel;
import com.intutrack.intudock.Models.AssignSlotToTransaction.AssignSlotToTransactionRequest;
import com.intutrack.intudock.Models.SlotsData.SlotModel;
import com.intutrack.intudock.Models.SlotsData.SlotsModel;
import com.intutrack.intudock.Models.Transaction.TransactionsModel;
import com.intutrack.intudock.Models.Transaction.TransactionsResult;
import com.intutrack.intudock.Preference.Preferences;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Retrofit.Helper.APIUtility;
import com.intutrack.intudock.UI.Adapters.SelectSlotAdapter;
import com.intutrack.intudock.Utilities.CommonUtils;
import com.intutrack.intudock.Utilities.Constants;

import java.util.ArrayList;

public class SelectTransactionActivity extends AppCompatActivity implements SelectSlotAdapter.SelectSlotAdapterCallback {

    private Spinner lrNumber, docks, currentStatus;
    private EditText transactionId, transporter, date, source, destination, driverNumber, vehicleNumber, eta;
    private TextView slotDuration, transactionType;
    private RecyclerView recyclerViewSlots;
    private Button done, cancel;
    private RelativeLayout relativeLayoutBack;
    private int currentSlotDurationTime, dockChangeFlag;
    private RelativeLayout relativeLayoutPlus, relativeLayoutMinus;
    private ArrayList docksList, transactionList, currentStatusList, transactionIdList;
    private SelectSlotAdapter adapter;
    private ArrayList<SlotsModel> slotList;
    private ArrayList<TransactionsResult> transactionsResults;
    private String comingFrom, selectedTransactionType, selectedTransactionId, selectedTransporter, selectedDate, selectedSource, selectedDestination, selectedDriver,
            slotSize, selectedVehicle, selectedDockName, selectedSlot, selectedDuration, selectedDockId, selectedWarehouseId, selectedStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_transaction);

        lrNumber = findViewById(R.id.spinner_transaction);
        transactionType = findViewById(R.id.tv_transaction_type);
        docks = findViewById(R.id.spinner_docks);
        currentStatus = findViewById(R.id.spinner_current_status);

        transactionId = findViewById(R.id.et_id);
        transporter = findViewById(R.id.et_transporter);
        date = findViewById(R.id.et_date);
        source = findViewById(R.id.et_source);
        destination = findViewById(R.id.et_destiniation);
        driverNumber = findViewById(R.id.et_driver_number);
        vehicleNumber = findViewById(R.id.et_vehicle);
        eta = findViewById(R.id.et_eta);

        slotDuration = findViewById(R.id.tv_slot_duration);

        recyclerViewSlots = findViewById(R.id.rv_time_slots);

        done = findViewById(R.id.btn_done);
        cancel = findViewById(R.id.btn_cancel);

        relativeLayoutPlus = findViewById(R.id.rl_plus);
        relativeLayoutMinus = findViewById(R.id.rl_minus);
        relativeLayoutBack = findViewById(R.id.rl_back);

        docksList = new ArrayList();
        transactionList = new ArrayList();
        transactionIdList = new ArrayList();
        currentStatusList = new ArrayList();
        slotList = new ArrayList();
        transactionsResults = new ArrayList<>();

        selectedStartTime="0";

        dockChangeFlag = 0;

        comingFrom = getIntent().getExtras().getString("from");

        if(comingFrom.equals("slotsAdapter")){

            try {

                selectedDockId = getIntent().getExtras().getString("dockId");
                currentSlotDurationTime = Integer.parseInt(getIntent().getExtras().getString("slotDuration"));
                selectedStartTime = getIntent().getExtras().getString("slotStartTime");
                slotSize = getIntent().getExtras().getString("slotSize");
                selectedDate = getIntent().getExtras().getString("selectedDate");
                selectedWarehouseId = getIntent().getExtras().getString("selectedWarehouseId");

            }catch (Exception e){
                selectedDockId = "";
                currentSlotDurationTime = 30;
                selectedStartTime = "0";
                selectedDate = "";
                selectedWarehouseId = "";
                slotSize="";
            }


            currentStatusList.add("IN TRANSIT");
            currentStatusList.add("AT GATE");
            currentStatusList.add("AT DOCK");

            slotDuration.setText(currentSlotDurationTime + " mins");

        }

        else if(comingFrom.equals("TransactionDetailsActivity")){

            try {
                selectedTransactionType = getIntent().getExtras().getString("transactionType");
                selectedTransactionId = getIntent().getExtras().getString("transactionId");
                selectedTransporter = getIntent().getExtras().getString("transporter");
                selectedDate = getIntent().getExtras().getString("selectedDate");
                selectedSource = getIntent().getExtras().getString("source");
                selectedDestination = getIntent().getExtras().getString("destination");
                selectedDriver = getIntent().getExtras().getString("driver");
                selectedVehicle = getIntent().getExtras().getString("vehicle");
                currentStatusList = getIntent().getParcelableArrayListExtra("currentStatus");
                selectedDockName = getIntent().getExtras().getString("dockName");
                selectedDockId = getIntent().getExtras().getString("dockId");
                selectedSlot = getIntent().getExtras().getString("slot");
                selectedDuration = getIntent().getExtras().getString("slotDuration");
                slotSize = getIntent().getExtras().getString("slotSize");

            }catch (Exception e){
                selectedTransactionType = "";
                selectedTransactionId = "";
                selectedTransporter = "";
                selectedDate = "";
                selectedSource = "";
                selectedDestination = "";
                selectedDriver = "";
                selectedVehicle = "";
                currentStatusList = new ArrayList();
                selectedDockName = "";
                selectedSlot = "";
                selectedDuration = "";
                slotSize="";

            }

            setValues();

        }



        relativeLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        //dock-transaction link
        transactionListData();

        date.setText(selectedDate);

        transactionId.setFocusableInTouchMode(true);
        transporter.setFocusableInTouchMode(true);
        date.setFocusableInTouchMode(false);
        source.setFocusableInTouchMode(true);
        destination.setFocusableInTouchMode(false);
        driverNumber.setFocusableInTouchMode(true);
        vehicleNumber.setFocusableInTouchMode(true);
        eta.setFocusableInTouchMode(true);

        ArrayAdapter<String> spinnerCurrentStatusArrayAdapter = new ArrayAdapter<String>(this,   R.layout.item_spinner_current_status, currentStatusList);
        spinnerCurrentStatusArrayAdapter.setDropDownViewResource(R.layout.item_spinner); // The drop down view
        currentStatus.setAdapter(spinnerCurrentStatusArrayAdapter);

        relativeLayoutPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentSlotDurationTime += Constants.SLOT_DURATION;
                slotDuration.setText(currentSlotDurationTime + " mins");

                selectedStartTime = "0";
                setSlotsData(slotList, "0");

            }
        });

        relativeLayoutMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentSlotDurationTime > 30){
                    currentSlotDurationTime -= Constants.SLOT_DURATION;
                    slotDuration.setText(currentSlotDurationTime + " mins");
                }

                selectedStartTime = "0";
                setSlotsData(slotList, "0");

            }
        });

        for (int i = 0; i < Preferences.loadDocksList(this).size(); i++){

            if( Preferences.loadDocksList(this).get(i).getDockId().equals(selectedDockId)){

                docksList.add(Preferences.loadDocksList(this).get(i).getDockName());

            }
        }

        for (int i = 0; i < Preferences.loadDocksList(this).size(); i++){

            if( !Preferences.loadDocksList(this).get(i).getDockId().equals(selectedDockId)){

                docksList.add(Preferences.loadDocksList(this).get(i).getDockName());

            }

        }

        ArrayAdapter<String> spinnerDocksArrayAdapter = new ArrayAdapter<String>(this,   R.layout.item_spinner_transaction_content, docksList);
        spinnerDocksArrayAdapter.setDropDownViewResource(R.layout.item_spinner_transaction); // The drop down view
        docks.setAdapter(spinnerDocksArrayAdapter);

        docks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if(position != 0){
                    for (int j = 0; j < Preferences.loadDocksList(SelectTransactionActivity.this).size(); j++){

                        if( Preferences.loadDocksList(SelectTransactionActivity.this).get(j).getDockName().equals(docks.getSelectedItem().toString())){

                            slotTimingsData(Preferences.loadDocksList(SelectTransactionActivity.this).get(j).getDockId(), 1);

                        }
                    }
                }else{

                    dockChangeFlag++;
                     if(dockChangeFlag == 1){

                         for (int j = 0; j < Preferences.loadDocksList(SelectTransactionActivity.this).size(); j++){

                             if( Preferences.loadDocksList(SelectTransactionActivity.this).get(j).getDockName().equals(docks.getSelectedItem().toString())){

                                 slotTimingsData(Preferences.loadDocksList(SelectTransactionActivity.this).get(j).getDockId(), 0);

                             }
                         }
                     }else{
                         for (int j = 0; j < Preferences.loadDocksList(SelectTransactionActivity.this).size(); j++){

                             if( Preferences.loadDocksList(SelectTransactionActivity.this).get(j).getDockName().equals(docks.getSelectedItem().toString())){

                                 slotTimingsData(Preferences.loadDocksList(SelectTransactionActivity.this).get(j).getDockId(), 1);

                             }
                         }
                     }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){

                    String currentDockId = "";
                    String currentTransactioId = "";

                    for (int i = 0; i < Preferences.loadDocksList(SelectTransactionActivity.this).size(); i++){

                        if(Preferences.loadDocksList(SelectTransactionActivity.this).get(i).getDockName().
                                equals(docks.getSelectedItem().toString()))
                            currentDockId = Preferences.loadDocksList(SelectTransactionActivity.this).get(i).getDockId();

                    }

                    for (int i = 0; i < transactionsResults.size(); i++){
                        if(transactionsResults.get(i).getLrNumber().equals(lrNumber.getSelectedItem().toString()))
                            currentTransactioId = transactionsResults.get(i).getTransactionId();
                    }

                    if(!selectedStartTime.equals("0"))
                        assignSlotToTransaction(currentTransactioId, currentDockId, Long.parseLong(selectedStartTime), currentSlotDurationTime);
                    else{
                        Toast.makeText(SelectTransactionActivity.this, "Select a slot!", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

    }

    private void setValues() {

        transactionType.setText(selectedTransactionType);
        transactionId.setText(selectedTransactionId);
        transporter.setText(selectedTransporter);
        date.setText(selectedDate);
        source.setText(selectedSource);
        destination.setText(selectedDestination);
        driverNumber.setText(selectedDriver);
        vehicleNumber.setText(selectedVehicle);

    }

    private void assignSlotToTransaction(String transactionId, String dockId, long slotStart, long duration) {

        AssignSlotToTransactionDock dockRequest = new AssignSlotToTransactionDock();
        dockRequest.setDockId(dockId);
        dockRequest.setDuration((duration * 1000) * 60);
        dockRequest.setStart(slotStart);

        AssignSlotToTransactionRequest request = new AssignSlotToTransactionRequest();
        request.setDock(dockRequest);

        IntuApplication.getApiUtility().AssignSlotToTransaction(this, true, transactionId, request, new APIUtility.APIResponseListener<AssignSlotToTransactionModel>() {
            @Override
            public void onReceiveResponse(AssignSlotToTransactionModel response) {

                Toast.makeText(SelectTransactionActivity.this, "Slot booked",Toast.LENGTH_LONG).show();
                finish();

            }
        });

    }

    private void slotTimingsData(String dockId, final int flagDockChange) {

        slotList.clear();

        IntuApplication.getApiUtility().SlotData(this, true, dockId, selectedDate, new APIUtility.APIResponseListener<SlotModel>() {
            @Override
            public void onReceiveResponse(SlotModel response) {

                for(int i = 0; i < response.getResult().get(0).getSlots().size(); i++)
                    slotList.add(response.getResult().get(0).getSlots().get(i));

                if(flagDockChange == 0){
                    setSlotsData(slotList, selectedStartTime);
                }else{
                    setSlotsData(slotList, "0");
                }

            }
        });

    }

    private void setSlotsData(ArrayList<SlotsModel> list, String strtTime) {

        ArrayList selectedSlots = new ArrayList();

        if(!strtTime.equals("0")){

            selectedSlots.add(strtTime);

            for (int i = 0; i < ((currentSlotDurationTime/30) - 1); i++){

                selectedSlots.add(Long.parseLong(strtTime)+ (Long.parseLong(slotSize)*(i+1)));

            }

        }

        adapter = new SelectSlotAdapter(list, SelectTransactionActivity.this, currentSlotDurationTime, selectedSlots);
        recyclerViewSlots.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(SelectTransactionActivity.this,3);
        recyclerViewSlots.setLayoutManager(layoutManager);
        recyclerViewSlots.setAdapter(adapter);

    }

    private void transactionListData() {

        IntuApplication.getApiUtility().TransactionsData(this, true, selectedWarehouseId, new APIUtility.APIResponseListener<TransactionsModel>() {
            @Override
            public void onReceiveResponse(final TransactionsModel response) {

                transactionList.add("Select Transaction");
                transactionIdList.add("Select Transaction");

                for(int i = 0; i < response.getResult().size(); i++){

                    Log.e("SelectTransactionAct","assigned " + response.getResult().get(i).isAssigned());

                    if(!response.getResult().get(i).isAssigned()){
                        if(!response.getResult().get(i).getCurrentStatus().get(0).getStatus().equals("REJECTED")){
                            if(!response.getResult().get(i).getCurrentStatus().get(0).getStatus().equals("COMPLETED")){
                                Log.e("SelectTransactionAct","un assigned " + response.getResult().get(i).getPhoneNumber().get(0));
//                                Log.e("TAG", "current status: " + response.getResult().get(i).getCurrentStatus().get(0).getStatus());
                                transactionList.add(response.getResult().get(i).getLrNumber());
                                transactionIdList.add(response.getResult().get(i).getTransactionId());

                            }
                        }
                    }
                }

                transactionsResults.addAll(response.getResult());

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(SelectTransactionActivity.this,
                        R.layout.item_spinner_transaction_content, transactionList);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner_transaction); // The drop down view
                lrNumber.setAdapter(spinnerArrayAdapter);

                lrNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                        for (int j = 0; j < response.getResult().size(); j++)
                            if(response.getResult().get(j).getTransactionId().equals(transactionIdList.get(position).toString())){

                                transactionType.setText(response.getResult().get(j).getType());
                                transporter.setText(response.getResult().get(j).getTransporterName());
                                source.setText(response.getResult().get(j).getSrcWarehouse().getName());
                                destination.setText(response.getResult().get(j).getDestWarehouse().getName());
                                driverNumber.setText(response.getResult().get(j).getPhoneNumber().get(0).toString());
                                vehicleNumber.setText(response.getResult().get(j).getVehicleNumber());

                            }

                        transactionId.setText(transactionList.get(position).toString());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        });

    }

    private boolean validate() {

        if(lrNumber.getSelectedItem().toString().equals("Select Transaction")){

            CommonUtils.alert(this, "Please select a transaction.");
            return false;

        }else if(transactionId.getText().toString().equals("")){

            CommonUtils.alert(this, "Please enter the transaction ID.");
            return false;

        }else if(transporter.getText().toString().equals("")){

            CommonUtils.alert(this, "Please enter the transporter name.");
            return false;

        }else if(date.getText().toString().equals("")){

            CommonUtils.alert(this, "Please select the date.");
            return false;

        }else if(source.getText().toString().equals("")){

            CommonUtils.alert(this, "Please enter the source.");
            return false;

        }else if(destination.getText().toString().equals("")){

            CommonUtils.alert(this, "Please enter the destination.");
            return false;

        }else if(driverNumber.getText().toString().equals("") || driverNumber.getText().toString().length() != 10){

            CommonUtils.alert(this, "Please enter a valid driver's number.");
            return false;

        }else if(vehicleNumber.getText().toString().equals("")){

            CommonUtils.alert(this, "Please enter the vehicle number.");
            return false;

        }

        return true;
    }

    @Override
    public void onSlotClick(ArrayList selectedList) {

        selectedStartTime = selectedList.get(0).toString();

        adapter = new SelectSlotAdapter(slotList, SelectTransactionActivity.this, currentSlotDurationTime, selectedList);
        recyclerViewSlots.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(SelectTransactionActivity.this,3);
        recyclerViewSlots.setLayoutManager(layoutManager);
        recyclerViewSlots.setAdapter(adapter);

        for(int i = 0; i < slotList.size(); i++){

            if(slotList.get(i).getStart() == Long.parseLong(selectedStartTime)){
                recyclerViewSlots.scrollToPosition(i);
            }

        }

    }
}
