package com.intutrack.intudock.UI.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.cleveroad.adaptivetablelayout.OnItemClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.intutrack.intudock.Models.Docks.DocksModel;
import com.intutrack.intudock.Models.Docks.DocksResult;
import com.intutrack.intudock.Models.Docks.RunningHours;
import com.intutrack.intudock.Models.SlotsData.SlotsModel;
import com.intutrack.intudock.Models.Transaction.TransactionsModel;
import com.intutrack.intudock.Preference.PrefEntities;
import com.intutrack.intudock.Preference.Preferences;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Retrofit.Helper.APIUtility;
import com.intutrack.intudock.UI.Adapters.DocksAdapter;
import com.intutrack.intudock.UI.Adapters.TableAdapter;
import com.intutrack.intudock.UI.CalendarViewActivity;
import com.intutrack.intudock.UI.IntuApplication;
import com.intutrack.intudock.UI.SelectTransactionActivity;
import com.intutrack.intudock.UI.TransactionDetailsActivity;
import com.intutrack.intudock.Utilities.CommonUtils;
import com.intutrack.intudock.Utilities.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ViewDocksFragment extends Fragment implements OnItemClickListener, DocksAdapter.DocksAdapterCallback {

    private ImageButton calendar;
    private ImageView noData;
    private TextView tileView, calendarView, slotDuration;
    public HorizontalCalendar horizontalCalendar;
    private RecyclerView recyclerViewDocks;
    private DocksAdapter adapter;
    private DocksModel model;
    private Calendar cal;
    private AdaptiveTableLayout tableViewDocks;
    private TableAdapter tableAdapter;
    private int day, month, year;
    private RelativeLayout relativeLayoutPlus, relativeLayoutMinus;
    public Spinner spinnerCity, spinnerWarehouse;
    private ArrayList<DocksResult> docksList;
    private int currentSlotDurationTime;
    private LinearLayout linearLayoutTimePeriod;
    private SwipeRefreshLayout swipeRefreshLayoutDocks;
    private ArrayList citiesList, warehouses;
    private String selectedCity, selectedWarehouse, selectedWarehouseId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_view_docks, container, false);

        calendar = rootView.findViewById(R.id.ib_calendar);
        noData = rootView.findViewById(R.id.iv_no_data);

        tileView = rootView.findViewById(R.id.tv_tile_view);
        calendarView = rootView.findViewById(R.id.tv_calendar_view);
        slotDuration = rootView.findViewById(R.id.tv_slot_duration);

        recyclerViewDocks = rootView.findViewById(R.id.rv_docks);

        tableViewDocks = rootView.findViewById(R.id.tableLayout);

        spinnerCity = rootView.findViewById(R.id.spinner_city);
        spinnerWarehouse = rootView.findViewById(R.id.spinner_warehouse);

        relativeLayoutPlus = rootView.findViewById(R.id.rl_plus);
        relativeLayoutMinus = rootView.findViewById(R.id.rl_minus);

        linearLayoutTimePeriod = rootView.findViewById(R.id.ll_time_period);

        swipeRefreshLayoutDocks = rootView.findViewById(R.id.swipe_refresh_docks);

        docksList = new ArrayList<>();

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        currentSlotDurationTime = Constants.SLOT_DURATION;

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

                        docksList.clear();
                        loadDocksData(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId(), horizontalCalendar.getSelectedDate());

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){

                    if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())){

                        docksList.clear();
                        loadDocksData(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId(), horizontalCalendar.getSelectedDate());

                    }

                }

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
        selectedDateBegining.set(Calendar.DATE, Integer.valueOf(selectedDay));
        selectedDateBegining.set(Calendar.YEAR, Integer.valueOf(selectedYear));

        Calendar selectedDateEnd = Calendar.getInstance();
        selectedDateEnd.set(Calendar.MONTH, Integer.valueOf(selectedMonth));
        selectedDateEnd.set(Calendar.DATE, Integer.valueOf(selectedDay));
        selectedDateEnd.set(Calendar.YEAR, Integer.valueOf(selectedYear));

        Calendar selectedStartDate = selectedDateBegining;
        selectedStartDate.add(Calendar.DATE, 0);

        Calendar selectedEndDate = selectedDateEnd;
        selectedEndDate.add(Calendar.DATE, 5);

        setUpHorizontalCalendar(selectedStartDate, selectedEndDate, 0);

        tileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tileViewData(docksList);
                linearLayoutTimePeriod.setVisibility(VISIBLE);

            }
        });

        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendarViewData(docksList);
                linearLayoutTimePeriod.setVisibility(GONE);

            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendarDialog();

            }
        });

        slotDuration.setText(Constants.SLOT_DURATION + " mins");

        relativeLayoutPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentSlotDurationTime += Constants.SLOT_DURATION;
                slotDuration.setText(currentSlotDurationTime + " mins");

                tileViewData(docksList);

            }
        });

        relativeLayoutMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentSlotDurationTime > 30){
                    currentSlotDurationTime -= Constants.SLOT_DURATION;
                    slotDuration.setText(currentSlotDurationTime + " mins");
                }

                tileViewData(docksList);

            }
        });

        swipeRefreshLayoutDocks.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){

                    if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())){

                        docksList.clear();
                        loadDocksData(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId(), horizontalCalendar.getSelectedDate());

                    }

                }

                if(swipeRefreshLayoutDocks != null)
                    swipeRefreshLayoutDocks.setRefreshing(false);

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    public void loadDocksData(String selectedWarehouseId, Calendar selectedStartDate) {

        String month, day ;

        month = CommonUtils.convertMonth((selectedStartDate.get(Calendar.MONTH) + 1));
        day = CommonUtils.convertDay(selectedStartDate.get(Calendar.DAY_OF_MONTH));

        String date = selectedStartDate.get(Calendar.YEAR) + "-" + month + "-" + day;

        IntuApplication.getApiUtility().DockData(getContext(), true, selectedWarehouseId, date, new APIUtility.APIResponseListener<DocksModel>() {
            @Override
            public void onReceiveResponse(DocksModel response) {

                docksList.clear();

                for(int i = 0; i< response.getResult().size(); i++){

                    docksList.add(response.getResult().get(i));

                }

                Preferences.storeDocksList(getContext(), docksList);

                tileViewData(docksList);

            }
        });

    }

    private void tileViewData(ArrayList<DocksResult> docksList) {

//        ArrayList<DocksResult> filteredList = new ArrayList<>();

//        for ( int i = 0; i < docksList.size(); i++){
//
//            long slotSize = (docksList.get(i).getSlotSize() / 1000)  / 60;
//            if (slotSize >= currentSlotDurationTime){
//                filteredList.add(docksList.get(i));
//            }
//
//        }

        tileView.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_dark_orange));
        tileView.setTextColor(getContext().getResources().getColor(R.color.white));

        calendarView.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_light_orange));
        calendarView.setTextColor(getContext().getResources().getColor(R.color.dark_orange));

        tableViewDocks.setVisibility(GONE);

        recyclerViewDocks.setVisibility(VISIBLE);
        swipeRefreshLayoutDocks.setVisibility(VISIBLE);

        if(docksList.size() == 0){

            swipeRefreshLayoutDocks.setVisibility(GONE);
            recyclerViewDocks.setVisibility(GONE);
            noData.setVisibility(VISIBLE);

        }
        else{
            swipeRefreshLayoutDocks.setVisibility(VISIBLE);
            recyclerViewDocks.setVisibility(VISIBLE);
            noData.setVisibility(GONE);

        }

        String selectedWarehouseId = "";
        String selectedWarehouseName = "";

        try{

            for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() - 1; i++){
                if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())) {
                    selectedWarehouseId = Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId();
                    selectedWarehouseName = Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName();
                }
            }

        }catch(Exception e){

        }

        adapter = new DocksAdapter(docksList, getContext(), ViewDocksFragment.this, currentSlotDurationTime, horizontalCalendar.getSelectedDate(),
                selectedWarehouseId, selectedWarehouseName);
        recyclerViewDocks.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewDocks.setLayoutManager(layoutManager);
        recyclerViewDocks.setAdapter(adapter);

    }

    private void calendarViewData(ArrayList<DocksResult> docksList) {

        calendarView.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_dark_orange));
        calendarView.setTextColor(getContext().getResources().getColor(R.color.white));

        tileView.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_rect_light_orange));
        tileView.setTextColor(getContext().getResources().getColor(R.color.dark_orange));

        if(docksList.size() == 0){
            swipeRefreshLayoutDocks.setVisibility(GONE);
            noData.setVisibility(VISIBLE);
        }
        else{

            noData.setVisibility(GONE);

            swipeRefreshLayoutDocks.setVisibility(GONE);

            recyclerViewDocks.setVisibility(GONE);

            tableViewDocks.setVisibility(VISIBLE);

            ArrayList<DocksResult> tempDocksList = new ArrayList<>();

            int pos = 0;

            ArrayList timeList = new ArrayList();

            timeList.add("08:00 am");
            timeList.add("08:30 am");
            timeList.add("09:00 am");
            timeList.add("09:30 am");
            timeList.add("10:00 am");
            timeList.add("10:30 am");
            timeList.add("11:00 am");
            timeList.add("11:30 am");
            timeList.add("12:00 pm");
            timeList.add("12:30 pm");
            timeList.add("01:00 pm");
            timeList.add("01:30 pm");
            timeList.add("02:00 pm");
            timeList.add("02:30 pm");
            timeList.add("03:00 pm");
            timeList.add("03:30 pm");
            timeList.add("04:00 pm");
            timeList.add("04:30 pm");
            timeList.add("05:00 pm");
            timeList.add("05:30 pm");
            timeList.add("06:00 pm");
            timeList.add("06:30 pm");
            timeList.add("07:00 pm");
            timeList.add("07:30 pm");

            for(int i = 0; i < docksList.size(); i++){

                ArrayList<SlotsModel> tempSlotsList = new ArrayList<>();

                DocksResult tempDockResult = new DocksResult();

                RunningHours tempRunningHours = new RunningHours();

                tempDockResult.setDockId(docksList.get(i).getDockId());
                tempDockResult.setDockName(docksList.get(i).getDockName());
                tempDockResult.setType(docksList.get(i).getType());
                tempDockResult.setBookedSlots(docksList.get(i).getBookedSlots());
                tempDockResult.setAvailableSlots(docksList.get(i).getAvailableSlots());
                tempDockResult.setSlotSize(docksList.get(i).getSlotSize());

                tempRunningHours.setStart(docksList.get(i).getRunningHours().getStart());
                tempRunningHours.setEnd(docksList.get(i).getRunningHours().getEnd());

                for (int j = 0; j < 24; j++){

                    SlotsModel tempSlots = new SlotsModel();

                    if(timeList.get(j).toString().equalsIgnoreCase(CommonUtils.convertTimestampToTime(docksList.get(i).getSlots().get(pos).getStart()))){

                        tempSlots.setTransactionId(docksList.get(i).getSlots().get(pos).getTransactionId());
                        tempSlots.setBooked(docksList.get(i).getSlots().get(pos).isBooked());
                        tempSlots.setBookable(docksList.get(i).getSlots().get(pos).isBookable());
                        tempSlots.setStart(docksList.get(i).getSlots().get(pos).getStart());
                        tempSlots.setDuration(docksList.get(i).getSlots().get(pos).getDuration());
                        tempSlots.setTransactionResult(docksList.get(i).getSlots().get(pos).getTransactionResult());

                        tempSlotsList.add(tempSlots);

                        pos += 1;

                    }else{

                        tempSlots.setTransactionId(docksList.get(i).getSlots().get(pos-1).getTransactionId());
                        tempSlots.setBooked(docksList.get(i).getSlots().get(pos-1).isBooked());
                        tempSlots.setBookable(docksList.get(i).getSlots().get(pos-1).isBookable());
                        tempSlots.setStart(docksList.get(i).getSlots().get(pos-1).getStart());
                        tempSlots.setDuration(docksList.get(i).getSlots().get(pos-1).getDuration());
                        tempSlots.setTransactionResult(docksList.get(i).getSlots().get(pos-1).getTransactionResult());

                        tempSlotsList.add(tempSlots);

                    }
                }

                pos = 0;
                tempDockResult.setSlots(tempSlotsList);
                tempDocksList.add(tempDockResult);

            }

            tableAdapter = new TableAdapter(getContext(), "ViewDocksFragment", tempDocksList, horizontalCalendar.getSelectedDate());

            tableAdapter.setOnItemClickListener(this);
            tableViewDocks.setAdapter(tableAdapter);

            tableViewDocks.setHeaderFixed(true);
            tableViewDocks.setSolidRowHeader(true);
            tableViewDocks.notifyDataSetChanged();

        }

    }

    public void calendarDialog(){

        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {

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

        Log.e("ViewDocksFrag", "date: " + startDate.get(Calendar.DATE));

        horizontalCalendar.setRange(startDate, endDate);
        horizontalCalendar.refresh();
        if(flag == 1) {
            horizontalCalendar.selectDate(startDate, true);
        }

        try{

            for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){

                if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())){

                    loadDocksData(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId(), startDate);

                }

            }

        }catch (Exception exception){

        }

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){

                    if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())){

                        loadDocksData(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId(), date);

                    }

                }

            }
        });

    }

    @Override
    public void onItemClick(int row, final int column) {

        if(Preferences.getPreference_boolean(getContext(), PrefEntities.SLOTS_CLICK_LISTENER_FLAG)){

            final ArrayList<DocksResult> tempDocksList = new ArrayList<>();

            int pos = 0;

            ArrayList timeList = new ArrayList();

            timeList.add("08:00 am");
            timeList.add("08:30 am");
            timeList.add("09:00 am");
            timeList.add("09:30 am");
            timeList.add("10:00 am");
            timeList.add("10:30 am");
            timeList.add("11:00 am");
            timeList.add("11:30 am");
            timeList.add("12:00 pm");
            timeList.add("12:30 pm");
            timeList.add("01:00 pm");
            timeList.add("01:30 pm");
            timeList.add("02:00 pm");
            timeList.add("02:30 pm");
            timeList.add("03:00 pm");
            timeList.add("03:30 pm");
            timeList.add("04:00 pm");
            timeList.add("04:30 pm");
            timeList.add("05:00 pm");
            timeList.add("05:30 pm");
            timeList.add("06:00 pm");
            timeList.add("06:30 pm");
            timeList.add("07:00 pm");
            timeList.add("07:30 pm");

            for(int i = 0; i < docksList.size(); i++){

                ArrayList<SlotsModel> tempSlotsList = new ArrayList<>();

                DocksResult tempDockResult = new DocksResult();

                RunningHours tempRunningHours = new RunningHours();

                tempDockResult.setDockId(docksList.get(i).getDockId());
                tempDockResult.setDockName(docksList.get(i).getDockName());
                tempDockResult.setType(docksList.get(i).getType());
                tempDockResult.setBookedSlots(docksList.get(i).getBookedSlots());
                tempDockResult.setAvailableSlots(docksList.get(i).getAvailableSlots());
                tempDockResult.setSlotSize(docksList.get(i).getSlotSize());

                tempRunningHours.setStart(docksList.get(i).getRunningHours().getStart());
                tempRunningHours.setEnd(docksList.get(i).getRunningHours().getEnd());

                for (int j = 0; j < 24; j++){

                    SlotsModel tempSlots = new SlotsModel();

                    if(timeList.get(j).toString().equalsIgnoreCase(CommonUtils.convertTimestampToTime(docksList.get(i).getSlots().get(pos).getStart()))){

                        tempSlots.setTransactionId(docksList.get(i).getSlots().get(pos).getTransactionId());
                        tempSlots.setBooked(docksList.get(i).getSlots().get(pos).isBooked());
                        tempSlots.setBookable(docksList.get(i).getSlots().get(pos).isBookable());
                        tempSlots.setStart(docksList.get(i).getSlots().get(pos).getStart());
                        tempSlots.setDuration(docksList.get(i).getSlots().get(pos).getDuration());
                        tempSlots.setTransactionResult(docksList.get(i).getSlots().get(pos).getTransactionResult());

                        tempSlotsList.add(tempSlots);

                        pos += 1;

                    }else{

                        tempSlots.setTransactionId(docksList.get(i).getSlots().get(pos-1).getTransactionId());
                        tempSlots.setBooked(docksList.get(i).getSlots().get(pos-1).isBooked());
                        tempSlots.setBookable(docksList.get(i).getSlots().get(pos-1).isBookable());
                        tempSlots.setStart(docksList.get(i).getSlots().get(pos-1).getStart());
                        tempSlots.setDuration(docksList.get(i).getSlots().get(pos-1).getDuration());
                        tempSlots.setTransactionResult(docksList.get(i).getSlots().get(pos-1).getTransactionResult());

                        tempSlotsList.add(tempSlots);

                    }
                }
                pos = 0;
                tempDockResult.setSlots(tempSlotsList);
                tempDocksList.add(tempDockResult);
            }

            String selectedWarehouseId = "";
            String selectedWarehouseName = "";

            for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() - 1; i++){
                if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())) {
                    selectedWarehouseId = Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId();
                    selectedWarehouseName = Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName();
                }
            }


            if(tempDocksList.get(row-1).getSlots().get(column-1).isBooked()){
                if (tempDocksList.get(row-1).getSlots().get(column-1).isBookable()){

                    Intent intent = new Intent(getContext(), TransactionDetailsActivity.class);
                    intent.putExtra("from", "SlotsAdapterBooked");
                    intent.putExtra("transactionType", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionResult().getType());
                    intent.putExtra("lrNumber", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionResult().getLrNumber());
                    intent.putExtra("transactionId", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionId());
                    intent.putExtra("transporter", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionResult().getTransporterName());
                    intent.putExtra("selectedDate", horizontalCalendar.getSelectedDate().get(Calendar.YEAR) + "-" +
                            CommonUtils.convertMonth((horizontalCalendar.getSelectedDate().get(Calendar.MONTH))+1) + "-" +
                            CommonUtils.convertMonth(horizontalCalendar.getSelectedDate().get(Calendar.DATE)));
                    intent.putExtra("source", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionResult().getSrcWarehouse().getName());
                    intent.putExtra("destination", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionResult().getDestWarehouse().getName());
                    intent.putExtra("driver", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionResult().getPhoneNumber().get(0).toString());
                    intent.putExtra("vehicle", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionResult().getVehicleNumber());
                    intent.putExtra("currentStatus", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionResult().getCurrentStatus().get(0).getStatus());
                    intent.putExtra("currentStatusTime", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionResult().getCurrentStatus().get(0).getTime());
                    intent.putExtra("dockName", tempDocksList.get(row-1).getDockName());
                    intent.putExtra("dockId", tempDocksList.get(row-1).getDockId());
                    intent.putExtra("slot", "" + tempDocksList.get(row-1).getSlots().get(column-1).getStart());
                    intent.putExtra("slotDuration", "" + (tempDocksList.get(row -1).getSlots().get(column-1).getDuration()/1000)/60);
                    intent.putExtra("selectedWarehouseId", selectedWarehouseId);
                    intent.putExtra("selectedWarehouseName", selectedWarehouseName);
                    intent.putExtra("slotSize", ""+tempDocksList.get(row-1).getSlotSize());

                    getContext().startActivity(intent);

                }
            }else{

                if (tempDocksList.get(row-1).getSlots().get(column-1).isBookable()){

                    final String finalSelectedWarehouseId = selectedWarehouseId;

                    final int finalRow = row;
                    IntuApplication.getApiUtility().TransactionsData(getContext(), true, selectedWarehouseId, new APIUtility.APIResponseListener<TransactionsModel>() {
                        @Override
                        public void onReceiveResponse(final TransactionsModel response) {

                            int count = 0;

//                        for(int i = 0; i < response.getResult().size(); i++){
//
//                            if(!response.getResult().get(i).isAssigned() &&
//                                    (response.getResult().get(i).getCurrentStatus().get(0).getStatus().equals("REJECTED") ||
//                                            response.getResult().get(i).getCurrentStatus().get(0).getStatus().equals("COMPLETED")  )){
//
//                                count += 1;
//
//                            }
//
//                            if (!response.getResult().get(i).isAssigned()){
//
//                                if(response.getResult().get(i).getCurrentStatus().get(0).getStatus().equals("REJECTED"))
//                                    Log.e("TAG", "current status: " + response.getResult().get(i).getCurrentStatus().get(0).getStatus());
//
//                            }
//
//                        }

                            if(count > 0){

                                Intent intent = new Intent(getContext(), SelectTransactionActivity.class);
                                intent.putExtra("dockId", tempDocksList.get(finalRow -1).getDockId());
                                intent.putExtra("from", "slotsAdapter");
                                intent.putExtra("selectedWarehouseId", finalSelectedWarehouseId);
                                intent.putExtra("slotDuration", "" + (tempDocksList.get(finalRow -1).getSlots().get(column-1).getDuration()/1000)/60);
                                intent.putExtra("slotStartTime", ""+tempDocksList.get(finalRow -1).getSlots().get(column-1).getStart());
                                intent.putExtra("selectedDate", horizontalCalendar.getSelectedDate().get(Calendar.YEAR) + "-" +
                                        CommonUtils.convertMonth((horizontalCalendar.getSelectedDate().get(Calendar.MONTH))+1) + "-" +
                                        CommonUtils.convertMonth(horizontalCalendar.getSelectedDate().get(Calendar.DATE)));
                                intent.putExtra("slotSize", ""+tempDocksList.get(finalRow -1).getSlotSize());
                                getContext().startActivity(intent);

                            }else{

                                Toast.makeText(getContext(),"There are no transactions available to link the slot!",Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }

            }


        }

    }

    @Override
    public void onRowHeaderClick(int row) {

        DocksResult model = docksList.get(row-1);

        Calendar cal = Calendar.getInstance();

        String stringTodayDate = "";
        String stringSelectedDate = "";

        long strtTime = 0;

        stringTodayDate = cal.get(Calendar.YEAR) + "-" + CommonUtils.convertMonth((cal.get(Calendar.MONTH))+1) + "-" +
                CommonUtils.convertMonth(cal.get(Calendar.DATE));

        stringSelectedDate = horizontalCalendar.getSelectedDate().get(Calendar.YEAR) + "-" +
                CommonUtils.convertMonth((horizontalCalendar.getSelectedDate().get(Calendar.MONTH))+1) + "-" +
                CommonUtils.convertMonth(horizontalCalendar.getSelectedDate().get(Calendar.DATE));

        boolean flagOccupied = false;

        if(stringSelectedDate.equals(stringTodayDate)){

            String transacType = "";
            String lrNumber = "";

            for(int i = 0; i < model.getSlots().size(); i++){

                if(model.getSlots().get(i).isBookable()){

                    if(model.getSlots().get(i-1).isBooked()){

                        flagOccupied = true;
                        transacType = model.getSlots().get(i-1).getTransactionResult().getType();
                        lrNumber = model.getSlots().get(i-1).getTransactionResult().getLrNumber();
                        strtTime = model.getSlots().get(i-1).getStart();

                    }
                    else{
                        flagOccupied = false;
                    }
                    i = model.getSlots().size();
                }

            }

            if(flagOccupied){

                final Dialog dialog = new Dialog(getContext());

                dialog.setCancelable(false);

                dialog.setContentView(R.layout.dialog_timer);

                dialog.show();

                TextView lrNo = dialog.findViewById(R.id.tv_lr_number);
                TextView transType = dialog.findViewById(R.id.tv_transaction_type);
                final TextView timer = dialog.findViewById(R.id.tv_timer);
                Button close = dialog.findViewById(R.id.btn_close);

                lrNo.setText(lrNumber);
                transType.setText(transacType);

                final Timer updateTimer;

                updateTimer = new Timer();
                final long finalStrtTime = strtTime;
                updateTimer.schedule(new TimerTask() {
                    public void run() {
                        try {

                            long millis = System.currentTimeMillis() - finalStrtTime;
                            final long hours = millis/(1000 * 60 * 60);
                            final long mins = (millis/(1000*60)) % 60;
                            final long seconds = (millis/(1000)) % 60;

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    timer.setText(hours + ":" + mins + ":" + seconds);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, 0, 1000);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        updateTimer.cancel();
                        dialog.cancel();

                    }
                });

            }

        }

    }

    @Override
    public void onColumnHeaderClick(int column) {

    }

    @Override
    public void onLeftTopHeaderClick() {

        String date = horizontalCalendar.getSelectedDate().get(Calendar.YEAR) + "-" + CommonUtils.convertMonth((horizontalCalendar.getSelectedDate().get(Calendar.MONTH))+1)
        + "-" + CommonUtils.convertMonth(horizontalCalendar.getSelectedDate().get(Calendar.DATE));

        String warehouseId = "";

        for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){

            if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())){

                warehouseId = Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId();

            }

        }

        Intent intent = new Intent(getContext(), CalendarViewActivity.class);

        intent.putExtra("date",date);
        intent.putExtra("warehouseId",warehouseId);

        startActivity(intent);

    }

    @Override
    public void OnTimerClick(final long startTime, String lrNumber, String transactionType) {

        final Dialog dialog = new Dialog(getContext());

        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_timer);

        dialog.show();

        TextView lrNo = dialog.findViewById(R.id.tv_lr_number);
        TextView transType = dialog.findViewById(R.id.tv_transaction_type);
        final TextView timer = dialog.findViewById(R.id.tv_timer);
        Button close = dialog.findViewById(R.id.btn_close);

        lrNo.setText(lrNumber);
        transType.setText(transactionType);

        final Timer updateTimer;

        updateTimer = new Timer();
        updateTimer.schedule(new TimerTask() {
            public void run() {
                try {

                    long millis = System.currentTimeMillis() - startTime;
                    final long hours = millis/(1000 * 60 * 60);
                    final long mins = (millis/(1000*60)) % 60;
                    final long seconds = (millis/(1000)) % 60;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timer.setText(hours + ":" + mins + ":" + seconds);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, 0, 1000);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateTimer.cancel();
                dialog.cancel();

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Docks Fragment", "onResume!");
        try {
            for(int i = 0; i < Preferences.loadWarehouseList(getContext()).size() -1 ; i++ ){

                if(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseName().equals(spinnerWarehouse.getSelectedItem().toString())){

                    loadDocksData(Preferences.loadWarehouseList(getContext()).get(i).getWarehouseId(), horizontalCalendar.getSelectedDate());

                }

            }
        }catch (Exception e){
            loadDocksData(selectedWarehouseId, horizontalCalendar.getSelectedDate());
        }

    }
}
