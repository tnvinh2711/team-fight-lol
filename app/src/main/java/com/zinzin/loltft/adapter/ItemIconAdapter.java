package com.zinzin.loltft.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zinzin.loltft.R;
import com.zinzin.loltft.model.Type;

import java.util.ArrayList;
import java.util.List;

public class ItemIconAdapter extends RecyclerView.Adapter<ItemIconAdapter.ViewHolder> {

    private Activity activity;
    private List<String> imgList = new ArrayList<>();

    public ItemIconAdapter(Activity activity, List<String> imgList) {
        this.activity = activity;
        this.imgList = imgList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_icon_item, viewGroup, false);
        return new ItemIconAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(imgList.get(position), position);
        Glide.with(activity).load(imgList.get(position)).into(viewHolder.ivIcon);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
        }

        void bind(final String item, final int position) {
        }
    }
}
