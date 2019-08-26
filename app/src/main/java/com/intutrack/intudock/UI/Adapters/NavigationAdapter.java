package com.intutrack.intudock.UI.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.intutrack.intudock.Models.NavigationModel;
import com.intutrack.intudock.R;

import java.util.ArrayList;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.MyViewHolder> {

    private ArrayList<NavigationModel> list;
    private Context context;
    private NavigationAdapterCallback callback;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title, text;
        private ImageView icon;
        private RelativeLayout menu;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            text = itemView.findViewById(R.id.tv_text);

            icon = itemView.findViewById(R.id.iv_icon);

            menu = itemView.findViewById(R.id.rl_menu);

        }
    }

    public NavigationAdapter(ArrayList<NavigationModel> list, Context mContext){
        this.list = list;
        this.context = mContext;
        callback = (NavigationAdapterCallback) mContext;
    }

    @Override
    public NavigationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu,parent,false);
        return new NavigationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NavigationAdapter.MyViewHolder holder, final int position) {

        NavigationModel model = list.get(position);

        holder.title.setText(model.getTitle());
        holder.text.setText(model.getText());

        holder.icon.setImageDrawable(context.getResources().getDrawable(model.getIcon()));

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callback.onMenuClick(list.get(position), position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface NavigationAdapterCallback {
        void onMenuClick(NavigationModel data, int pos);
    }

}
