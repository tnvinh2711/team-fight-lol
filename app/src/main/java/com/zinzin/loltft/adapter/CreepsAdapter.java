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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zinzin.loltft.R;
import com.zinzin.loltft.model.Round;

import java.util.ArrayList;
import java.util.List;

public class CreepsAdapter extends RecyclerView.Adapter<CreepsAdapter.ViewHolder> {

    private Activity activity;
    private List<Round> creepList = new ArrayList<>();

    public CreepsAdapter(Activity activity, List<Round> creepList) {
        this.activity = activity;
        this.creepList = creepList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.creeps_items, viewGroup, false);
        return new CreepsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(creepList.get(position), position);
        Round creep = creepList.get(position);
        Glide.with(activity).load(creep.getUrl()).apply(RequestOptions.bitmapTransform(new RoundedCorners(14))).into(viewHolder.ivIcon);
        viewHolder.tvName.setText("Round: "+creep.getName());
        viewHolder.tvDes.setText(creep.getDes());
    }

    @Override
    public int getItemCount() {
        return creepList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvDes;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_items);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDes = itemView.findViewById(R.id.tv_des);
        }

        void bind(final Round item, final int position) {
        }
    }
}
