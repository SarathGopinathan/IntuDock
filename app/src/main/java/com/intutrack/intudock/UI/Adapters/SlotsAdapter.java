package com.intutrack.intudock.UI.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.intutrack.intudock.Models.SlotsData.SlotsModel;
import com.intutrack.intudock.Models.Transaction.TransactionsModel;
import com.intutrack.intudock.Preference.PrefEntities;
import com.intutrack.intudock.Preference.Preferences;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Retrofit.Helper.APIUtility;
import com.intutrack.intudock.UI.IntuApplication;
import com.intutrack.intudock.UI.SelectTransactionActivity;
import com.intutrack.intudock.UI.TransactionDetailsActivity;
import com.intutrack.intudock.Utilities.CommonUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class SlotsAdapter extends RecyclerView.Adapter<SlotsAdapter.MyViewHolder> {

    private ArrayList<SlotsModel> list;
    private String selectedDock, selectedWarehouseId, selectedWarehouseName, selectedDockName, slotSize;
    private int selectedSlotDuration;
    private Context context;
    private Calendar selectedDate;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView slotTime;
        private LinearLayout linearLayoutSlot;

        public MyViewHolder(View itemView) {
            super(itemView);

            slotTime = itemView.findViewById(R.id.tv_slot);
            linearLayoutSlot = itemView.findViewById(R.id.ll_slot);

        }
    }

    public SlotsAdapter(ArrayList<SlotsModel> list, Context mContext, String selectedDock, int selectedSlotDuration, Calendar selectedDate,
                        String selectedWarehouseId, String selectedWarehouseName, String selectedDockName, String slotSize){

        this.list = list;
        this.context = mContext;
        this.selectedDock = selectedDock;
        this.selectedSlotDuration = selectedSlotDuration;
        this.selectedDate = selectedDate;
        this.selectedWarehouseId = selectedWarehouseId;
        this.selectedWarehouseName = selectedWarehouseName;
        this.selectedDockName = selectedDockName;
        this.slotSize = slotSize;

    }

    @Override
    public SlotsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot,parent,false);
        return new SlotsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final SlotsModel model = list.get(position);

        String time = CommonUtils.convertTimestampToTime(model.getStart());

        holder.slotTime.setText(time);

        if(model.isBookable()){

            if(model.isBooked()){
                holder.linearLayoutSlot.setBackground(context.getResources().getDrawable(R.drawable.stroke_rounded_rect_lavender));
                holder.slotTime.setTextColor(context.getResources().getColor(R.color.lavender));
            }else{
                holder.linearLayoutSlot.setBackground(context.getResources().getDrawable(R.drawable.stroke_rounded_rect_purple));
                holder.slotTime.setTextColor(context.getResources().getColor(R.color.purple));
            }

        }else{

            if(model.isBooked()){
                holder.linearLayoutSlot.setBackground(context.getResources().getDrawable(R.drawable.stroke_rounded_rect_light_red_booked_slot));
                holder.slotTime.setTextColor(context.getResources().getColor(R.color.light_red_booked_slot));
            }else{
                holder.linearLayoutSlot.setBackground(context.getResources().getDrawable(R.drawable.stroke_rounded_rect_red_booked_slot));
                holder.slotTime.setTextColor(context.getResources().getColor(R.color.red_booked_slot));
            }

        }

        holder.slotTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Preferences.getPreference_boolean(context, PrefEntities.SLOTS_CLICK_LISTENER_FLAG)){

                    int bookableSlotCount = selectedSlotDuration/30;
                    int tempPosition = position;
                    boolean flagFront = false;
                    boolean flagBack = false;
                    ArrayList slotStartTimes = new ArrayList();
                    ArrayList<SlotsModel> slotsModel = list;

                    if(model.isBookable() && !model.isBooked()){

                        while (bookableSlotCount != 0){

                            if(!flagBack && !flagBack){

                                if(!flagFront){

                                    while(!slotsModel.get(tempPosition).isBooked() && slotsModel.get(tempPosition).isBookable()){

                                        if(bookableSlotCount != 0 &&  tempPosition != slotsModel.size()){

                                            slotStartTimes.add(slotsModel.get(tempPosition).getStart());
                                            tempPosition += 1;
                                            bookableSlotCount -= 1;

                                        }
                                        else
                                            break;

                                        if(tempPosition == slotsModel.size())
                                            break;
                                        Log.e("SlotsAdapter", "tempPosition: " + tempPosition);
                                    }
                                    flagFront = true;

                                }else {

                                    if (position != 0){

                                        tempPosition = position-1;

                                        while(!slotsModel.get(tempPosition).isBooked() && slotsModel.get(tempPosition).isBookable()){

                                            if(bookableSlotCount != 0){

                                                slotStartTimes.add(slotsModel.get(tempPosition).getStart());
                                                tempPosition -= 1;
                                                bookableSlotCount -= 1;

                                            }else
                                                break;

                                            if (tempPosition < 0)
                                                break;

                                            Log.e("SlotsAdapter", "tempPosition: " + tempPosition);
                                        }


                                    }

                                    flagBack = true;

                                }

                            }
                            else
                                break;

                        }

                        if(bookableSlotCount!=0){

                            Toast.makeText(context, "This will not be a suitable slot selection for the duration you require!", Toast.LENGTH_LONG).show();

                        }else{

                            Collections.sort(slotStartTimes);

                            for (int i = 0; i < slotStartTimes.size(); i++){
                                Log.e("SlotsAdapter", "Time: " + CommonUtils.convertTimestampToTime(Long.parseLong(slotStartTimes.get(i).toString())));
                            }

                            moveToActivity(model, slotStartTimes.get(0).toString());
                        }

                    }else if(!model.isBookable()){
                        Toast.makeText(context, "This slot is no longer available for booking!",Toast.LENGTH_LONG).show();
                    }else if(model.isBookable() && model.isBooked()){

                        final String month, date ;

                        month = CommonUtils.convertMonth((selectedDate.get(Calendar.MONTH) + 1));
                        date = CommonUtils.convertDay(selectedDate.get(Calendar.DATE));

                        Intent intent = new Intent(context, TransactionDetailsActivity.class);
                        intent.putExtra("from", "SlotsAdapterBooked");
                        intent.putExtra("transactionType", model.getTransactionResult().getType());
                        intent.putExtra("lrNumber", model.getTransactionResult().getLrNumber());
                        intent.putExtra("transactionId", model.getTransactionId());
                        intent.putExtra("transporter", model.getTransactionResult().getTransporterName());
                        intent.putExtra("selectedDate", selectedDate.get(Calendar.YEAR) + "-" + month + "-" + date);
                        intent.putExtra("source", model.getTransactionResult().getSrcWarehouse().getName());
                        intent.putExtra("destination", model.getTransactionResult().getDestWarehouse().getName());
                        intent.putExtra("driver", model.getTransactionResult().getPhoneNumber().get(0).toString());
                        intent.putExtra("vehicle", model.getTransactionResult().getVehicleNumber());
                        intent.putExtra("currentStatus", model.getTransactionResult().getCurrentStatus().get(0).getStatus());
                        intent.putExtra("currentStatusTime", model.getTransactionResult().getCurrentStatus().get(0).getTime());
                        intent.putExtra("dockName", selectedDockName);
                        intent.putExtra("dockId", selectedDock);
                        intent.putExtra("slot", "" + model.getStart());
                        intent.putExtra("slotDuration", "" + (model.getDuration()/60)/1000);
                        intent.putExtra("selectedWarehouseId", selectedWarehouseId);
                        intent.putExtra("selectedWarehouseName", selectedWarehouseName);
                        intent.putExtra("slotSize", slotSize);

                        Log.e("SlotsAdapter","transa ID: " + model.getTransactionId());

                        context.startActivity(intent);

                    }

                }

