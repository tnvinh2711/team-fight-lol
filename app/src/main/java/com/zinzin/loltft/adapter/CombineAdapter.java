package com.zinzin.loltft.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zinzin.loltft.R;
import com.zinzin.loltft.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CombineAdapter extends RecyclerView.Adapter<CombineAdapter.ViewHolder> {

    private Activity activity;
    private List<String> combineList = new ArrayList<>();

    public CombineAdapter(Activity activity,List<String> combineList) {
        this.activity = activity;
        this.combineList = combineList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_combine, viewGroup, false);
        return new CombineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(combineList.get(position), position);
        Glide.with(activity).load(combineList.get(position)).apply(RequestOptions.bitmapTransform(new RoundedCorners(14))).into(viewHolder.ivIcon);
    }

    @Override
    public int getItemCount() {
        return combineList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_item_combine);
        }

        void bind(final String combine, final int position) {
        }
    }
}
