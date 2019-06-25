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

public class MiniIconAdapter extends RecyclerView.Adapter<MiniIconAdapter.ViewHolder> {

    private Activity activity;
    private List<Type> imgList = new ArrayList<>();

    public MiniIconAdapter(Activity activity, List<Type> imgList) {
        this.activity = activity;
        this.imgList = imgList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mini_icon_item, viewGroup, false);
        return new MiniIconAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(imgList.get(position), position);
        Glide.with(activity).load(imgList.get(position).getUrl()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivIcon);
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

        void bind(final Type item, final int position) {
        }
    }
}
