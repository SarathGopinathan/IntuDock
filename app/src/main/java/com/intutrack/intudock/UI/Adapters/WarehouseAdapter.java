package com.intutrack.intudock.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.intutrack.intudock.Models.Warehouse.WarehouseResult;
import com.intutrack.intudock.R;

import java.util.ArrayList;

public class WarehouseAdapter extends RecyclerView.Adapter<WarehouseAdapter.MyViewHolder> {

    private ArrayList<WarehouseResult> list;
    private Context context;
    private WarehouseAdapterCallback callback;


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name, location, totalDocks, assignedTransactions, openTransactions;
        private LinearLayout linearLayoutWarehouse;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_warehouse_name);
            location = itemView.findViewById(R.id.tv_location);
            totalDocks = itemView.findViewById(R.id.tv_total_docks);
            assignedTransactions = itemView.findViewById(R.id.tv_assigned_transactions);
            openTransactions = itemView.findViewById(R.id.tv_open_transactions);

            linearLayoutWarehouse = itemView.findViewById(R.id.ll_warehouse);

        }
    }

    public WarehouseAdapter(ArrayList<WarehouseResult> list, Context mContext, Fragment fragment){
        this.list = list;
        this.context = mContext;
        callback = (WarehouseAdapterCallback) fragment;
    }

    @Override
    public WarehouseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_warehouse,parent,false);
        return new WarehouseAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WarehouseAdapter.MyViewHolder holder, final int position) {

        WarehouseResult model = list.get(position);

        holder.name.setText(model.getWarehouseName());
        holder.location.setText(model.getCity());
        holder.totalDocks.setText(String.valueOf(model.getTotalDocks()));
        holder.assignedTransactions.setText(String.valueOf(model.getAssignedTransactions()));
        holder.openTransactions.setText(String.valueOf(model.getOpenTransactions()));

        holder.linearLayoutWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callback.onWarehouseClick(list.get(position).getCity(), list.get(position).getWarehouseName(),
                        list.get(position).getWarehouseId());

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface WarehouseAdapterCallback {
        void onWarehouseClick(String city, String warehouseName, String warehouseId);
    }


}
