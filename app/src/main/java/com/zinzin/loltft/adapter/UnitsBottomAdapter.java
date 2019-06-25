package com.zinzin.loltft.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;
import com.zinzin.loltft.R;
import com.zinzin.loltft.model.Unit;
import com.zinzin.loltft.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class UnitsBottomAdapter extends RecyclerView.Adapter<UnitsBottomAdapter.ViewHolder> {

    private Activity activity;
    private List<Unit> unitsList = new ArrayList<>();
    private OnItemClickListener listener;

    public UnitsBottomAdapter(Activity activity, List<Unit> unitsList) {
        this.activity = activity;
        this.unitsList = unitsList;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.units_item_bottom_sheet, viewGroup, false);
        return new UnitsBottomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(unitsList.get(position), position, listener);
        Unit units = unitsList.get(position);
        Glide.with(activity).load(units.getUrl()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivIconUnit);
        viewHolder.tvNameUnit.setText(units.getName());


        if(units.isClick()){
            Utils.setLocked(viewHolder.ivIconUnit);
            viewHolder.tvNameUnit.setTextColor(activity.getResources().getColor(R.color.gray));
        } else {
            Utils.setUnlocked(viewHolder.ivIconUnit);
            switch (units.getCost()) {
                case "$1":
                    viewHolder.tvNameUnit.setTextColor(activity.getResources().getColor(R.color.color_cost_1));
                    break;
                case "$2":
                    viewHolder.tvNameUnit.setTextColor(activity.getResources().getColor(R.color.color_cost_2));
                    break;
                case "$3":
                    viewHolder.tvNameUnit.setTextColor(activity.getResources().getColor(R.color.color_cost_3));
                    break;
                case "$4":
                    viewHolder.tvNameUnit.setTextColor(activity.getResources().getColor(R.color.color_cost_4));
                    break;
                case "$5":
                    viewHolder.tvNameUnit.setTextColor(activity.getResources().getColor(R.color.color_cost_5));
                    break;
            }
        }
    }
    public void updateList(List<Unit> list){
        unitsList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return unitsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIconUnit;
        TextView tvNameUnit;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIconUnit = itemView.findViewById(R.id.iv_icon_unit);
            tvNameUnit = itemView.findViewById(R.id.tv_name);
        }

        void bind(final Unit item, final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.setClickable(false);
                    if (listener != null)
                        listener.OnItemClick(item, position);
                    notifyDataSetChanged();
                }
            });
        }
    }


    public interface OnItemClickListener {
        void OnItemClick(Unit item, int position);
    }
}
