package com.intutrack.intudock.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.cleveroad.adaptivetablelayout.OnItemClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.intutrack.intudock.Models.BaseResponse;
import com.intutrack.intudock.Models.Docks.DocksModel;
import com.intutrack.intudock.Models.Docks.DocksResult;
import com.intutrack.intudock.Models.Docks.RunningHours;
import com.intutrack.intudock.Models.SlotsData.SlotsModel;
import com.intutrack.intudock.Models.Transaction.TransactionsModel;
import com.intutrack.intudock.Preference.PrefEntities;
import com.intutrack.intudock.Preference.Preferences;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Retrofit.Helper.APIUtility;
import com.intutrack.intudock.UI.Adapters.TableAdapter;
import com.intutrack.intudock.Utilities.CommonUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CalendarViewActivity extends BaseActivity implements OnItemClickListener{


    private AdaptiveTableLayout tableViewDocks;
    private TableAdapter tableAdapter;
    private ArrayList<DocksResult> docksList;
    private String date, warehouseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        tableViewDocks = findViewById(R.id.tableLayout);

        date = getIntent().getExtras().getString("date");
        warehouseId = getIntent().getExtras().getString("warehouseId");

        docksList = new ArrayList<>();

        IntuApplication.getApiUtility().DockData(this, true, warehouseId, date, new APIUtility.APIResponseListener<DocksModel>() {
            @Override
            public void onReceiveResponse(DocksModel response) {

                Log.e("CalendarAct", "DATE: " + date);

                docksList.addAll(response.getResult());

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0,4)));
                calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(5,7)));
                calendar.set(Calendar.DATE, Integer.parseInt(date.substring(8,10)));

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

                setTableAdapter(tempDocksList, calendar);
            }
        });
    }

    private void setTableAdapter(ArrayList<DocksResult> tempDocksList, Calendar calendar) {

        tableAdapter = new TableAdapter(CalendarViewActivity.this, "CalendarActivity", tempDocksList, calendar);
        tableViewDocks.setAdapter(tableAdapter);
        tableAdapter.setOnItemClickListener(this);

        tableViewDocks.setHeaderFixed(true);
        tableViewDocks.setSolidRowHeader(true);
        tableViewDocks.notifyDataSetChanged();


    }

    @Override
    public void onItemClick(int row, final int column) {

        if(Preferences.getPreference_boolean(CalendarViewActivity.this, PrefEntities.SLOTS_CLICK_LISTENER_FLAG)){

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

            String selectedWarehouseId = warehouseId;
            String selectedWarehouseName = "";

            for(int i = 0; i < Preferences.loadWarehouseList(this).size() - 1; i++){
                if(Preferences.loadWarehouseList(this).get(i).getWarehouseId().equals(selectedWarehouseId)) {
                    selectedWarehouseId = Preferences.loadWarehouseList(this).get(i).getWarehouseId();
                    selectedWarehouseName = Preferences.loadWarehouseList(this).get(i).getWarehouseName();
                }
            }


            if(tempDocksList.get(row-1).getSlots().get(column-1).isBooked()){
                if (tempDocksList.get(row-1).getSlots().get(column-1).isBookable()){

                    Intent intent = new Intent(this, TransactionDetailsActivity.class);
                    intent.putExtra("from", "SlotsAdapterBooked");
                    intent.putExtra("transactionType", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionResult().getType());
                    intent.putExtra("lrNumber", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionResult().getLrNumber());
                    intent.putExtra("transactionId", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionId());
                    intent.putExtra("transporter", tempDocksList.get(row-1).getSlots().get(column-1).getTransactionResult().getTransporterName());
                    intent.putExtra("selectedDate", date.substring(0,4) + "-" + date.substring(5,7) + "-" + date.substring(8,10));
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

                    startActivity(intent);

                }
            }else{

                if (tempDocksList.get(row-1).getSlots().get(column-1).isBookable()){


                    final String finalSelectedWarehouseId = selectedWarehouseId;

                    final int finalRow = row;
                    IntuApplication.getApiUtility().TransactionsData(CalendarViewActivity.this, true, finalSelectedWarehouseId, new APIUtility.APIResponseListener<TransactionsModel>() {
                        @Override
                        public void onReceiveResponse(final TransactionsModel response) {

                            int count = 0;

                            for(int i = 0; i < response.getResult().size(); i++){

                                if(!response.getResult().get(i).isAssigned()){

                                    count += 1;

                                }

                            }

                            if(count > 0){

                                Intent intent = new Intent(CalendarViewActivity.this, SelectTransactionActivity.class);
                                intent.putExtra("dockId", tempDocksList.get(finalRow -1).getDockId());
                                intent.putExtra("from", "slotsAdapter");
                                intent.putExtra("selectedWarehouseId", finalSelectedWarehouseId);
                                intent.putExtra("slotDuration", "" + (tempDocksList.get(finalRow -1).getSlots().get(column-1).getDuration()/1000)/60);
                                intent.putExtra("slotStartTime", ""+tempDocksList.get(finalRow -1).getSlots().get(column-1).getStart());
                                intent.putExtra("selectedDate", date.substring(0,4) + "-" + date.substring(5,7) + "-" + date.substring(8,10));
                                intent.putExtra("slotSize", ""+tempDocksList.get(finalRow -1).getSlotSize());
                                CalendarViewActivity.this.startActivity(intent);

                            }else{

                                Toast.makeText(CalendarViewActivity.this,"There are no transactions available to link the slot!",Toast.LENGTH_LONG).show();

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

        boolean flagOccupied = false;

        if(date.equals(stringTodayDate)){

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

                final Dialog dialog = new Dialog(CalendarViewActivity.this);

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

                            CalendarViewActivity.this.runOnUiThread(new Runnable() {
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

    }
}
