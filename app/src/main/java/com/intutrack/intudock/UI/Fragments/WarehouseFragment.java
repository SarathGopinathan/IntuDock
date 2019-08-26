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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.intutrack.intudock.Models.Warehouse.WarehouseModel;
import com.intutrack.intudock.Models.Warehouse.WarehouseResult;
import com.intutrack.intudock.Preference.Preferences;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Retrofit.Helper.APIUtility;
import com.intutrack.intudock.UI.Adapters.WarehouseAdapter;
import com.intutrack.intudock.UI.DockTransactionActivity;
import com.intutrack.intudock.UI.IntuApplication;
import com.intutrack.intudock.Utilities.CommonUtils;

import java.util.ArrayList;
import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class WarehouseFragment extends Fragment implements WarehouseAdapter.WarehouseAdapterCallback {

    private WarehouseAdapter adapter;
    private RecyclerView recyclerViewWarehouse;
    private Spinner spinnerCity;
    private EditText search;
    private HorizontalCalendar horizontalCalendar;
    private ImageButton imageButtonCalendar;
    private ImageView noData,emptyData;
    private Calendar cal;
    private int day, month, year;
    private View rootView;
    private RelativeLayout relativeLayoutContent;
    private SwipeRefreshLayout swipeRefreshLayoutWarehouse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_warehouse, container, false);

        relativeLayoutContent = rootView.findViewById(R.id.rl_content);

        recyclerViewWarehouse = rootView.findViewById(R.id.rv_warehouses);
        spinnerCity = rootView.findViewById(R.id.spinner_city);
        search = rootView.findViewById(R.id.et_search);

        imageButtonCalendar = rootView.findViewById(R.id.ib_calendar);

        noData = rootView.findViewById(R.id.iv_no_data);
        emptyData = rootView.findViewById(R.id.iv_empty);

        swipeRefreshLayoutWarehouse = rootView.findViewById(R.id.swipe_refresh_warehouse);

        imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendarDialog();
            }
        });

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        warehouseData(day, month+1, year);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);

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

                warehouseData(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH)+1, date.get(Calendar.YEAR));

            }
        });

        swipeRefreshLayoutWarehouse.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                warehouseData(horizontalCalendar.getSelectedDate().get(Calendar.DATE),
                        (horizontalCalendar.getSelectedDate().get(Calendar.MONTH)+1), horizontalCalendar.getSelectedDate().get(Calendar.YEAR));

                search.setText("");

                if (swipeRefreshLayoutWarehouse != null) {
                    swipeRefreshLayoutWarehouse.setRefreshing(false);
                }

            }

        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void setUpHorizontalCalendar(Calendar startDate, Calendar endDate, int flag) {

        horizontalCalendar.setRange(startDate, endDate);
        horizontalCalendar.refresh();
        if(flag == 1) {
            horizontalCalendar.selectDate(startDate, true);
        }

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                warehouseData(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH)+1, date.get(Calendar.YEAR));

            }
        });

    }

    private void warehouseData(int day, int month, int year) {

        Log.e("TAG", "date: " + day + " month: " + month);

        String date = year + "-" + CommonUtils.convertMonth(month) + "-" + CommonUtils.convertDay(day);

        IntuApplication.getApiUtility().WarehouseData(getContext(), true, date, new APIUtility.APIResponseListener<WarehouseModel>() {
            @Override
            public void onReceiveResponse(final WarehouseModel response) {

                if(response.getResult().get(response.getResult().size()-1).getCities().size() > 0){

                    try{

                        emptyData.setVisibility(View.GONE);
                        relativeLayoutContent.setVisibility(View.VISIBLE);

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),   R.layout.item_spinner_content,
                                response.getResult().get(response.getResult().size()-1).getCities());
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner); // The drop down view
                        spinnerCity.setAdapter(spinnerArrayAdapter);

                        Preferences.storeWarehouseList(getContext(),response.getResult());

                        final ArrayList<WarehouseResult> warehouseList = new ArrayList<>();

                        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                                warehouseList.clear();

                                for(int i = 0; i < response.getResult().size()-1; i++ )
                                    if(response.getResult().get(i).getCity()
                                            .equalsIgnoreCase((String) response.getResult().get(response.getResult().size()-1).getCities().get(position)))
                                        warehouseList.add(response.getResult().get(i));

                                if(warehouseList.size() == 0){

                                    recyclerViewWarehouse.setVisibility(View.GONE);
                                    swipeRefreshLayoutWarehouse.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);

                                }
                                else{

                                    recyclerViewWarehouse.setVisibility(View.VISIBLE);
                                    swipeRefreshLayoutWarehouse.setVisibility(View.VISIBLE);
                                    noData.setVisibility(View.GONE);

                                }

                                adapter = new WarehouseAdapter( warehouseList, getContext(), WarehouseFragment.this);
                                recyclerViewWarehouse.setHasFixedSize(true);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                recyclerViewWarehouse.setLayoutManager(layoutManager);
                                recyclerViewWarehouse.setAdapter(adapter);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                                warehouseList.clear();

                                for(int i = 0; i < response.getResult().size()-1; i++ )
                                    if(response.getResult().get(i).getCity()
                                            .equalsIgnoreCase((String) response.getResult().get(response.getResult().size()-1).getCities().get(0)))
                                        warehouseList.add(response.getResult().get(i));

                                if(warehouseList.size() == 0){

                                    recyclerViewWarehouse.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);

                                }
                                else{

                                    recyclerViewWarehouse.setVisibility(View.VISIBLE);
                                    noData.setVisibility(View.GONE);

                                }

                                adapter = new WarehouseAdapter( warehouseList, getContext(), WarehouseFragment.this);
                                recyclerViewWarehouse.setHasFixedSize(true);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                recyclerViewWarehouse.setLayoutManager(layoutManager);
                                recyclerViewWarehouse.setAdapter(adapter);
                            }
                        });

                        search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                warehouseList.clear();
                                for(int j = 0; j < response.getResult().size()-1; j++ ){
                                    if(response.getResult().get(j).getWarehouseName().contains(charSequence) &&
                                            response.getResult().get(j).getCity().equals(spinnerCity.getSelectedItem().toString()))
                                        warehouseList.add(response.getResult().get(j));
                                }

                                if(warehouseList.size() == 0){

                                    recyclerViewWarehouse.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);

                                }
                                else{

                                    recyclerViewWarehouse.setVisibility(View.VISIBLE);
                                    noData.setVisibility(View.GONE);

                                    adapter = new WarehouseAdapter( warehouseList, getContext(), WarehouseFragment.this);
                                    recyclerViewWarehouse.setHasFixedSize(true);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                    recyclerViewWarehouse.setLayoutManager(layoutManager);
                                    recyclerViewWarehouse.setAdapter(adapter);


                                }

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });


                    }catch(Exception e){
                        try{
                            Toast.makeText(getActivity(), "Kindly refresh the page", Toast.LENGTH_LONG).show();
                        }catch (Exception exception){

                        }
                    }
                }else{

                    emptyData.setVisibility(View.VISIBLE);
                    relativeLayoutContent.setVisibility(View.GONE);

                }

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
                warehouseData(startDate.get(Calendar.DATE), startDate.get(Calendar.MONTH)+1, startDate.get(Calendar.YEAR));

            }};

        DatePickerDialog dpDialog=new DatePickerDialog(getContext(), R.style.DialogTheme, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        dpDialog.show();

    }

    @Override
    public void onWarehouseClick(String city, String warehouseName, String warehouseId) {

        Intent intent = new Intent(getContext(), DockTransactionActivity.class);
        intent.putExtra("city",city);
        intent.putExtra("warehouseName",warehouseName);
        intent.putExtra("warehouseId",warehouseId);
        intent.putExtra("day", horizontalCalendar.getSelectedDate().get(Calendar.DATE));
        intent.putExtra("month", horizontalCalendar.getSelectedDate().get(Calendar.MONTH));
        intent.putExtra("year", horizontalCalendar.getSelectedDate().get(Calendar.YEAR));

        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();

        try{
            warehouseData(Integer.parseInt(CommonUtils.convertDay(horizontalCalendar.getSelectedDate().get(Calendar.DATE))),
                    Integer.parseInt(CommonUtils.convertMonth((horizontalCalendar.getSelectedDate().get(Calendar.MONTH)+1))),
                    horizontalCalendar.getSelectedDate().get(Calendar.YEAR));
        }catch (Exception e){

        }

    }
}
