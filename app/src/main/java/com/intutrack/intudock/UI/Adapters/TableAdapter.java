package com.intutrack.intudock.UI.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.ViewHolderImpl;
import com.intutrack.intudock.Models.Docks.DocksModel;
import com.intutrack.intudock.Models.Docks.DocksResult;
import com.intutrack.intudock.Models.SlotsData.SlotsModel;
import com.intutrack.intudock.Models.Transaction.TransactionsResult;
import com.intutrack.intudock.Models.TransactionStatus.TransactionsStatusResultModel;
import com.intutrack.intudock.R;
import com.intutrack.intudock.Utilities.CommonUtils;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.WINDOW_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TableAdapter extends LinkedAdaptiveTableAdapter<ViewHolderImpl> {
    private final LayoutInflater mLayoutInflater;
    private Context context;
    private ArrayList<DocksResult> docksList;
    private String activityName;
    private ArrayList timeList;
    private Calendar selectedDate;
    private int pos;
    private Resources res;

    public TableAdapter(Context context, String activityName, ArrayList<DocksResult> docksList, Calendar selectedDate) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.activityName = activityName;
        this.docksList = docksList;
        this.selectedDate = selectedDate;

        this.timeList = new ArrayList();

        this.pos = 0;

        this.res = context.getResources();


        this.timeList.add("08:00 am");
        this.timeList.add("08:30 am");
        this.timeList.add("09:00 am");
        this.timeList.add("09:30 am");
        this.timeList.add("10:00 am");
        this.timeList.add("10:30 am");
        this.timeList.add("11:00 am");
        this.timeList.add("11:30 am");
        this.timeList.add("12:00 pm");
        this.timeList.add("12:30 pm");
        this.timeList.add("01:00 pm");
        this.timeList.add("01:30 pm");
        this.timeList.add("02:00 pm");
        this.timeList.add("02:30 pm");
        this.timeList.add("03:00 pm");
        this.timeList.add("03:30 pm");
        this.timeList.add("04:00 pm");
        this.timeList.add("04:30 pm");
        this.timeList.add("05:00 pm");
        this.timeList.add("05:30 pm");
        this.timeList.add("06:00 pm");
        this.timeList.add("06:30 pm");
        this.timeList.add("07:00 pm");
        this.timeList.add("07:30 pm");

    }

    @Override
    public int getRowCount() {
        ArrayList<DocksResult> model = docksList;
        return (model.size()+1);
    }

    @Override
    public int getColumnCount() {
        return 25;
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateItemViewHolder(@NonNull ViewGroup parent) {
        return new CellViewHolder(mLayoutInflater.inflate(R.layout.item_cell, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_header, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateRowHeaderViewHolder(@NonNull ViewGroup parent) {
        return new RowViewHolder(mLayoutInflater.inflate(R.layout.item_row_header, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateLeftTopHeaderViewHolder(@NonNull ViewGroup parent) {
        return new TopLeftViewHolder(mLayoutInflater.inflate(R.layout.item_top_left_header, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImpl viewHolder, final int row, final int column) {

        final CellViewHolder holder = (CellViewHolder) viewHolder;

        //        Log.e("TableAdapter", "text: " + vh.tvText.getText().toString());

        TransactionsResult model = docksList.get(row-1).getSlots().get(column-1).getTransactionResult();
        SlotsModel slotModel = docksList.get(row-1).getSlots().get(column-1);

        if (slotModel.isBookable()){

            if (slotModel.isBooked()){

                holder.rlNoData.setVisibility(GONE);
                holder.rlNoDataExpired.setVisibility(GONE);
                holder.rlData.setVisibility(VISIBLE);

                holder.transactionType.setBackgroundColor(context.getResources().getColor(R.color.purple));
                holder.lrNumber.setTextColor(context.getResources().getColor(R.color.purple));
                holder.textVehicleNumber.setTextColor(context.getResources().getColor(R.color.purple));

                holder.lrNumber.setText(model.getLrNumber());
                holder.transactionType.setText(model.getType());
                holder.vehicleNumber.setText(model.getVehicleNumber());

            }else{

                holder.rlNoData.setVisibility(VISIBLE);
                holder.rlNoDataExpired.setVisibility(GONE);
                holder.rlData.setVisibility(GONE);

            }

        }else{

            if (slotModel.isBooked()){

                holder.rlNoData.setVisibility(GONE);
                holder.rlNoDataExpired.setVisibility(GONE);
                holder.rlData.setVisibility(VISIBLE);

                holder.transactionType.setBackgroundColor(context.getResources().getColor(R.color.red_booked_slot));
                holder.lrNumber.setTextColor(context.getResources().getColor(R.color.red_booked_slot));
                holder.textVehicleNumber.setTextColor(context.getResources().getColor(R.color.red_booked_slot));

                holder.lrNumber.setText(model.getLrNumber());
                holder.transactionType.setText(model.getType());
                holder.vehicleNumber.setText(model.getVehicleNumber());

            }else{

                holder.rlNoData.setVisibility(GONE);
                holder.rlNoDataExpired.setVisibility(VISIBLE);
                holder.rlData.setVisibility(GONE);

            }

        }

    }

    @Override
    public void onBindHeaderColumnViewHolder(@NonNull ViewHolderImpl viewHolder, int column) {

        final ViewHolder vh = (ViewHolder) viewHolder;

        vh.tvText.setText(timeList.get(column-1).toString());

//        Log.e("TableAdapter", "text: " + vh.tvText.getText().toString());

    }

    @Override
    public void onBindHeaderRowViewHolder(@NonNull ViewHolderImpl viewHolder, int row) {

        RowViewHolder vh = (RowViewHolder) viewHolder;

        DocksResult model = docksList.get(row-1);

        vh.dockName.setText(model.getDockName());

        Calendar cal = Calendar.getInstance();

        String stringTodayDate = "";
        String stringSelectedDate = "";

        stringTodayDate = cal.get(Calendar.YEAR) + "-" + CommonUtils.convertMonth((cal.get(Calendar.MONTH))+1) + "-" +
                CommonUtils.convertMonth(cal.get(Calendar.DATE));

        stringSelectedDate = selectedDate.get(Calendar.YEAR) + "-" + CommonUtils.convertMonth((selectedDate.get(Calendar.MONTH))+1) + "-" +
                CommonUtils.convertMonth(selectedDate.get(Calendar.DATE));

        boolean flagOccupied = false;

        if(stringSelectedDate.equals(stringTodayDate)){

            String transType = "", strtTime = "";

            for(int i = 0; i < model.getSlots().size(); i++){

                if(model.getSlots().get(i).isBookable()){

                    if(model.getSlots().get(i-1).isBooked()){

                        flagOccupied = true;
                        transType = model.getSlots().get(i-1).getTransactionResult().getType();
                        strtTime = CommonUtils.convertTimestampToTime(model.getSlots().get(i-1).getStart());

                    }
                    else{
                        flagOccupied = false;
                    }
                    i = model.getSlots().size();
                }

            }

            if(flagOccupied){

                vh.linearLayoutDockData.setVisibility(VISIBLE);
                vh.transactionType.setText(transType);
                vh.startTime.setText(" " + strtTime);

            }
            else{
                vh.linearLayoutDockData.setVisibility(GONE);
            }

        }else{

            vh.linearLayoutDockData.setVisibility(GONE);

        }

    }

    @Override
    public void onBindLeftTopHeaderViewHolder(@NonNull ViewHolderImpl viewHolder) {

        TopLeftViewHolder holder = (TopLeftViewHolder) viewHolder;

        if(activityName.equals("CalendarActivity"))
            holder.linearLayoutHeader.setVisibility(GONE);
        else
            holder.linearLayoutHeader.setVisibility(VISIBLE);

    }

    @Override
    public int getColumnWidth(int column) {

        return res.getDimensionPixelSize(R.dimen.column_width);
    }

    @Override
    public int getHeaderColumnHeight() {
        return res.getDimensionPixelSize(R.dimen.column_header_height);
    }

    @Override
    public int getRowHeight(int row) {
        return res.getDimensionPixelSize(R.dimen.row_height);
    }

    @Override
    public int getHeaderRowWidth() {
        return res.getDimensionPixelSize(R.dimen.row_header_width);
    }

    private static class ViewHolder extends ViewHolderImpl {
        TextView tvText;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvText = itemView.findViewById(R.id.tv_header);

        }
    }

    private static class RowViewHolder extends ViewHolderImpl {
        TextView dockName, transactionType, startTime;
        LinearLayout linearLayoutDockData;

        private RowViewHolder(@NonNull View itemView) {
            super(itemView);

            dockName = itemView.findViewById(R.id.tv_dock_name);
            transactionType = itemView.findViewById(R.id.tv_transaction_type);
            startTime = itemView.findViewById(R.id.tv_start_time);

            linearLayoutDockData = itemView.findViewById(R.id.ll_dock_status);

        }
    }

    private static class CellViewHolder extends ViewHolderImpl {
        RelativeLayout rlData, rlNoData, rlNoDataExpired;
        TextView lrNumber, transactionType, vehicleNumber, textVehicleNumber;

        private CellViewHolder(@NonNull View itemView) {
            super(itemView);

            rlData = itemView.findViewById(R.id.rl_data);
            rlNoData = itemView.findViewById(R.id.rl_no_data);
            rlNoDataExpired = itemView.findViewById(R.id.rl_no_data_expired);

            lrNumber = itemView.findViewById(R.id.tv_lr_number);
            transactionType = itemView.findViewById(R.id.tv_transaction_type);
            vehicleNumber = itemView.findViewById(R.id.tv_vehicle_number);
            textVehicleNumber = itemView.findViewById(R.id.tv_text_vehicle_no);

        }
    }

    private static class TopLeftViewHolder extends ViewHolderImpl {
        LinearLayout linearLayoutHeader;

        private TopLeftViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayoutHeader = itemView.findViewById(R.id.ll_header);

        }
    }

}
