package com.intutrack.intudock.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.intutrack.intudock.Models.AddTransaction.AddTransactionRequest;
import com.intutrack.intudock.Models.AddTransaction.AddTransactionResponse;
import com.intutrack.intudock.Models.AddTransaction.DockRequest;
import com.intutrack.intudock.Models.AddTransaction.WarehouseRequest;
import com.intutrack.intudock.Models.AssignSlotToTransaction.AssignSlotToTransactionDock;
import com.intutrack.intudock.Models.AssignSlotToTransaction.AssignSlotToTransactionModel;
import com.intutrack.intudock.Models.AssignSlotToTransaction.AssignSlotToTransactionRequest;
import com.intutrack.intudock.Models.Docks.DocksModel;
import com.intutrack.intudock.Models.Docks.DocksResult;
import com.intutrack.intudock.Models.EditTransaction.EditTransactionChangeRequest;
import com.intutrack.intudock.Models.EditTransaction.EditTransactionChangeSourceRequest;
import com.intutrack.intudock.Models.EditTransaction.EditTransactionDockRequest;
import com.intutrack.intudock.Models.EditTransaction.EditTransactionRequest;
import com.intutrack.intudock.Models.EditTransaction.EditTransactionResponse;
import com.intutrack.intudock.Models.SlotsData.SlotModel;
import com.intutrack.intudock.Models.SlotsData.SlotsModel;
import com.intutrack.intudock.Models.TransactionStatus.TransactionStatusRequest;
import com.intutrack.intudock.Models.TransactionStatus.TransactionsStatusModel;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Retrofit.Helper.APIUtility;
import com.intutrack.intudock.UI.Adapters.SelectSlotAdapter;
import com.intutrack.intudock.Utilities.CommonUtils;
import com.intutrack.intudock.Utilities.Constants;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionActivity extends AppCompatActivity implements SelectSlotAdapter.SelectSlotAdapterCallback {

    private EditText transactionId, transporter, date, source, destination, driverNumber, vehicleNumber, eta;
    private Spinner transactionType, currentStatus, docks;
    private TextView slotDuration, title, dateFilter;
    private RelativeLayout relativeLayoutPlus, relativeLayoutMinus,relativeLayoutBack;
    private RecyclerView recyclerViewSlots;
    private Button done, cancel;
    private ArrayList currentStatusList, transactionTypeList;
    private SelectSlotAdapter adapter;
    private ArrayList<SlotsModel> slotList;
    private ArrayList<DocksResult> docksList;
    private String selectedDate, comingFrom, selectedWarehouseId, selectedWarehouseName, selectedSlot, selectedDockName;
    private int currentSlotDurationTime;
    private long selectedTimeSlot;
    private int flagDateClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        transactionType = findViewById(R.id.spinner_transaction_type);
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
        title = findViewById(R.id.tv_title);
        dateFilter = findViewById(R.id.tv_date_filter);

        recyclerViewSlots = findViewById(R.id.rv_time_slots);

        done = findViewById(R.id.btn_done);
        cancel = findViewById(R.id.btn_cancel);

        relativeLayoutPlus = findViewById(R.id.rl_plus);
        relativeLayoutMinus = findViewById(R.id.rl_minus);

        relativeLayoutBack = findViewById(R.id.rl_back);

        currentStatusList = new ArrayList();
        slotList = new ArrayList();
        docksList = new ArrayList();
        transactionTypeList = new ArrayList();

        selectedTimeSlot = 0;
        flagDateClick = 0;

        selectedDate = getIntent().getExtras().getString("selectedDate");
        comingFrom = getIntent().getExtras().getString("from");
        selectedWarehouseId = getIntent().getExtras().getString("warehouseId");
        selectedWarehouseName = getIntent().getExtras().getString("warehouseName");

        if(comingFrom.equals("ViewTransactionsFragment")){
            handleNewTransaction();
        }
        else if(comingFrom.equals("TransactionDetailsActivity")){
            title.setText("Reschedule Transaction");
            selectedDockName = getIntent().getExtras().getString("dockName");
            handleTransactionsDetails();
        }
        else if(comingFrom.equals("TransactionsAdapterSchedule")){
            title.setText("Schedule Transaction");
            handleTransactionsDetails();
        }
        else if(comingFrom.equals("TransactionsAdapterEdit")){
            try{
                selectedDockName = getIntent().getExtras().getString("dockName");
                Log.e("TAG", selectedDockName);
            }catch (Exception e){
                selectedDockName = "";
            }

            title.setText("Edit Transaction");
            handleTransactionsDetails();
        }

        dateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarDialog();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

    }

    private void handleNewTransaction() {

        loadDocksData(selectedWarehouseId, selectedDate);

        relativeLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        dateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarDialog();
            }
        });

        transactionId.setFocusableInTouchMode(true);
        transporter.setFocusableInTouchMode(true);
        date.setFocusableInTouchMode(false);
        source.setFocusableInTouchMode(true);
        destination.setFocusableInTouchMode(false);
        driverNumber.setFocusableInTouchMode(true);
        vehicleNumber.setFocusableInTouchMode(true);
        eta.setFocusableInTouchMode(true);

        Calendar tempCal = Calendar.getInstance();
        String todayDate = tempCal.get(Calendar.YEAR) + "-" + CommonUtils.convertMonth((tempCal.get(Calendar.MONTH)+1)) + "-" +
                CommonUtils.convertMonth(tempCal.get(Calendar.DATE));

        date.setText(todayDate);
        dateFilter.setText(selectedDate);

        transactionTypeList.add("LOAD");
        transactionTypeList.add("UNLOAD");

        ArrayAdapter<String> spinnerTransactionTypeArrayAdapter = new ArrayAdapter<String>(this,   R.layout.item_spinner_current_status, transactionTypeList);
        spinnerTransactionTypeArrayAdapter.setDropDownViewResource(R.layout.item_spinner); // The drop down view
        transactionType.setAdapter(spinnerTransactionTypeArrayAdapter);

        currentStatusList.add("IN TRANSIT");
        currentStatusList.add("AT GATE");
        currentStatusList.add("AT DOCK");

        ArrayAdapter<String> spinnerCurrentStatusArrayAdapter = new ArrayAdapter<String>(this,   R.layout.item_spinner_current_status, currentStatusList);
        spinnerCurrentStatusArrayAdapter.setDropDownViewResource(R.layout.item_spinner); // The drop down view
        currentStatus.setAdapter(spinnerCurrentStatusArrayAdapter);

        currentSlotDurationTime = Constants.SLOT_DURATION;

        relativeLayoutPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentSlotDurationTime += Constants.SLOT_DURATION;
                slotDuration.setText(currentSlotDurationTime + " mins");

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

                setSlotsData(slotList,"0");

            }
        });

        destination.setText(selectedWarehouseName);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){

                    WarehouseRequest srcRequest = new WarehouseRequest();
                    WarehouseRequest destRequest = new WarehouseRequest();

                    srcRequest.setId(null);
                    srcRequest.setName(source.getText().toString());

                    destRequest.setId(selectedWarehouseId);
                    destRequest.setName(selectedWarehouseName);

                    ArrayList phoneNumber = new ArrayList();
                    phoneNumber.add(driverNumber.getText().toString());

                    DockRequest dockRequest = new DockRequest();

                    for (int i = 0; i < docksList.size(); i++){

                        if(docks.getSelectedItem().toString().equals(docksList.get(i).getDockName())){
                            dockRequest.setId(docksList.get(i).getDockId());
                            dockRequest.setStart(selectedTimeSlot);
                        }

                        dockRequest.setDuration((currentSlotDurationTime * 1000) * 60);

                    }


                    AddTransactionRequest request = new AddTransactionRequest();

                    request.setLrNumber(transactionId.getText().toString());
                    request.setVehicleNumber(vehicleNumber.getText().toString());
                    request.setPhoneNumber(phoneNumber);
                    request.setType(transactionType.getSelectedItem().toString());
                    request.setStatus(currentStatus.getSelectedItem().toString());
                    request.setTransporter(transporter.getText().toString());
                    request.setSrcWarehouse(srcRequest);
                    request.setDestWarehouse(destRequest);
                    request.setDock(dockRequest);

                    if(selectedTimeSlot == 0){
                        if(currentStatus.getSelectedItem().toString().equals("IN TRANSIT") ||
                                currentStatus.getSelectedItem().toString().equals("AT GATE"))
                            newTransactionApi(request);
                        else{
                            CommonUtils.alert(AddTransactionActivity.this, "Current Status must be IN TRANSIT or AT GATE");
                        }

                    }else{
                        newTransactionApi(request);
                    }

                }

            }
        });

    }

    private void handleTransactionsDetails() {

        final String selectedTransactionType, selectedLrNumber, selectedTransactionId, selectedTransporter, selectedDate, selectedSource, selectedDestination, selectedDriver,
                selectedVehicle, selectedWarehouseName, selectedDuration, selectedCurrentStatus, selectedCurrentStatusTime;


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
//        selectedDockName = getIntent().getExtras().getString("dockName");
//        selectedDockId = getIntent().getExtras().getString("dockId");
//        selectedSlot = getIntent().getExtras().getString("slot");
//        selectedDuration = getIntent().getExtras().getString("slotDuration");

        loadDocksData(selectedWarehouseId, selectedDate);

        relativeLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        if(!comingFrom.equals("TransactionsAdapterEdit")){
            if(selectedTransactionType.equals("LOAD")){

                transactionTypeList.add("LOAD");

            }
            else{

                transactionTypeList.add("UNLOAD");

            }
        }else{
            if(selectedTransactionType.equals("LOAD")){

                transactionTypeList.add("LOAD");
                transactionTypeList.add("UNLOAD");

            }
            else{

                transactionTypeList.add("UNLOAD");
                transactionTypeList.add("LOAD");

            }
        }

        ArrayAdapter<String> spinnerTransactionTypeArrayAdapter = new ArrayAdapter<String>(this,   R.layout.item_spinner_current_status, transactionTypeList);
        spinnerTransactionTypeArrayAdapter.setDropDownViewResource(R.layout.item_spinner); // The drop down view
        transactionType.setAdapter(spinnerTransactionTypeArrayAdapter);

        if( comingFrom.equals("TransactionDetailsActivity")  || comingFrom.equals("TransactionsAdapterSchedule") ){

            transactionId.setFocusableInTouchMode(false);
            transporter.setFocusableInTouchMode(false);
            date.setFocusableInTouchMode(false);
            source.setFocusableInTouchMode(false);
            destination.setFocusableInTouchMode(false);
            driverNumber.setFocusableInTouchMode(false);
            vehicleNumber.setFocusableInTouchMode(false);
            eta.setFocusableInTouchMode(false);

            dateFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    flagDateClick = 0;
                    calendarDialog();
                }
            });

        }
        else if(comingFrom.equals("TransactionsAdapterEdit")){

            transactionId.setFocusableInTouchMode(true);
            transporter.setFocusableInTouchMode(true);
            source.setFocusableInTouchMode(true);
            destination.setFocusableInTouchMode(false);
            date.setFocusableInTouchMode(false);
            driverNumber.setFocusableInTouchMode(true);
            vehicleNumber.setFocusableInTouchMode(true);
            eta.setFocusableInTouchMode(true);

            dateFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    flagDateClick = 1;
                    calendarDialog();
                }
            });
        }

        Calendar tempCal = Calendar.getInstance();
        String todayDate = tempCal.get(Calendar.YEAR) + "-" + CommonUtils.convertMonth((tempCal.get(Calendar.MONTH)+1)) + "-" +
                CommonUtils.convertMonth(tempCal.get(Calendar.DATE));

        transactionId.setText(selectedLrNumber);
        transporter.setText(selectedTransporter);
        date.setText(todayDate);
        dateFilter.setText(selectedDate);
        source.setText(selectedSource);
        destination.setText(selectedDestination);
        driverNumber.setText(selectedDriver);
        vehicleNumber.setText(selectedVehicle);

        currentStatusList = new ArrayList();

        if(selectedCurrentStatus.equals("IN TRANSIT")){
            currentStatusList.add("IN TRANSIT");
//            currentStatusList.add("AT GATE");
//            currentStatusList.add("AT DOCK");
        }
        else if(selectedCurrentStatus.equals("AT GATE")){
            currentStatusList.add("AT GATE");
//            currentStatusList.add("AT DOCK");
        }
        else if(selectedCurrentStatus.equals("AT DOCK")){
            currentStatusList.add("AT DOCK");
        }
        else if(selectedCurrentStatus.equals("COMPLETED")){
            currentStatusList.add("COMPLETED");
        }

        ArrayAdapter<String> spinnerCurrentStatusArrayAdapter = new ArrayAdapter<String>(this,   R.layout.item_spinner_current_status, currentStatusList);
        spinnerCurrentStatusArrayAdapter.setDropDownViewResource(R.layout.item_spinner); // The drop down view
        currentStatus.setAdapter(spinnerCurrentStatusArrayAdapter);

        currentSlotDurationTime = 30;

        relativeLayoutPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentSlotDurationTime += Constants.SLOT_DURATION;
                slotDuration.setText(currentSlotDurationTime + " mins");

                setSlotsData(slotList,"0");

            }
        });

        relativeLayoutMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentSlotDurationTime > 30){
                    currentSlotDurationTime -= Constants.SLOT_DURATION;
                    slotDuration.setText(currentSlotDurationTime + " mins");
                }

                setSlotsData(slotList,"0");

            }
        });

        destination.setText(selectedWarehouseName);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Log.e("AddTransAct", "Time slot: " + selectedTimeSlot);

                if(validate()){

                    if( comingFrom.equals("TransactionDetailsActivity") || comingFrom.equals("TransactionsAdapterSchedule")){

                        String dockId = "";

                        for(int i = 0; i < docksList.size(); i++){

                            if(docks.getSelectedItem().toString().equalsIgnoreCase(docksList.get(i).getDockName()))
                                dockId = docksList.get(i).getDockId();

                        }

                        assignSlotToTransaction(selectedTransactionId, dockId, selectedTimeSlot, currentSlotDurationTime);

                    }
                    else if(comingFrom.equals("TransactionsAdapterEdit")){

                        transactionId.setFocusableInTouchMode(true);
                        transporter.setFocusableInTouchMode(true);
                        source.setFocusableInTouchMode(true);
                        destination.setFocusableInTouchMode(false);
                        driverNumber.setFocusableInTouchMode(true);
                        vehicleNumber.setFocusableInTouchMode(true);
                        eta.setFocusableInTouchMode(true);

                        ArrayList phoneNumber = new ArrayList();
                        phoneNumber.add(driverNumber.getText().toString());

                        EditTransactionChangeRequest changeRequest = new EditTransactionChangeRequest();

                        if(selectedTimeSlot == 0){

                            EditTransactionChangeSourceRequest sourceChange = new EditTransactionChangeSourceRequest();
                            sourceChange.setName(source.getText().toString());

//                            sourceChange.setId(null);

                            changeRequest.setLrNumber(transactionId.getText().toString());
                            changeRequest.setVehicleNumber(vehicleNumber.getText().toString());
                            changeRequest.setPhoneNumber(phoneNumber);
                            changeRequest.setSrcWarehouse(sourceChange);
                            changeRequest.setType(transactionType.getSelectedItem().toString());
                            changeRequest.setStatus(currentStatus.getSelectedItem().toString());
                            changeRequest.setTransporter(transporter.getText().toString());

                        }else{

                            EditTransactionDockRequest dockRequest = new EditTransactionDockRequest();

                            for (int i = 0; i < docksList.size(); i++){

                                if(docks.getSelectedItem().toString().equals(docksList.get(i).getDockName())){
                                    dockRequest.setId(docksList.get(i).getDockId());
                                    dockRequest.setStart(selectedTimeSlot);
                                }

                                dockRequest.setDuration((currentSlotDurationTime * 1000) * 60);

                            }

                            EditTransactionChangeSourceRequest sourceChange = new EditTransactionChangeSourceRequest();
                            sourceChange.setName(source.getText().toString());


                            changeRequest.setLrNumber(transactionId.getText().toString());
                            changeRequest.setVehicleNumber(vehicleNumber.getText().toString());
                            changeRequest.setPhoneNumber(phoneNumber);
                            changeRequest.setSrcWarehouse(sourceChange);
                            changeRequest.setType(transactionType.getSelectedItem().toString());
                            changeRequest.setStatus(currentStatus.getSelectedItem().toString());
                            changeRequest.setTransporter(transporter.getText().toString());
                            changeRequest.setDock(dockRequest);
                        }

                        EditTransactionRequest request = new EditTransactionRequest();

                        request.setTransactionId(selectedTransactionId);
                        request.setChange(changeRequest);

                        if(selectedTimeSlot == 0){
                            if(currentStatus.getSelectedItem().toString().equals("IN TRANSIT") ||
                                    currentStatus.getSelectedItem().toString().equals("AT GATE"))
                                editTransactionApi(request);
                            else{
                                CommonUtils.alert(AddTransactionActivity.this, "Current Status must be IN TRANSIT or AT GATE");
                            }

                        }else{
                            editTransactionApi(request);
                        }

                    }

                }

            }
        });

    }

    private void loadDocksData(String selectedWarehouseId, String selectedStartDate) {

        docksList.clear();
        docksList = new ArrayList<>();

        IntuApplication.getApiUtility().DockData(AddTransactionActivity.this, true, selectedWarehouseId, selectedStartDate, new APIUtility.APIResponseListener<DocksModel>() {
            @Override
            public void onReceiveResponse(DocksModel response) {

                for(int i = 0; i< response.getResult().size(); i++){
                    docksList.add(response.getResult().get(i));
                }

                ArrayList docksName = new ArrayList();

                if(comingFrom.equals("TransactionDetailsActivity")){

                    try{
                        docksName.add(selectedDockName);
                    }catch (Exception e){

                    }

                    for(int i = 0; i< response.getResult().size(); i++){

                        if(!response.getResult().get(i).getDockName().equals(selectedDockName))
                            docksName.add(response.getResult().get(i).getDockName());

                    }
                }else if(comingFrom.equals("TransactionsAdapterEdit")){

                    try{

                        if(selectedDockName.equals("")){
                            for(int i = 0; i< response.getResult().size(); i++){
                                docksName.add(response.getResult().get(i).getDockName());
                            }
                        }else{

                            docksName.add(selectedDockName);
                            for(int i = 0; i< response.getResult().size(); i++){

                                if(!response.getResult().get(i).getDockName().equals(selectedDockName))
                                    docksName.add(response.getResult().get(i).getDockName());

                            }

                        }
                    }catch (Exception e){
                        for(int i = 0; i< response.getResult().size(); i++){
                            docksName.add(response.getResult().get(i).getDockName());
                        }
                    }

                }
                else{
                    for(int i = 0; i< response.getResult().size(); i++){
                        docksName.add(response.getResult().get(i).getDockName());
                    }

                }

                ArrayAdapter<String> spinnerDocksArrayAdapter = new ArrayAdapter<String>(AddTransactionActivity.this,   R.layout.item_spinner_transaction_content, docksName);
                spinnerDocksArrayAdapter.setDropDownViewResource(R.layout.item_spinner_transaction); // The drop down view
                docks.setAdapter(spinnerDocksArrayAdapter);

                docks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                        for(int i = 0; i < docksList.size(); i++){
                            if(docksList.get(i).getDockName().equals(docks.getSelectedItem().toString()))
                                slotTimingsData(docksList.get(i).getDockId());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        });

    }

    private void slotTimingsData(String dockId) {

        slotList.clear();

        IntuApplication.getApiUtility().SlotData(this, true, dockId, dateFilter.getText().toString(), new APIUtility.APIResponseListener<SlotModel>() {
            @Override
            public void onReceiveResponse(SlotModel response) {

                for(int i = 0; i < response.getResult().get(0).getSlots().size(); i++)
                    slotList.add(response.getResult().get(0).getSlots().get(i));

                if(comingFrom.equals("ViewTransactionsFragment") || comingFrom.equals("TransactionsAdapterSchedule") ||
                        comingFrom.equals("TransactionsAdapterEdit")){

                    if(flagDateClick == 1){
                        setSlotsData(slotList,selectedSlot);
                    }else{
                        setSlotsData(slotList,"0");
                    }
                    setSlotsData(slotList,"0");

                }
                else
                    setSlotsData(slotList,selectedSlot);

            }
        });

    }

    private void setSlotsData(ArrayList<SlotsModel> list, String strtTime) {

        ArrayList selectedSlots = new ArrayList();

        try {
            if(!strtTime.equals("0")){

                selectedSlots.add(strtTime);

            }
        }catch (Exception e) {
        }

        adapter = new SelectSlotAdapter(list, AddTransactionActivity.this, currentSlotDurationTime, selectedSlots);
        recyclerViewSlots.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(AddTransactionActivity.this,3);
        recyclerViewSlots.setLayoutManager(layoutManager);
        recyclerViewSlots.setAdapter(adapter);

    }


    private void newTransactionApi(AddTransactionRequest request) {

        IntuApplication.getApiUtility().AddTransaction(this, true, request, new APIUtility.APIResponseListener<AddTransactionResponse>() {
            @Override
            public void onReceiveResponse(AddTransactionResponse response) {

                String message;

                try{
                    if(response.getResult().get(0).getDock().getMessage().isEmpty()){

                        message = "";

                    }else{

                        message = response.getResult().get(0).getDock().getMessage();

                    }
                }catch(NullPointerException exception){
                    message = "";
                }

                Toast.makeText(AddTransactionActivity.this, "Transaction added. \n " + message, Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    private void editTransactionApi(EditTransactionRequest request) {

        IntuApplication.getApiUtility().EditTransaction(this, true, request, new APIUtility.APIResponseListener<EditTransactionResponse>() {
            @Override
            public void onReceiveResponse(EditTransactionResponse response) {

                String message;

                try{
                    if(response.getResult().get(0).getDock().getMessage().isEmpty()){

                        message = "";

                    }else{

                        message = response.getResult().get(0).getDock().getMessage();

                    }
                }catch(NullPointerException exception){
                    message = "";
                }

                Toast.makeText(AddTransactionActivity.this, "TRANSACTION EDITED " + message, Toast.LENGTH_LONG).show();
                finish();

            }
        });

    }

    private void assignSlotToTransaction(final String transactionId, String dockId, long slotStart, long duration) {

        AssignSlotToTransactionDock dockRequest = new AssignSlotToTransactionDock();
        dockRequest.setDockId(dockId);
        dockRequest.setDuration((duration * 1000) * 60);
        dockRequest.setStart(slotStart);

        AssignSlotToTransactionRequest request = new AssignSlotToTransactionRequest();
        request.setDock(dockRequest);

        IntuApplication.getApiUtility().AssignSlotToTransaction(this, true, transactionId, request, new APIUtility.APIResponseListener<AssignSlotToTransactionModel>() {
            @Override
            public void onReceiveResponse(AssignSlotToTransactionModel response) {

                if(currentStatus.getSelectedItemPosition() != 0){

                    changeCurrentStatus(transactionId);

                }else{

                    finish();

                }

            }
        });

    }

    private void changeCurrentStatus(String transId) {

        TransactionStatusRequest request = new TransactionStatusRequest();

        request.setTransactionId(transId);
        request.setNewStatus(currentStatus.getSelectedItem().toString());

        IntuApplication.getApiUtility().TransactionStatusChange(AddTransactionActivity.this, true, request, new APIUtility.APIResponseListener<TransactionsStatusModel>() {
            @Override
            public void onReceiveResponse(TransactionsStatusModel response) {

               finish();

            }
        });

    }

    public void calendarDialog(){

        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {

                dateFilter.setText(year + "-" + CommonUtils.convertMonth(((monthOfYear)+1)) + "-" + CommonUtils.convertDay(dayOfMonth));
                loadDocksData(selectedWarehouseId,
                        year + "-" + CommonUtils.convertMonth(((monthOfYear)+1)) + "-" + CommonUtils.convertDay(dayOfMonth));

            }};

        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);

        DatePickerDialog dpDialog=new DatePickerDialog(this, R.style.DialogTheme, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        dpDialog.show();

    }

    private boolean validate() {

        if(transactionId.getText().toString().equals("")){

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
    public void onSlotClick(ArrayList selectedSlots) {

        selectedTimeSlot = Long.parseLong(selectedSlots.get(0).toString());

        adapter = new SelectSlotAdapter(slotList, AddTransactionActivity.this, currentSlotDurationTime, selectedSlots);
        recyclerViewSlots.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(AddTransactionActivity.this,3);
        recyclerViewSlots.setLayoutManager(layoutManager);
        recyclerViewSlots.setAdapter(adapter);

        for(int i = 0; i < slotList.size(); i++){

            if(slotList.get(i).getStart() == selectedTimeSlot){
                recyclerViewSlots.scrollToPosition(i);
            }

        }

    }
}
