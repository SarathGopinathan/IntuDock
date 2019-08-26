package com.intutrack.intudock.UI.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.intutrack.intudock.Models.CancelTransaction.CancelTransactionResponse;
import com.intutrack.intudock.Models.Transaction.TransactionsResult;
import com.intutrack.intudock.Models.TransactionStatus.TransactionStatusRequest;
import com.intutrack.intudock.Models.TransactionStatus.TransactionsStatusModel;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Retrofit.Helper.APIUtility;
import com.intutrack.intudock.UI.AddTransactionActivity;
import com.intutrack.intudock.UI.IntuApplication;
import com.intutrack.intudock.Utilities.CommonUtils;
import java.util.ArrayList;
import java.util.Timer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.MyViewHolder> {

    private ArrayList<TransactionsResult> list;
    private Context context;
    private TransactionsAdapterCallback callback;
    private String selectedDate;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView id, dockName, slot, date, source, destination, vehicle, driverNumber, transporterName, eta, timer, transactionType;
        private Spinner currentStatus;
        private LinearLayout linearLayoutSchedule, linearLayoutCancel, linearLayoutScheduleCancel, linearLayoutTransporterData,
                linearLayoutTimer;
        private RelativeLayout relativeLayoutEditTransaction;
        private ImageButton moreDetails;
        private int counter = 0;

        private Timer updateTimer;

        public MyViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.tv_transaction_id);
            dockName = itemView.findViewById(R.id.tv_dock_name);
            slot = itemView.findViewById(R.id.tv_slot);
            date = itemView.findViewById(R.id.tv_date);
            source = itemView.findViewById(R.id.tv_source);
            destination = itemView.findViewById(R.id.tv_destination);
            vehicle = itemView.findViewById(R.id.tv_vehicle);
            driverNumber = itemView.findViewById(R.id.tv_driver_number);
            transporterName = itemView.findViewById(R.id.tv_transporter_name);
            eta = itemView.findViewById(R.id.tv_eta);
            timer = itemView.findViewById(R.id.tv_timer);
            transactionType = itemView.findViewById(R.id.tv_transaction_type);

            currentStatus = itemView.findViewById(R.id.spinner_current_status);

            linearLayoutSchedule = itemView.findViewById(R.id.ll_schedule);
            linearLayoutCancel = itemView.findViewById(R.id.ll_cancel);
            linearLayoutScheduleCancel = itemView.findViewById(R.id.ll_schedule_cancel);
            linearLayoutTransporterData = itemView.findViewById(R.id.ll_transporter_data);
            linearLayoutTimer = itemView.findViewById(R.id.ll_time);

            moreDetails = itemView.findViewById(R.id.ib_more_details);

            relativeLayoutEditTransaction = itemView.findViewById(R.id.rl_edit_transaction);

        }
    }



    public TransactionsAdapter(ArrayList<TransactionsResult> list, Context mContext, Fragment fragment, String selectedDate){
        this.list = list;
        this.context = mContext;
        this.callback = (TransactionsAdapterCallback) fragment;
        this.selectedDate = selectedDate;
    }

    @Override
    public TransactionsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transactions,parent,false);
        return new TransactionsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TransactionsAdapter.MyViewHolder holder, int position) {

        final TransactionsResult model = list.get(position);

        holder.linearLayoutScheduleCancel.setVisibility(GONE);
        holder.linearLayoutTransporterData.setVisibility(GONE);
        holder.linearLayoutTimer.setVisibility(GONE);

        holder.id.setText(model.getLrNumber());
        try{
            holder.dockName.setText(model.getDockDetails().getObj().getDockName());
        }catch(Exception e){
            holder.dockName.setText("NA");
        }

        if(model.isAssigned()){

            try{
                holder.linearLayoutSchedule.setVisibility(VISIBLE);
                holder.linearLayoutCancel.setVisibility(VISIBLE);
                holder.slot.setText(CommonUtils.convertTimestampToTime(model.getDockDetails().getStart()));
            }catch (Exception e){
                holder.linearLayoutSchedule.setVisibility(VISIBLE);
                holder.linearLayoutCancel.setVisibility(VISIBLE);
                holder.slot.setText("NA");
            }

        }else{
            holder.linearLayoutSchedule.setVisibility(VISIBLE);
            holder.linearLayoutCancel.setVisibility(GONE);
            holder.slot.setText("NA");
        }
        holder.date.setText(CommonUtils.convertTimestampToDate(model.getDate()));
        holder.source.setText(model.getSrcWarehouse().getName());
        holder.destination.setText(model.getDestWarehouse().getName());
        holder.vehicle.setText(model.getVehicleNumber());
        holder.transactionType.setText(model.getType());
        holder.driverNumber.setText("" + model.getPhoneNumber().get(0));
        holder.transporterName.setText(model.getTransporterName());
//        holder.eta.setText(model.getEta());
//        holder.timer.setText(model.getTimer());
        holder.transporterName.setText(model.getTransporterName());

        final ArrayList currentStatusList = new ArrayList();

        for(int i = 0; i < model.getCurrentStatus().size(); i++)
            currentStatusList.add(model.getCurrentStatus().get(i).getStatus());

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context,   R.layout.item_spinner_status_content, currentStatusList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.item_status_spinner); // The drop down view
        holder.currentStatus.setAdapter(spinnerArrayAdapter);

        if (holder.currentStatus.getSelectedItem().toString().equals("COMPLETED") ||
                holder.currentStatus.getSelectedItem().toString().equals("REJECTED")){

            holder.relativeLayoutEditTransaction.setVisibility(GONE);

        }
        else{

            holder.relativeLayoutEditTransaction.setVisibility(VISIBLE);

        }

        holder.currentStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(holder.currentStatus.getSelectedItemPosition() == 0){

                }else{

                    TransactionStatusRequest request = new TransactionStatusRequest();

                    request.setTransactionId(model.getTransactionId());
                    request.setNewStatus(holder.currentStatus.getSelectedItem().toString());

                    IntuApplication.getApiUtility().TransactionStatusChange(context, true, request, new APIUtility.APIResponseListener<TransactionsStatusModel>() {
                        @Override
                        public void onReceiveResponse(TransactionsStatusModel response) {

                            final ArrayList tempStatusList = new ArrayList();

                            for(int i = 0; i < response.getResult().get(0).getCurrentStatus().size(); i++)
                                tempStatusList.add(response.getResult().get(0).getCurrentStatus().get(i).getStatus());

                            currentStatusList.clear();
                            currentStatusList.addAll(tempStatusList);

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context,   R.layout.item_spinner_status_content, currentStatusList);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.item_status_spinner); // The drop down view
                            holder.currentStatus.setAdapter(spinnerArrayAdapter);

                            if (tempStatusList.get(0).toString().equals("COMPLETED") || tempStatusList.get(0).toString().equals("REJECTED")){

                                holder.linearLayoutScheduleCancel.setVisibility(GONE);
                                holder.relativeLayoutEditTransaction.setVisibility(GONE);

                            }

                            callback.onStatusChange();

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        holder.moreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.counter == 0){

                    holder.linearLayoutScheduleCancel.setVisibility(View.VISIBLE);
                    holder.linearLayoutTransporterData.setVisibility(View.VISIBLE);

                    holder.counter = 1;

                    holder.moreDetails.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_more_up_arrow));

                    if(holder.currentStatus.getSelectedItem().toString().equals("AT DOCK")){
                        holder.linearLayoutTimer.setVisibility(View.VISIBLE);

                        if(holder.updateTimer != null){
                            holder.updateTimer.cancel();
                        }else{

                        }
                    }
                    if (holder.currentStatus.getSelectedItem().toString().equals("COMPLETED") ||
                            holder.currentStatus.getSelectedItem().toString().equals("REJECTED")){

                        holder.linearLayoutScheduleCancel.setVisibility(GONE);

                    }

                }
                else{

                    holder.linearLayoutScheduleCancel.setVisibility(GONE);
                    holder.linearLayoutTransporterData.setVisibility(GONE);
                    holder.linearLayoutTimer.setVisibility(GONE);

                    holder.counter = 0;

                    holder.moreDetails.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_more_down_arrow));

                }

            }
        });

        if(holder.currentStatus.getSelectedItemPosition() >= 2 )
            holder.linearLayoutTimer.setVisibility(View.VISIBLE);
        else
            holder.linearLayoutTimer.setVisibility(View.GONE);

        holder.linearLayoutSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callback.OnScheduleClick("TransactionsAdapterSchedule", model.getDestWarehouse().getId(), model.getDestWarehouse().getName(),
                        model.getType(), model.getTransactionId(), model.getLrNumber(), model.getTransporterName(), model.getSrcWarehouse().getName(),
                        model.getDestWarehouse().getName(), model.getPhoneNumber().get(0).toString(), model.getVehicleNumber(),
                        model.getCurrentStatus().get(0).getStatus(), model.getCurrentStatus().get(0).getTime());

            }
        });

        holder.linearLayoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);

                dialog.setContentView(R.layout.dialog_cancel_assignment);

                Button yes = dialog.findViewById(R.id.btn_yes);
                Button no = dialog.findViewById(R.id.btn_no);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //call cancel api
                        IntuApplication.getApiUtility().CancelTransaction(context, true, model.getTransactionId(), new APIUtility.APIResponseListener<CancelTransactionResponse>() {
                            @Override
                            public void onReceiveResponse(CancelTransactionResponse response) {
                                callback.onCancelClick();
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

        holder.relativeLayoutEditTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //send all details via intent

                Intent intent = new Intent(context, AddTransactionActivity.class);

                intent.putExtra("from", "TransactionsAdapterEdit");
                intent.putExtra("selectedWarehouseId", model.getDestWarehouse().getId());
                intent.putExtra("selectedWarehouseName", model.getDestWarehouse().getName());
                intent.putExtra("transactionType", model.getType());
                intent.putExtra("transactionId", model.getTransactionId());
                intent.putExtra("lrNumber", model.getLrNumber());
                intent.putExtra("transporter", model.getTransporterName());
                intent.putExtra("selectedDate", selectedDate);
                intent.putExtra("source", model.getSrcWarehouse().getName());
                intent.putExtra("destination", model.getDestWarehouse().getName());
                intent.putExtra("driver", model.getPhoneNumber().get(0).toString());
                intent.putExtra("vehicle", model.getVehicleNumber());
                intent.putExtra("currentStatus", model.getCurrentStatus().get(0).getStatus());
                intent.putExtra("currentStatusTime", model.getCurrentStatus().get(0).getTime());

                try{
                    if(model.isAssigned()){
                        intent.putExtra("dockName", model.getDockDetails().getObj().getDockName());
                    }else{
                        intent.putExtra("dockName", "");
                    }
                }catch (Exception e){
                    intent.putExtra("dockName", "");
                }

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface TransactionsAdapterCallback {
        void onCancelClick();

        void onStatusChange();

        void OnScheduleClick(String transactionsAdapterSchedule, String id, String name, String type, String transactionId, String lrNumber, String transporterName, String name1, String name2, String toString, String vehicleNumber, String status, long time);
    }

}