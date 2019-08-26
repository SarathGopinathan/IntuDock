package com.intutrack.intudock.UI.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.intutrack.intudock.Models.Docks.DocksModel;
import com.intutrack.intudock.Models.Transaction.TransactionsResult;
import com.intutrack.intudock.Preference.Preferences;
import com.intutrack.intudock.Retrofit.Helper.APIUtility;
import com.intutrack.intudock.UI.Adapters.TransactionsAdapter;
import com.intutrack.intudock.Models.Transaction.TransactionsModel;
import com.intutrack.intudock.R;
import com.intutrack.intudock.UI.Adapters.WarehouseAdapter;
import com.intutrack.intudock.UI.AddTransactionActivity;
import com.intutrack.intudock.UI.IntuApplication;
import com.intutrack.intudock.Utilities.CommonUtils;

import java.util.ArrayList;
import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class ViewTransactionsFragment extends Fragment implements TransactionsAdapter.TransactionsAdapterCallback {
    private ImageButton imageButtonCalendar;
    private ImageView noData;
    private TextView all, assigned, unassigned;
    private HorizontalCalendar horizontalCalendar;
    private TransactionsAdapter adapter;
    private RecyclerView recyclerViewTransactions;
    private Calendar cal;
    private int day, month, year;
    private Spinner spinnerCity, spinnerWarehouse;
    private ArrayList<TransactionsResult> transactionsList;
    private FloatingActionButton addTransaction;
    private SwipeRefreshLayout swipeRefreshLayoutTransactions;
    private ArrayList citiesList, warehouses;
    private String selectedCity, selectedWarehouse, selectedWarehouseId;
    private EditText search;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_view_transactions, container, false);

        recyclerViewTransactions = rootView.findViewById(R.id.rv_transactions);

        imageButtonCalendar = rootView.findViewById(R.id.ib_calendar);
        noData = rootView.findViewById(R.id.iv_no_data);

        all = rootView.findViewById(R.id.tv_all);
        assigned = rootView.findViewById(R.id.tv_assigned);
        unassigned = rootView.findViewById(R.id.tv_unassigned);

        spinnerCity = rootView.findViewById(R.id.spinner_city);
        spinnerWarehouse = rootView.findViewById(R.id.spinner_warehouse);

        addTransaction = rootView.findViewById(R.id.fab_add_transaction);

        swipeRefreshLayoutTransactions = rootView.findViewById(R.id.swipe_refresh_transactions);

        search = rootView.findViewById(R.id.et_search);

        transactionsList = new ArrayList<>();

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        selectedCity = getActivity().getIntent().getExtras().get("city").toString();
        selectedWarehouse = getActivity().getIntent().getExtras().get("warehouseName").toString();
        selectedWarehouseId = getActivity().getIntent().getExtras().get("warehouseId").toString();
        final String selectedDay = getActivity().getIntent().getExtras().get("day").toString();
        final String selectedMonth = getActivity().getIntent().getExtras().get("month").toString();
        final String selectedYear = getActivity().getIntent().getExtras().get("year").toString();

        citiesList = new ArrayList();

        warehouses = new ArrayList();

        citiesList.add(selectedCity);

        for (int i = 0; i < Preferences.loadWarehouseList(getContext()).get(Preferences.loadWarehouseList(getContext()).size()-1).getCities().size(); i++){

            if( !Preferences.loadWarehouseList(getContext()).get(Preferences.loadWarehouseList(getContext()).size()-1).getCities().get(i)
                    .equals(selectedCity)){

                citiesList.add(Preferences.loadWarehouseList(getContext()).get(Preferences.loadWarehouseList(getContext()).size()-1).getCities().get(i));

            }
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),   R.layout.item_spinner_content, citiesList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner); // The drop down view
        spinnerCity.setAdapter(spinnerArrayAdapter);

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                warehouses.clear();

                if(spinnerCity.getSelectedItem().toString().equals(selectedCity)){

                    warehouses.add(selectedWarehouse);

                    for (int i = 0; i < Preferences.loadWarehouseList(getContext()).size()-1; i++){

                        if( (Preferences.loadWarehouseList(getContext()).get(i).getCity()).equals(selectedCity) &&
                                !(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName()).equals(selectedWarehouse)){

                            warehouses.add( Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName());

                        }

                    }

                    ArrayAdapter<String> spinnerWarehouseArrayAdapter = new ArrayAdapter<String>(getContext(),   R.layout.item_spinner_warehouse_content, warehouses);
                    spinnerWarehouseArrayAdapter.setDropDownViewResource(R.layout.item_spinner_warehouse); // The drop down view
                    spinnerWarehouse.setAdapter(spinnerWarehouseArrayAdapter);

                }else{

                    for (int i = 0; i < Preferences.loadWarehouseList(getContext()).size()-1; i++){

                        if( (Preferences.loadWarehouseList(getContext()).get(i).getCity()).equals(spinnerCity.getSelectedItem().toString())){

                            warehouses.add( Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName());

                        }

                    }

                    ArrayAdapter<String> spinnerWarehouseArrayAdapter = new ArrayAdapter<String>(getContext(),   R.layout.item_spinner_warehouse_content, warehouses);
                    spinnerWarehouseArrayAdapter.setDropDownViewResource(R.layout.item_spinner_warehouse); // The drop down view
                    spinnerWarehouse.setAdapter(spinnerWarehouseArrayAdapter);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){

                    if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())){

                        transactionsList.clear();
                        loadTransactionData(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId());

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 5);

        horizontalCalendar = new HorizontalCalendar.Builder(rootView, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .showBottomText(false)
                .sizeTopText(10)
                .sizeMiddleText(15)
                .selectedDateBackground(getResources().getDrawable(R.drawable.rounded_rect_powder_blue))
                .end()
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //do something
            }
        });

        Calendar selectedDateBegining = Calendar.getInstance();
        selectedDateBegining.set(Calendar.MONTH, Integer.valueOf(selectedMonth));
        selectedDateBegining.set(Calendar.DAY_OF_MONTH, Integer.valueOf(selectedDay));
        selectedDateBegining.set(Calendar.YEAR, Integer.valueOf(selectedYear));

        Calendar selectedDateEnd = Calendar.getInstance();
        selectedDateEnd.set(Calendar.MONTH, Integer.valueOf(selectedMonth));
        selectedDateEnd.set(Calendar.DAY_OF_MONTH, Integer.valueOf(selectedDay));
        selectedDateEnd.set(Calendar.YEAR, Integer.valueOf(selectedYear));

        Calendar selectedStartDate = selectedDateBegining;
        selectedStartDate.add(Calendar.DATE, 0);

        Calendar selectedEndDate = selectedDateEnd;
        selectedEndDate.add(Calendar.DATE, 5);

        setUpHorizontalCalendar(selectedStartDate, selectedEndDate, 0);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                all.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_dark_orange));
                assigned.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_light_orange));
                unassigned.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_light_orange));

                all.setTextColor(getResources().getColor(R.color.white));
                assigned.setTextColor(getResources().getColor(R.color.dark_orange));
                unassigned.setTextColor(getResources().getColor(R.color.dark_orange));

                setTransactionList(transactionsList);


            }
        });

        assigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assigned.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_dark_orange));
                all.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_light_orange));
                unassigned.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_light_orange));

                assigned.setTextColor(getResources().getColor(R.color.white));
                all.setTextColor(getResources().getColor(R.color.dark_orange));
                unassigned.setTextColor(getResources().getColor(R.color.dark_orange));

                ArrayList assignedTransactions = new ArrayList();

                for(int i = 0; i < transactionsList.size(); i++)
                    if(transactionsList.get(i).isAssigned()){
                        assignedTransactions.add(transactionsList.get(i));
                    }

                setTransactionList(assignedTransactions);

            }
        });

        unassigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                unassigned.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_dark_orange));
                all.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_light_orange));
                assigned.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_light_orange));

                unassigned.setTextColor(getResources().getColor(R.color.white));
                all.setTextColor(getResources().getColor(R.color.dark_orange));
                assigned.setTextColor(getResources().getColor(R.color.dark_orange));

                ArrayList unAssignedTransactions = new ArrayList();

                for(int i = 0; i < transactionsList.size(); i++)
                    if(!transactionsList.get(i).isAssigned()){
                        unAssignedTransactions.add(transactionsList.get(i));
                    }

                setTransactionList(unAssignedTransactions);

            }
        });

        imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendarDialog();
            }
        });

        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tempDate = horizontalCalendar.getSelectedDate().get(Calendar.YEAR) + "-" +
                        CommonUtils.convertMonth((horizontalCalendar.getSelectedDate().get(Calendar.MONTH)+1)) + "-" +
                        CommonUtils.convertDay((horizontalCalendar.getSelectedDate().get(Calendar.DATE)));

                String tempWarehouseId = "";

                for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){

                    if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())){

                        tempWarehouseId = Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId();

                    }

                }

                IntuApplication.getApiUtility().DockData(getContext(), true, tempWarehouseId, tempDate, new APIUtility.APIResponseListener<DocksModel>() {
                    @Override
                    public void onReceiveResponse(DocksModel response) {

                        if(response.getResult().size() > 0){

                            String month, date ;

                            month = CommonUtils.convertMonth((horizontalCalendar.getSelectedDate().get(Calendar.MONTH)) + 1);
                            date = CommonUtils.convertMonth(horizontalCalendar.getSelectedDate().get(Calendar.DATE));

                            for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){

                                if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())){

                                    Intent intent = new Intent(getContext(), AddTransactionActivity.class);
                                    intent.putExtra("from","ViewTransactionsFragment");
                                    intent.putExtra("city",spinnerCity.getSelectedItem().toString());
                                    intent.putExtra("warehouseId", Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId());
                                    intent.putExtra("warehouseName", Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName());
                                    intent.putExtra("selectedDate", horizontalCalendar.getSelectedDate().get(Calendar.YEAR) + "-" + month + "-" + date);

                                    startActivity(intent);

                                }

                            }

                        }else{
                            Toast.makeText(getContext(), "No docks available",Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });

        swipeRefreshLayoutTransactions.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                search.setText("");

                for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){

                    if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())){

                        transactionsList.clear();
                        loadTransactionData(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId());

                    }

                }

                if(swipeRefreshLayoutTransactions != null)
                    swipeRefreshLayoutTransactions.setRefreshing(false);

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void loadTransactionData(String selectedWarehouseId) {

        IntuApplication.getApiUtility().TransactionsData(getContext(), true, selectedWarehouseId, new APIUtility.APIResponseListener<TransactionsModel>() {
            @Override
            public void onReceiveResponse(final TransactionsModel response) {

                Log.e("TAG", "API CALL");

                transactionsList.clear();

                all.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_dark_orange));
                assigned.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_light_orange));
                unassigned.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_light_orange));

                all.setTextColor(getResources().getColor(R.color.white));
                assigned.setTextColor(getResources().getColor(R.color.dark_orange));
                unassigned.setTextColor(getResources().getColor(R.color.dark_orange));

                for(int i = 0; i< response.getResult().size(); i++){
                    transactionsList.add(response.getResult().get(i));
                }

                Preferences.storeTransactionsList(getContext(), transactionsList);

                setTransactionList(transactionsList);

                search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        transactionsList.clear();
                        for(int j = 0; j < response.getResult().size(); j++ ){
                            if(response.getResult().get(j).getLrNumber().contains(charSequence) ||
                                    response.getResult().get(j).getVehicleNumber().contains(charSequence) ||
                                    response.getResult().get(j).getCurrentStatus().get(0).getStatus().contains(charSequence))
                                transactionsList.add(response.getResult().get(j));
                        }

                        if(transactionsList.size() == 0){

                            recyclerViewTransactions.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);

                        }
                        else{

                            recyclerViewTransactions.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);

                            adapter = new TransactionsAdapter( transactionsList, getContext(), ViewTransactionsFragment.this,
                                    horizontalCalendar.getSelectedDate().get(Calendar.YEAR) + "-" +
                                    CommonUtils.convertMonth((horizontalCalendar.getSelectedDate().get(Calendar.MONTH))+1) + "-" +
                                    CommonUtils.convertDay(horizontalCalendar.getSelectedDate().get(Calendar.DATE)));
                            recyclerViewTransactions.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            recyclerViewTransactions.setLayoutManager(layoutManager);
                            recyclerViewTransactions.setAdapter(adapter);

                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

            }
        });

    }

    public void calendarDialog(){

        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {

//                tvDate.setText(dayOfMonth + "/" +(monthOfYear + 1) + "/" + year);

                Calendar selectedDateBegining = Calendar.getInstance();
                selectedDateBegining.set(Calendar.MONTH,view.getMonth());
                selectedDateBegining.set(Calendar.DAY_OF_MONTH,view.getDayOfMonth());
                selectedDateBegining.set(Calendar.YEAR,view.getYear());

                Calendar selectedDateEnd = Calendar.getInstance();
                selectedDateEnd.set(Calendar.MONTH,view.getMonth());
                selectedDateEnd.set(Calendar.DAY_OF_MONTH,view.getDayOfMonth());
                selectedDateEnd.set(Calendar.YEAR,view.getYear());

                Calendar startDate = selectedDateBegining;
                startDate.add(Calendar.DATE, 0);

                Calendar endDate = selectedDateEnd;
                endDate.add(Calendar.DATE, 5);

                setUpHorizontalCalendar(startDate, endDate, 1);

            }};

        DatePickerDialog dpDialog=new DatePickerDialog(getContext(), R.style.DialogTheme, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        dpDialog.show();

    }

    private void setUpHorizontalCalendar(Calendar startDate, Calendar endDate, int flag) {

        Log.e("ViewDocksFrag", "Trans date: " + startDate.get(Calendar.DATE));

        horizontalCalendar.setRange(startDate, endDate);
        horizontalCalendar.refresh();
        if(flag == 1){
            horizontalCalendar.selectDate(startDate, true);
        }
    }

    public void setTransactionList(ArrayList<TransactionsResult> list){

        if(list.size() == 0){

            recyclerViewTransactions.setVisibility(View.GONE);
            swipeRefreshLayoutTransactions.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);

        }
        else{
            swipeRefreshLayoutTransactions.setVisibility(View.VISIBLE);
            recyclerViewTransactions.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);

        }

        String month, date ;

        month = CommonUtils.convertMonth((horizontalCalendar.getSelectedDate().get(Calendar.MONTH) + 1));
        date = CommonUtils.convertDay(horizontalCalendar.getSelectedDate().get(Calendar.DATE));

        adapter = new TransactionsAdapter(list, getContext(), ViewTransactionsFragment.this,
                horizontalCalendar.getSelectedDate().get(Calendar.YEAR) + "-" + month + "-" + date);
        recyclerViewTransactions.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewTransactions.setLayoutManager(layoutManager);
        recyclerViewTransactions.setAdapter(adapter);

    }


    @Override
    public void onCancelClick() {

        for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){

            if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())){

                transactionsList.clear();
                loadTransactionData(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId());

            }

        }

