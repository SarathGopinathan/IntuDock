package com.intutrack.intudock.UI.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.intutrack.intudock.Models.Docks.DocksResult;
import com.intutrack.intudock.Models.SlotsData.SlotsModel;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Utilities.CommonUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class DocksAdapter extends RecyclerView.Adapter<DocksAdapter.MyViewHolder> {

    private ArrayList<DocksResult> list;
    private Context context;
    private SlotsModel slotsModel;
    private SlotsAdapter adapter;
    private int selectedSlotDuration;
    private Calendar selectedDate;
    private String selectedWarehouseId, selectedWarehouseName;
    private DocksAdapterCallback callback;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView dockName, bookedSlots, availableSlots, textBookedSlots, textAvailableSlots,
                transactionType, startTime, status, vehicleNumber;
        private RecyclerView recyclerViewSlots;
        private RelativeLayout relativeLayoutTimer;
        private LinearLayout linearLayoutDockStatus, linearLayoutDockData;

        public MyViewHolder(View itemView) {
            super(itemView);

            dockName = itemView.findViewById(R.id.tv_dock_name);
            bookedSlots = itemView.findViewById(R.id.tv_booked_slots);
            availableSlots = itemView.findViewById(R.id.tv_available_slots);
            textBookedSlots = itemView.findViewById(R.id.tv_text_booked_slots);
            textAvailableSlots = itemView.findViewById(R.id.tv_text_available_slots);
            transactionType = itemView.findViewById(R.id.tv_transaction_type);
            startTime = itemView.findViewById(R.id.tv_start_time);
            status = itemView.findViewById(R.id.tv_status);
            vehicleNumber = itemView.findViewById(R.id.tv_vehicle_number);

            recyclerViewSlots = itemView.findViewById(R.id.rv_time_slots);

            relativeLayoutTimer = itemView.findViewById(R.id.rl_timer);

            linearLayoutDockStatus = itemView.findViewById(R.id.ll_dock_status);
            linearLayoutDockData = itemView.findViewById(R.id.ll_dock_data);
        }
    }

    public DocksAdapter(ArrayList<DocksResult> list, Context mContext, Fragment fragment, int selectedSlotDuration,
                        Calendar selectedDate, String selectedWarehouseId, String selectedWarehouseName){

        this.list = list;
        this.context = mContext;
        this.selectedSlotDuration = selectedSlotDuration;
        this.selectedDate = selectedDate;
        this.selectedWarehouseId = selectedWarehouseId;
        this.selectedWarehouseName = selectedWarehouseName;
        this.callback = (DocksAdapterCallback) fragment;

    }

    @Override
    public DocksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_docks,parent,false);
        return new DocksAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DocksAdapter.MyViewHolder holder, int position) {

        DocksResult model = list.get(position);

        Calendar cal = Calendar.getInstance();

        String stringTodayDate = "";
        String stringSelectedDate = "";

        stringTodayDate = cal.get(Calendar.YEAR) + "-" + CommonUtils.convertMonth(cal.get(Calendar.MONTH)) + "-" +
                CommonUtils.convertMonth(cal.get(Calendar.DATE));

        stringSelectedDate = selectedDate.get(Calendar.YEAR) + "-" + CommonUtils.convertMonth(selectedDate.get(Calendar.MONTH)) + "-" +
                CommonUtils.convertMonth(selectedDate.get(Calendar.DATE));

        boolean flagOccupied = false;

        if(stringSelectedDate.equals(stringTodayDate)){

            String transType = "", strtTime = "", lrNo = "", tempVehicleNumber = "";
            long strtTimeValue = 0;

            for(int i = 0; i < model.getSlots().size(); i++){

                if(i == model.getSlots().size()-1){

                    if(model.getSlots().get(i).isBooked()){

                        flagOccupied = true;
                        transType = model.getSlots().get(i).getTransactionResult().getType();
                        lrNo = model.getSlots().get(i).getTransactionResult().getLrNumber();
                        tempVehicleNumber = model.getSlots().get(i).getTransactionResult().getVehicleNumber();
                        strtTime = CommonUtils.convertTimestampToTime(model.getSlots().get(i).getStart());
                        strtTimeValue = model.getSlots().get(i).getStart();

                    }
                    else{
                        flagOccupied = false;
                    }

                }else if(i == 0){
                    if(model.getSlots().get(i).isBooked()){

                        if(System.currentTimeMillis() > model.getSlots().get(i).getStart() &&
                                System.currentTimeMillis() < model.getSlots().get(i+1).getStart()){
                            flagOccupied = true;
                            transType = model.getSlots().get(i).getTransactionResult().getType();
                            lrNo = model.getSlots().get(i).getTransactionResult().getLrNumber();
                            tempVehicleNumber = model.getSlots().get(i).getTransactionResult().getVehicleNumber();
                            strtTime = CommonUtils.convertTimestampToTime(model.getSlots().get(i).getStart());
                            strtTimeValue = model.getSlots().get(i).getStart();
                        }
                    }
                    else{
                        flagOccupied = false;
                    }
                }
                else{
                    if(model.getSlots().get(i).isBookable()){

                        if(model.getSlots().get(i-1).isBooked()){

                            flagOccupied = true;
                            transType = model.getSlots().get(i-1).getTransactionResult().getType();
                            tempVehicleNumber = model.getSlots().get(i-1).getTransactionResult().getVehicleNumber();
                            lrNo = model.getSlots().get(i-1).getTransactionResult().getLrNumber();
                            strtTime = CommonUtils.convertTimestampToTime(model.getSlots().get(i-1).getStart());
                            strtTimeValue = model.getSlots().get(i-1).getStart();

                        }
                        else{
                            flagOccupied = false;
                        }
                        i = model.getSlots().size();
                    }
                }
            }

            if(flagOccupied){

                holder.linearLayoutDockStatus.setVisibility(View.VISIBLE);
                holder.linearLayoutDockData.setBackground(context.getResources().getDrawable(R.drawable.left_rounded_rect_purple_dock_occupied));

                holder.dockName.setTextColor(context.getResources().getColor(R.color.black));
                holder.textBookedSlots.setTextColor(context.getResources().getColor(R.color.black));
                holder.bookedSlots.setTextColor(context.getResources().getColor(R.color.black));
                holder.textAvailableSlots.setTextColor(context.getResources().getColor(R.color.black));
                holder.availableSlots.setTextColor(context.getResources().getColor(R.color.black));
                holder.transactionType.setTextColor(context.getResources().getColor(R.color.black));
                holder.status.setTextColor(context.getResources().getColor(R.color.black));
                holder.vehicleNumber.setTextColor(context.getResources().getColor(R.color.black));

                holder.dockName.setText(model.getDockName());
                holder.availableSlots.setText(model.getAvailableSlots());
                holder.bookedSlots.setText(model.getBookedSlots());

                holder.transactionType.setText(transType + "ING since ");
                holder.startTime.setText(strtTime);

                Log.e("TAG", "view vno: "+tempVehicleNumber);
                holder.status.setText("STATUS Occupied");
                holder.vehicleNumber.setText(tempVehicleNumber);

                final long tempStartTime = strtTimeValue;
                final String tempLrNo = lrNo, tempTransactionType = transType;

                holder.relativeLayoutTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        callback.OnTimerClick(tempStartTime, tempLrNo, tempTransactionType);

                    }
                });

            }
            else{

                holder.linearLayoutDockStatus.setVisibility(View.GONE);
                holder.linearLayoutDockData.setBackground(context.getResources().getDrawable(R.drawable.left_rounded_rect_purple));

                holder.dockName.setTextColor(context.getResources().getColor(R.color.white));
                holder.textBookedSlots.setTextColor(context.getResources().getColor(R.color.white));
                holder.bookedSlots.setTextColor(context.getResources().getColor(R.color.white));
                holder.textAvailableSlots.setTextColor(context.getResources().getColor(R.color.white));
                holder.availableSlots.setTextColor(context.getResources().getColor(R.color.white));

                holder.dockName.setText(model.getDockName());
                holder.availableSlots.setText(model.getAvailableSlots());
                holder.bookedSlots.setText(model.getBookedSlots());

                holder.status.setText("STATUS Unoccupied");
                holder.status.setTextColor(context.getResources().getColor(R.color.white));

            }

        }else{

            holder.linearLayoutDockStatus.setVisibility(View.GONE);
            holder.linearLayoutDockData.setBackground(context.getResources().getDrawable(R.drawable.left_rounded_rect_purple));

            holder.dockName.setTextColor(context.getResources().getColor(R.color.white));
            holder.textBookedSlots.setTextColor(context.getResources().getColor(R.color.white));
            holder.bookedSlots.setTextColor(context.getResources().getColor(R.color.white));
            holder.textAvailableSlots.setTextColor(context.getResources().getColor(R.color.white));
            holder.availableSlots.setTextColor(context.getResources().getColor(R.color.white));

            holder.dockName.setText(model.getDockName());
            holder.availableSlots.setText(model.getAvailableSlots());
            holder.bookedSlots.setText(model.getBookedSlots());

        }

        ArrayList<SlotsModel> slotList = new ArrayList<>();

        for(int i = 0; i < model.getSlots().size(); i++)
            slotList.add(model.getSlots().get(i));

        adapter = new SlotsAdapter(slotList, context, model.getDockId(), selectedSlotDuration, selectedDate, selectedWarehouseId,
                selectedWarehouseName, model.getDockName(), ""+model.getSlotSize());
        holder.recyclerViewSlots.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,3);
        holder.recyclerViewSlots.setLayoutManager(layoutManager);
        holder.recyclerViewSlots.setAdapter(adapter);
        holder.recyclerViewSlots.addOnItemTouchListener(scrollTouchListener);

    }

    RecyclerView.OnItemTouchListener scrollTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            int action = e.getAction();
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    rv.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface DocksAdapterCallback {
        void OnTimerClick(long startTime, String lrNumber, String transactionType);
    }
}