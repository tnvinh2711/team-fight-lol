package com.zinzin.loltft.adapter;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zinzin.loltft.R;
import com.zinzin.loltft.model.Unit;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class HeaderRecyclerViewSection extends StatelessSection {
    private static final String TAG = HeaderRecyclerViewSection.class.getSimpleName();
    private String title;
    private List<Unit> list;
    private Activity activity;
    private int colorTitle;
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public HeaderRecyclerViewSection(Activity activity, String title, List<Unit> list, int colorTitle) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.units_item)
                .headerResourceId(R.layout.header_hero_layout)
                .build());
        this.activity = activity;
        this.title = title;
        this.list = list;
        this.colorTitle = colorTitle;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new HeroViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final HeroViewHolder viewHolder = (HeroViewHolder) holder;
        final Unit unit = list.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.itemView.setClickable(false);
                if (listener != null)
                    listener.OnItemClick(unit, position);
            }
        });
        Glide.with(activity).load(unit.getUrl()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivIconUnit);
        viewHolder.tvNameUnit.setText(unit.getName());
//        viewHolder.tvNameUnit.setTextColor(activity.getResources().getColor(units.getColor_name()));
        Glide.with(activity).load(unit.getType().get(0).getUrl()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivIcon1);
        Glide.with(activity).load(unit.getType().get(1).getUrl()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivIcon2);
        if (unit.getType().size() > 2) {
            viewHolder.ivIcon3.setVisibility(View.VISIBLE);
            Glide.with(activity).load(unit.getType().get(2).getUrl()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivIcon3);
        } else {
            viewHolder.ivIcon3.setVisibility(View.GONE);
        }
        switch (unit.getCost()) {
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

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder hHolder = (HeaderViewHolder) holder;
        hHolder.headerTitle.setText(title);
        hHolder.headerTitle.setBackgroundColor(activity.getResources().getColor(colorTitle));
    }

    public interface OnItemClickListener {
        void OnItemClick(Unit item, int position);
    }
}