//        ViewDocksFragment viewDocksFragment = new ViewDocksFragment();
//
//        try{
//
//            for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){
//
//                if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(viewDocksFragment.spinnerWarehouse.getSelectedItem().toString())){
//
//                    viewDocksFragment.loadDocksData(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId(),viewDocksFragment.horizontalCalendar.getSelectedDate());
//
//
//                }
//
//            }
//
//        }catch (Exception e){
//
//        }

    }


    @Override
    public void OnScheduleClick(String transactionsAdapterSchedule, String id, String name, String type,String transactionId,
                                String lrNumber, String transporterName, String srcName, String destName, String phno,
                                String vehicleNumber, String status, long time) {

        String tempSelectedDate = horizontalCalendar.getSelectedDate().get(Calendar.YEAR) + "-" +
                CommonUtils.convertMonth((horizontalCalendar.getSelectedDate().get(Calendar.MONTH) + 1)) + "-" +
                CommonUtils.convertMonth(horizontalCalendar.getSelectedDate().get(Calendar.DATE));

        //send all data via intent
        Intent intent = new Intent(getContext(), AddTransactionActivity.class);

        intent.putExtra("from", transactionsAdapterSchedule);
        intent.putExtra("selectedWarehouseId", id);
        intent.putExtra("selectedWarehouseName", name);
        intent.putExtra("transactionType", type);
        intent.putExtra("transactionId", transactionId);
        intent.putExtra("lrNumber", lrNumber);
        intent.putExtra("transporter", transporterName);
        intent.putExtra("selectedDate", tempSelectedDate);
        intent.putExtra("source", srcName);
        intent.putExtra("destination", destName);
        intent.putExtra("driver", phno);
        intent.putExtra("vehicle", vehicleNumber);
        intent.putExtra("currentStatus", status);
        intent.putExtra("currentStatusTime", time);

        getContext().startActivity(intent);

    }

    @Override
    public void onStatusChange() {

        for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){

            if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())){

                transactionsList.clear();
                loadTransactionData(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId());

            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){

                if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())){

                    transactionsList.clear();
                    loadTransactionData(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId());

                }

            }
        }catch (Exception e){
            loadTransactionData(selectedWarehouseId);
        }
    }
}
