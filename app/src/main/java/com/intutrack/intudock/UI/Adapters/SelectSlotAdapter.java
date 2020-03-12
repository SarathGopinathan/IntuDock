package com.intutrack.intudock.UI.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.intutrack.intudock.Models.NavigationModel;
import com.intutrack.intudock.Models.SlotsData.SlotsModel;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Utilities.CommonUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class SelectSlotAdapter extends RecyclerView.Adapter<SelectSlotAdapter.MyViewHolder> {

    private ArrayList<SlotsModel> list;
    private Context context;
    private SelectSlotAdapterCallback callback;
    private int flag = 0;
    private int duration;
    private ArrayList selectedSlots;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView slotTime;
        private LinearLayout linearLayoutSlot;

        public MyViewHolder(View itemView) {
            super(itemView);

            slotTime = itemView.findViewById(R.id.tv_slot);
            linearLayoutSlot = itemView.findViewById(R.id.ll_slot);

        }
    }

    public SelectSlotAdapter(ArrayList<SlotsModel> list, Context mContext, int duration, ArrayList selectedSlots){

        this.list = list;
        this.context = mContext;
        this.callback = (SelectSlotAdapterCallback) mContext;
        this.duration = duration;
        this.selectedSlots = selectedSlots;

    }

    @Override
    public SelectSlotAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot,parent,false);
        return new SelectSlotAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

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

        try{
            for(int i = 0; i < selectedSlots.size(); i++){

                if(model.getStart() == Long.parseLong(selectedSlots.get(i).toString())){

                    holder.linearLayoutSlot.setBackground(context.getResources().getDrawable(R.drawable.rounded_rect_purple));
                    holder.slotTime.setTextColor(context.getResources().getColor(R.color.white));

                }

            }
        }catch(Exception e){

        }

        holder.slotTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(list.get(position).isBookable()) {

                    handleOnClick(model, position);

                }else{

                    Snackbar snackbar = Snackbar
                            .make(view, "This slot is currently unavailable!", Snackbar.LENGTH_LONG);

                    snackbar.show();

//                    Toast.makeText(context,"This slot is currently unavailable!",Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    private void handleOnClick(SlotsModel model, final int position) {

        if(model.isBookable()){

            int bookableSlotCount = duration/30;
            int tempPosition = position;
            boolean flagFront = false;
            boolean flagBack = false;
            ArrayList slotStartTimes = new ArrayList();
            ArrayList<SlotsModel> slotsModel = list;

            if(model.isBookable()){

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
//                                Log.e("SlotsAdapter", "tempPosition: " + tempPosition);
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

                    callback.onSlotClick(slotStartTimes);
                }

            }else{
                Toast.makeText(context, "This slot is no longer available for booking!",Toast.LENGTH_LONG).show();
            }

        }else{
            callback.onSlotClick(new ArrayList());
            Toast.makeText(context, "This slot is no longer available for booking!",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface SelectSlotAdapterCallback {
        void onSlotClick(ArrayList slotStartTimes);
    }


}