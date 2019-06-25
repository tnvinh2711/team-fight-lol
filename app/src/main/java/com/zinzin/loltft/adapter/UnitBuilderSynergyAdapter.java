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
import com.zinzin.loltft.R;
import com.zinzin.loltft.model.UnitsInfo;

import java.util.ArrayList;
import java.util.List;

public class UnitBuilderSynergyAdapter extends RecyclerView.Adapter<UnitBuilderSynergyAdapter.ViewHolder> {

    private Activity activity;
    private List<UnitsInfo> infos = new ArrayList<>();

    public UnitBuilderSynergyAdapter(Activity activity, List<UnitsInfo> infos) {
        this.activity = activity;
        this.infos = infos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.detail_units_items, viewGroup, false);
        return new UnitBuilderSynergyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(infos.get(position), position);
        UnitsInfo unitsInfo = infos.get(position);
        Glide.with(activity).load(unitsInfo.getImgInfo()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivIcon);

        viewHolder.tvName.setText(unitsInfo.getName() + " - " + unitsInfo.getCount());
        viewHolder.tvType.setText(unitsInfo.getType());
        if (unitsInfo.getDes().equals("")) {
            viewHolder.tvDes.setVisibility(View.GONE);
        } else {
            viewHolder.tvDes.setVisibility(View.VISIBLE);
        }

        viewHolder.tvDes.setText(unitsInfo.getDes());
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvType;
        TextView tvDes;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvType = itemView.findViewById(R.id.tv_type);
            tvDes = itemView.findViewById(R.id.tv_des);
        }

        void bind(final UnitsInfo item, final int position) {
        }
    }
}