//                Log.e("SlotsAdapter", "Inside onClick");
            }
        });

    }

    private void moveToActivity(SlotsModel model, final String slotStartTime) {


        final String month, date ;

        month = CommonUtils.convertMonth((selectedDate.get(Calendar.MONTH) + 1));
        date = CommonUtils.convertDay(selectedDate.get(Calendar.DATE));

        if(model.isBooked()){

            if(model.isBookable()){

                //send all details

                Intent intent = new Intent(context, TransactionDetailsActivity.class);
                intent.putExtra("from", "SlotsAdapterBooked");
                intent.putExtra("transactionType", model.getTransactionResult().getType());
                intent.putExtra("lrNumber", model.getTransactionResult().getLrNumber());
                intent.putExtra("transactionId", model.getTransactionId());
                intent.putExtra("transporter", model.getTransactionResult().getTransporterName());
                intent.putExtra("selectedDate", selectedDate.get(Calendar.YEAR) + "-" + month + "-" + date);
                intent.putExtra("source", model.getTransactionResult().getSrcWarehouse().getName());
                intent.putExtra("destination", model.getTransactionResult().getDestWarehouse().getName());
                intent.putExtra("driver", model.getTransactionResult().getPhoneNumber().get(0).toString());
                intent.putExtra("vehicle", model.getTransactionResult().getVehicleNumber());
                intent.putExtra("currentStatus", model.getTransactionResult().getCurrentStatus().get(0).getStatus());
                intent.putExtra("currentStatusTime", model.getTransactionResult().getCurrentStatus().get(0).getTime());
                intent.putExtra("dockName", selectedDockName);
                intent.putExtra("dockId", selectedDock);
                intent.putExtra("slot", "" + model.getStart());
                intent.putExtra("slotDuration", "" + (model.getDuration()/60)/1000);
                intent.putExtra("selectedWarehouseId", selectedWarehouseId);
                intent.putExtra("selectedWarehouseName", selectedWarehouseName);
                intent.putExtra("slotSize", slotSize);

                context.startActivity(intent);

            }else{

                //send all details

                Intent intent = new Intent(context, TransactionDetailsActivity.class);
                intent.putExtra("from", "SlotsAdapterBookedExpired");
                intent.putExtra("transactionType", model.getTransactionResult().getType());
                intent.putExtra("lrNumber", model.getTransactionResult().getLrNumber());
                intent.putExtra("transactionId", model.getTransactionId());
                intent.putExtra("transporter", model.getTransactionResult().getTransporterName());
                intent.putExtra("selectedDate", selectedDate.get(Calendar.YEAR) + "-" + month + "-" + date);
                intent.putExtra("source", model.getTransactionResult().getSrcWarehouse().getName());
                intent.putExtra("destination", model.getTransactionResult().getDestWarehouse().getName());
                intent.putExtra("driver", model.getTransactionResult().getPhoneNumber().get(0).toString());
                intent.putExtra("vehicle", model.getTransactionResult().getVehicleNumber());
                intent.putExtra("currentStatus", model.getTransactionResult().getCurrentStatus().get(0).getStatus());
                intent.putExtra("currentStatusTime", model.getTransactionResult().getCurrentStatus().get(0).getTime());
                intent.putExtra("dockName", selectedDockName);
                intent.putExtra("dockId", selectedDock);
                intent.putExtra("slot", "" + model.getStart());
                intent.putExtra("slotDuration", "" + (model.getDuration()/60)/1000);
                intent.putExtra("selectedWarehouseId", selectedWarehouseId);
                intent.putExtra("selectedWarehouseName", selectedWarehouseName);
                context.startActivity(intent);

            }

        }else{

            if(model.isBookable()){

                IntuApplication.getApiUtility().TransactionsData(context, true, selectedWarehouseId, new APIUtility.APIResponseListener<TransactionsModel>() {
                    @Override
                    public void onReceiveResponse(final TransactionsModel response) {

                        int count = 0;

                        for(int i = 0; i < response.getResult().size(); i++){

                            if (!response.getResult().get(i).isAssigned()){

                                if(!response.getResult().get(i).getCurrentStatus().get(0).getStatus().equals("REJECTED")){
                                    if(!response.getResult().get(i).getCurrentStatus().get(0).getStatus().equals("COMPLETED")){
                                        Log.e("TAG", "current status: " + response.getResult().get(i).getCurrentStatus().get(0).getStatus());
                                        count += 1;
                                    }
                                }
                            }

                        }

                        if(count > 0){

                            Intent intent = new Intent(context, SelectTransactionActivity.class);
                            intent.putExtra("dockId", selectedDock);
                            intent.putExtra("from", "slotsAdapter");
                            intent.putExtra("selectedWarehouseId", selectedWarehouseId);
                            intent.putExtra("slotDuration", "" + selectedSlotDuration);
                            intent.putExtra("slotStartTime", slotStartTime);
                            intent.putExtra("selectedDate", selectedDate.get(Calendar.YEAR) + "-" + month + "-" + date);
                            intent.putExtra("slotSize", slotSize);
                            context.startActivity(intent);

                        }else{
                            Toast.makeText(context,"There are no transactions available to link the slot!",Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}