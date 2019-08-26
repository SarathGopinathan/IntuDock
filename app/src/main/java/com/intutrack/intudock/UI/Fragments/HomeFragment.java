package com.intutrack.intudock.UI.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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
import com.intutrack.intudock.R;
import com.intutrack.intudock.Utilities.CommonUtils;
import com.intutrack.intudock.Utilities.CustomMarkerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private View rootView;
    private BarChart barChart;
    private RelativeLayout relativeLayoutToday, relativeLayoutLastWeek, relativeLayoutLastMonth, relativeLayoutCustom, relativeLayoutApply;
    private TextView today,lastWeek, lastMonth, custom, fromDate, toDate, apply, totalTransactions ;
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

        calFromDate = Calendar.getInstance();
        calToDate = Calendar.getInstance();

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

        yVals1.add(new BarEntry(0, 200));
        yVals1.add(new BarEntry(1, 200));
        yVals1.add(new BarEntry(2, 200));
        yVals1.add(new BarEntry(3, 200));
        yVals1.add(new BarEntry(4, 30));
        yVals1.add(new BarEntry(5, 220));
        yVals1.add(new BarEntry(6, 300));
        yVals1.add(new BarEntry(7, 150));
        yVals1.add(new BarEntry(8, 150));
        yVals1.add(new BarEntry(9, 150));
        yVals1.add(new BarEntry(10, 150));
        yVals1.add(new BarEntry(11, 150));

        BarDataSet set1;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)barChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            // create 2 datasets with different types
            set1 = new BarDataSet(yVals1, "2017");
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

                        //call api

                    }else {

                        CommonUtils.alert(getContext(), "Please select a to date that is ahead of from date!");

                    }

                }else{

                    relativeLayoutApply.setBackground(getResources().getDrawable(R.drawable.rounded_rect_solid_purple));
                    apply.setTextColor(getResources().getColor(R.color.white));


                    //call api

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

}
