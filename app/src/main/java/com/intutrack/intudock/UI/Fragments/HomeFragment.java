package com.intutrack.intudock.UI.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.intutrack.intudock.Models.AnalyticsData.AnalyticsModel;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Retrofit.Helper.APIUtility;
import com.intutrack.intudock.UI.IntuApplication;
import com.intutrack.intudock.Utilities.CommonUtils;
import com.intutrack.intudock.Utilities.CustomMarkerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private View rootView;
    private BarChart barChart;
    private RelativeLayout relativeLayoutToday, relativeLayoutLastWeek, relativeLayoutLastMonth, relativeLayoutCustom, relativeLayoutApply;
    private TextView today,lastWeek, lastMonth, custom, fromDate, toDate, apply, totalTransactions, completedTransactions, completedTransactionPercent,
            upcomingTransactions, upcomingTransactionsPercent, totalDocks, occupied, unoccupied, mostUsed, leastUsed, totalUtilization,
            peakhour, peakhourTransactions, offpeakhour, offpeakhourTransactions, avgLoadingTime, avgLoadingTimePercent,
            avgUnloadingTime, avgUnloadingTimePercent, avgActivityTime, avgActivityTimePercent;
    private ImageView imageViewAvgLoadingTime, imageViewAvgUnloadingTime, imageViewAvgActivityTime;
    private ArrayList<String> xAxisLabel;
    private LinearLayout linearLayoutCustom, linearLayoutUpcoming, linearLayoutOccupied, linearLayoutUnoccupied;
    private Calendar calFromDate, calToDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        barChart = rootView.findViewById(R.id.bar_graph);

        linearLayoutCustom = rootView.findViewById(R.id.ll_custom);
        linearLayoutUpcoming = rootView.findViewById(R.id.ll_upcoming);
        linearLayoutOccupied = rootView.findViewById(R.id.ll_occupied);
        linearLayoutUnoccupied = rootView.findViewById(R.id.ll_unoccupied);

        relativeLayoutToday = rootView.findViewById(R.id.rl_today);
        relativeLayoutLastWeek = rootView.findViewById(R.id.rl_last_week);
        relativeLayoutLastMonth = rootView.findViewById(R.id.rl_last_month);
        relativeLayoutCustom = rootView.findViewById(R.id.rl_custom);
        relativeLayoutApply = rootView.findViewById(R.id.rl_apply);

        today = rootView.findViewById(R.id.tv_today);
        lastWeek = rootView.findViewById(R.id.tv_last_week);
        lastMonth = rootView.findViewById(R.id.tv_last_month);
        custom = rootView.findViewById(R.id.tv_custom);
        fromDate = rootView.findViewById(R.id.tv_from_date);
        toDate = rootView.findViewById(R.id.tv_to_date);
        apply = rootView.findViewById(R.id.tv_apply);
        totalTransactions = rootView.findViewById(R.id.tv_total_transactions);
        completedTransactions = rootView.findViewById(R.id.tv_completed);
        completedTransactionPercent = rootView.findViewById(R.id.tv_completed_percent);
        upcomingTransactions = rootView.findViewById(R.id.tv_upcoming);
        upcomingTransactionsPercent = rootView.findViewById(R.id.tv_upcoming_percent);
        totalDocks = rootView.findViewById(R.id.tv_total_docks);
        occupied = rootView.findViewById(R.id.tv_occupied);
        unoccupied = rootView.findViewById(R.id.tv_unoccupied);
        mostUsed = rootView.findViewById(R.id.tv_most_used);
        leastUsed = rootView.findViewById(R.id.tv_least_used);
        totalUtilization = rootView.findViewById(R.id.tv_total_utilization);
        peakhour = rootView.findViewById(R.id.tv_peak_hour_time);
        peakhourTransactions = rootView.findViewById(R.id.tv_peak_hour_value);
        offpeakhour = rootView.findViewById(R.id.tv_offpeak_hour_time);
        offpeakhourTransactions = rootView.findViewById(R.id.tv_offpeak_hour_value);
        avgLoadingTime = rootView.findViewById(R.id.tv_avg_loading_time);
        avgLoadingTimePercent = rootView.findViewById(R.id.tv_avg_loading_time_percent);
        avgUnloadingTime = rootView.findViewById(R.id.tv_avg_unloading_time);
        avgUnloadingTimePercent = rootView.findViewById(R.id.tv_avg_unloading_time_percent);
        avgActivityTime = rootView.findViewById(R.id.tv_avg_activity_time);
        avgActivityTimePercent = rootView.findViewById(R.id.tv_avg_activity_time_percent);

        imageViewAvgLoadingTime = rootView.findViewById(R.id.iv_avg_loading_time);
        imageViewAvgUnloadingTime = rootView.findViewById(R.id.iv_avg_unloading_time);
        imageViewAvgActivityTime = rootView.findViewById(R.id.iv_avg_activity_time);

        calFromDate = Calendar.getInstance();
        calToDate = Calendar.getInstance();

        todayData();

        relativeLayoutToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                todayData();

            }
        });

        relativeLayoutLastWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lastWeekData();

            }
        });

        relativeLayoutLastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lastMonthData();

            }
        });

        relativeLayoutCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customData();

            }
        });

        return rootView;
    }

    private void todayData() {

        relativeLayoutToday.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_purple));
        today.setTextColor(getResources().getColor(R.color.white));

        relativeLayoutLastWeek.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_grey));
        lastWeek.setTextColor(getResources().getColor(R.color.purple));

        relativeLayoutLastMonth.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_grey));
        lastMonth.setTextColor(getResources().getColor(R.color.purple));

        relativeLayoutCustom.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_grey));
        custom.setTextColor(getResources().getColor(R.color.purple));

        linearLayoutUpcoming.setVisibility(View.VISIBLE);
        linearLayoutOccupied.setVisibility(View.VISIBLE);
        linearLayoutUnoccupied.setVisibility(View.VISIBLE);
        linearLayoutCustom.setVisibility(View.GONE);
        
        //call API

        analyticsData();

    }

    private void lastWeekData() {

        relativeLayoutToday.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_grey));
        today.setTextColor(getResources().getColor(R.color.purple));

        relativeLayoutLastWeek.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_purple));
        lastWeek.setTextColor(getResources().getColor(R.color.white));

        relativeLayoutLastMonth.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_grey));
        lastMonth.setTextColor(getResources().getColor(R.color.purple));

        relativeLayoutCustom.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_grey));
        custom.setTextColor(getResources().getColor(R.color.purple));

        linearLayoutUpcoming.setVisibility(View.GONE);
        linearLayoutOccupied.setVisibility(View.GONE);
        linearLayoutUnoccupied.setVisibility(View.GONE);
        linearLayoutCustom.setVisibility(View.GONE);

        //call API
        analyticsData();

    }

    private void lastMonthData() {

        relativeLayoutToday.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_grey));
        today.setTextColor(getResources().getColor(R.color.purple));

        relativeLayoutLastWeek.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_grey));
        lastWeek.setTextColor(getResources().getColor(R.color.purple));

        relativeLayoutLastMonth.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_purple));
        lastMonth.setTextColor(getResources().getColor(R.color.white));

        relativeLayoutCustom.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_grey));
        custom.setTextColor(getResources().getColor(R.color.purple));

        linearLayoutUpcoming.setVisibility(View.GONE);
        linearLayoutOccupied.setVisibility(View.GONE);
        linearLayoutUnoccupied.setVisibility(View.GONE);
        linearLayoutCustom.setVisibility(View.GONE);

        //call API
        analyticsData();

    }

    private void customData() {

        relativeLayoutToday.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_grey));
        today.setTextColor(getResources().getColor(R.color.purple));

        relativeLayoutLastWeek.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_grey));
        lastWeek.setTextColor(getResources().getColor(R.color.purple));

        relativeLayoutLastMonth.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_grey));
        lastMonth.setTextColor(getResources().getColor(R.color.purple));

        relativeLayoutCustom.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_purple));
        custom.setTextColor(getResources().getColor(R.color.white));

        linearLayoutUpcoming.setVisibility(View.GONE);
        linearLayoutOccupied.setVisibility(View.GONE);
        linearLayoutUnoccupied.setVisibility(View.GONE);
        linearLayoutCustom.setVisibility(View.VISIBLE);

        fromDate.setText(calFromDate.get(Calendar.YEAR) + "-" + CommonUtils.convertMonth((calFromDate.get(Calendar.MONTH))+1) +
                "-" + CommonUtils.convertDay(calFromDate.get(Calendar.DATE)));

        toDate.setText(calToDate.get(Calendar.YEAR) + "-" + CommonUtils.convertMonth((calToDate.get(Calendar.MONTH))+1) +
                "-" + CommonUtils.convertDay(calToDate.get(Calendar.DATE)));

        relativeLayoutApply.setBackground(getResources().getDrawable(R.drawable.stroke_extra_rounded_rect_purple));
        apply.setTextColor(getResources().getColor(R.color.purple));

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                relativeLayoutApply.setBackground(getResources().getDrawable(R.drawable.stroke_extra_rounded_rect_purple));
                apply.setTextColor(getResources().getColor(R.color.purple));
                calendarDialog(0);

            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                relativeLayoutApply.setBackground(getResources().getDrawable(R.drawable.stroke_extra_rounded_rect_purple));
                apply.setTextColor(getResources().getColor(R.color.purple));
                calendarDialog(1);

            }
        });

        relativeLayoutApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(calFromDate.after(calToDate)){

                    if((calFromDate.get(Calendar.DAY_OF_MONTH) == calToDate.get(Calendar.DAY_OF_MONTH)) &&
                            (calFromDate.get(Calendar.MONTH) == calToDate.get(Calendar.MONTH)) &&
                            (calFromDate.get(Calendar.YEAR) == calToDate.get(Calendar.YEAR))){

                        relativeLayoutApply.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_purple));
                        apply.setTextColor(getResources().getColor(R.color.white));

                        //call API
                        analyticsData();

                    }else {

                        CommonUtils.alert(getContext(), "Please select a to date that is ahead of from date!");

                    }

                }else{

                    relativeLayoutApply.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_purple));
                    apply.setTextColor(getResources().getColor(R.color.white));

                    //call API
                    analyticsData();

                }

            }
        });


    }

    public void calendarDialog(final int fromOrTo){

        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {

                if (fromOrTo == 0){

                    calFromDate = Calendar.getInstance();

                    calFromDate.set(Calendar.YEAR, year);
                    calFromDate.set(Calendar.MONTH, monthOfYear);
                    calFromDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    fromDate.setText(year + "-" + CommonUtils.convertMonth(((monthOfYear)+1)) + "-" + CommonUtils.convertDay(dayOfMonth));

                }else{

                    calToDate = Calendar.getInstance();

                    calToDate.set(Calendar.YEAR, year);
                    calToDate.set(Calendar.MONTH, monthOfYear);
                    calToDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    toDate.setText(year + "-" + CommonUtils.convertMonth(((monthOfYear)+1)) + "-" + CommonUtils.convertDay(dayOfMonth));

                }
            }};

        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);

        if(fromOrTo == 0){

            DatePickerDialog dpDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, listener, year, month, day);
            dpDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
            dpDialog.show();

        }else{

            DatePickerDialog dpDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, listener, year, month, day);
            dpDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
            dpDialog.getDatePicker().setMinDate(calFromDate.getTimeInMillis());
            dpDialog.show();

        }

    }


    private void analyticsData(String startDate, String endDate) {

        //callAPI
        IntuApplication.getApiUtility().AnalyticsData(getContext(), true, "", "", new APIUtility.APIResponseListener<AnalyticsModel>() {
            @Override
            public void onReceiveResponse(AnalyticsModel response) {

                ArrayList graphValues = new ArrayList();
                graphValues.add("200");
                graphValues.add("200");
                graphValues.add("200");
                graphValues.add("200");
                graphValues.add("30");
                graphValues.add("220");
                graphValues.add("300");
                graphValues.add("150");
                graphValues.add("150");
                graphValues.add("150");
                graphValues.add("150");
                graphValues.add("150");

                setAnalyticsData("40","20","75%","10",
                        "25%","05", "04","01","D-01",
                        "D-11","40%","3pm-4pm","300 (50%)",
                        "3pm-4pm","30 (10%)","30 mins","10%",
                        "40 mins","10%","35 mins","10%", graphValues);

            }
        });

    }

    private void setAnalyticsData(String totalTransactionValue, String completedTransactionsValue, String completedTransactionPercentValue,
    String upcomingTransactionsValue, String upcomingTransactionsPercentValue, String totalDocksValue, String occupiedValue,
    String unoccupiedValue, String mostUsedValue, String leastUsedValue, String totalUtilizationValue, String peakhourValue,
    String peakhourTransactionsValue, String offpeakhourValue, String offpeakhourTransactionsValue, String avgLoadingTimeValue,
    String avgLoadingTimePercentValue, String avgUnloadingTimeValue, String avgUnloadingTimePercentValue, String avgActivityTimeValue,
    String avgActivityTimePercentValue, ArrayList graphValues) {

        totalTransactions.setText(totalTransactionValue);
        completedTransactions.setText(completedTransactionsValue);
        completedTransactionPercent.setText(completedTransactionPercentValue);
        upcomingTransactions.setText(upcomingTransactionsValue);
        upcomingTransactionsPercent.setText(upcomingTransactionsPercentValue);
        totalDocks.setText(totalDocksValue);
        occupied.setText(occupiedValue);
        unoccupied.setText(unoccupiedValue);
        mostUsed.setText(mostUsedValue);
        leastUsed.setText(leastUsedValue);
        totalUtilization.setText(totalUtilizationValue);
        peakhour.setText(peakhourValue);
        peakhourTransactions.setText(peakhourTransactionsValue);
        offpeakhour.setText(offpeakhourValue);
        offpeakhourTransactions.setText(offpeakhourTransactionsValue);
        avgLoadingTime.setText(avgLoadingTimeValue);
        avgLoadingTimePercent.setText(avgLoadingTimePercentValue);
        avgUnloadingTime.setText(avgUnloadingTimeValue);
        avgUnloadingTimePercent.setText(avgUnloadingTimePercentValue);
        avgActivityTime.setText(avgActivityTimeValue);
        avgActivityTimePercent.setText(avgActivityTimePercentValue);

//        if(avgLoadingTimePercentValueBoolean){
//            imageViewAvgLoadingTime.setImageDrawable(getResources().getDrawable(R.drawable.icon_down_arrow));
//        }else{
//            imageViewAvgLoadingTime.setImageDrawable(getResources().getDrawable(R.drawable.icon_up_arrow));
//        }
//
//        if(avgUnloadingTimePercentValueBoolean){
//            imageViewAvgLoadingTime.setImageDrawable(getResources().getDrawable(R.drawable.icon_down_arrow));
//        }else{
//            imageViewAvgLoadingTime.setImageDrawable(getResources().getDrawable(R.drawable.icon_up_arrow));
//        }
//
//        if(avgActivityTimePercentValueBoolean){
//            imageViewAvgLoadingTime.setImageDrawable(getResources().getDrawable(R.drawable.icon_down_arrow));
//        }else{
//            imageViewAvgLoadingTime.setImageDrawable(getResources().getDrawable(R.drawable.icon_up_arrow));
//        }

        graphData(graphValues);

    }

    private void graphData(ArrayList values) {

        xAxisLabel = new ArrayList<>();

        xAxisLabel.add("8am");
        xAxisLabel.add("9am");
        xAxisLabel.add("10am");
        xAxisLabel.add("11am");
        xAxisLabel.add("12pm");
        xAxisLabel.add("1pm");
        xAxisLabel.add("2pm");
        xAxisLabel.add("3pm");
        xAxisLabel.add("4pm");
        xAxisLabel.add("5pm");
        xAxisLabel.add("6pm");
        xAxisLabel.add("7pm");


        barChart.setDrawBarShadow(false);
        barChart.setMaxVisibleValueCount(50);
        barChart.setDrawGridBackground(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.animateY(1500);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setTextSize(0.3f);
        barChart.setTouchEnabled(true);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setScaleEnabled(false);
        IMarker marker = new CustomMarkerView(getContext(), R.layout.marker);
        barChart.setMarker(marker);


        XAxis xl = barChart.getXAxis();

        xl.setLabelCount(12);
        xl.setCenterAxisLabels(false);
        xl.setValueFormatter(new IndexAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value) {
                return xAxisLabel.get((int) value);
            }
        });

        //data

        List<BarEntry> yVals1 = new ArrayList<BarEntry>();

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinValue(0);
        leftAxis.setAxisMaximum(350);
        leftAxis.setStartAtZero(true);
        barChart.getAxisRight().setEnabled(false);

        for(int i = 0; i < 12; i++){

            yVals1.add(new BarEntry(i, Float.parseFloat(values.get(i).toString())));

        }

        BarDataSet set1;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)barChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            // create 2 datasets with different types
            set1 = new BarDataSet(yVals1, "");
            set1.setColor(getResources().getColor(R.color.blue_graph));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setDrawValues(false);
            barChart.setData(data);
        }

//        barChart.getXAxis().setAxisMinValue(0);
//        barChart.setFitBars(true);
        barChart.getLegend().setEnabled(false);
        barChart.invalidate();


    }

}
