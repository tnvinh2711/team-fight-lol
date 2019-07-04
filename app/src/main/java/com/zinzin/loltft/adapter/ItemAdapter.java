package com.zinzin.loltft.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zinzin.loltft.R;
import com.zinzin.loltft.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Activity activity;
    private List<Item> itemList = new ArrayList<>();

    public ItemAdapter(Activity activity, List<Item> itemList) {
        this.activity = activity;
        this.itemList = itemList;
    }
    public void updateList(List<Item> list) {
        itemList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_items, viewGroup, false);
        return new ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(itemList.get(position), position);
        Item item = itemList.get(position);
        viewHolder.tvName.setText(item.getName());
        Glide.with(activity).load(item.getUrl()).apply(RequestOptions.bitmapTransform(new RoundedCorners(14))).into(viewHolder.ivIcon);
        viewHolder.tvDes.setText(item.getDes());
        if(item.getListCombine()!= null && item.getListCombine().size()>0){
            CombineAdapter combineAdapter = new CombineAdapter(activity, item.getListCombine());
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            viewHolder.rcvCombine.setLayoutManager(layoutManager);
            viewHolder.rcvCombine.setAdapter(combineAdapter);
        } else {
            ItemBuilderAdapter combineAdapter = new ItemBuilderAdapter(activity, item.getListItemBuilder());
            GridLayoutManager adapterManager = new GridLayoutManager(activity, 4);
            viewHolder.rcvCombine.setLayoutManager(adapterManager);
            viewHolder.rcvCombine.setAdapter(combineAdapter);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivIcon;
        TextView tvDes;
        RecyclerView rcvCombine;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_items);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDes = itemView.findViewById(R.id.tv_des);
            rcvCombine = itemView.findViewById(R.id.rcv_combine);
        }

        void bind(final Item item, final int position) {
        }
    }
}
