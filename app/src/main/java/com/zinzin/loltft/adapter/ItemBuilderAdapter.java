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
import com.zinzin.loltft.model.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilderAdapter extends RecyclerView.Adapter<ItemBuilderAdapter.ViewHolder> {

    private Activity activity;
    private List<ItemBuilder> itemBuilders = new ArrayList<>();

    public ItemBuilderAdapter(Activity activity, List<ItemBuilder> combineList) {
        this.activity = activity;
        this.itemBuilders = combineList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_builder_item, viewGroup, false);
        return new ItemBuilderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(itemBuilders.get(position), position);
        Glide.with(activity).load(itemBuilders.get(position).getListItem().get(0)).apply(RequestOptions.bitmapTransform(new RoundedCorners(14))).into(viewHolder.ivIcon);
        Glide.with(activity).load(itemBuilders.get(position).getListItem().get(1)).apply(RequestOptions.bitmapTransform(new RoundedCorners(14))).into(viewHolder.ivCombine1);
        Glide.with(activity).load(itemBuilders.get(position).getListItem().get(2)).apply(RequestOptions.bitmapTransform(new RoundedCorners(14))).into(viewHolder.ivCombine2);
    }

    @Override
    public int getItemCount() {
        return itemBuilders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        ImageView ivCombine1;
        ImageView ivCombine2;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_item);
            ivCombine1 = itemView.findViewById(R.id.iv_combine_1);
            ivCombine2 = itemView.findViewById(R.id.iv_combine_2);
        }

        void bind(final ItemBuilder combine, final int position) {
        }
    }
}
