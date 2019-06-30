package com.zinzin.loltft.adapter;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zinzin.loltft.R;
import com.zinzin.loltft.model.Team;
import com.zinzin.loltft.model.Unit;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class HeaderTeamRecyclerViewSection extends StatelessSection {
    private static final String TAG = HeaderTeamRecyclerViewSection.class.getSimpleName();
    private String title;
    private List<Team.Hero> list;
    private Activity activity;
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public HeaderTeamRecyclerViewSection(Activity activity, String title, List<Team.Hero> list) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_team)
                .headerResourceId(R.layout.header_team_layout)
                .build());
        this.activity = activity;
        this.title = title;
        this.list = list;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TeamViewHolder viewHolder = (TeamViewHolder) holder;
        final Team.Hero hero = list.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.itemView.setClickable(false);
                if (listener != null)
                    listener.OnItemClick(hero, position);
            }
        });
        Glide.with(activity).load(hero.getUrl_hero()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivIconUnit);
        viewHolder.tvName.setText(hero.getName_hero());
        viewHolder.tvType.setText(hero.getType());
        Glide.with(activity).load(hero.getUrl_item_1()).into(viewHolder.ivItem1);
        Glide.with(activity).load(hero.getUrl_item_2()).into(viewHolder.ivItem2);
        Glide.with(activity).load(hero.getUrl_item_3()).into(viewHolder.ivItem3);
        Glide.with(activity).load(hero.getUrl__item_combine_1_1()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivItemCombine1_1);
        Glide.with(activity).load(hero.getUrl__item_combine_1_2()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivItemCombine1_2);
        Glide.with(activity).load(hero.getUrl__item_combine_2_1()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivItemCombine2_1);
        Glide.with(activity).load(hero.getUrl__item_combine_2_2()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivItemCombine2_2);
        Glide.with(activity).load(hero.getUrl__item_combine_3_1()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivItemCombine3_1);
        Glide.with(activity).load(hero.getUrl__item_combine_3_2()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivItemCombine3_2);
        switch (hero.getCost()) {
            case "$1":
                viewHolder.tvName.setTextColor(activity.getResources().getColor(R.color.color_cost_1));
                break;
            case "$2":
                viewHolder.tvName.setTextColor(activity.getResources().getColor(R.color.color_cost_2));
                break;
            case "$3":
                viewHolder.tvName.setTextColor(activity.getResources().getColor(R.color.color_cost_3));
                break;
            case "$4":
                viewHolder.tvName.setTextColor(activity.getResources().getColor(R.color.color_cost_4));
                break;
            case "$5":
                viewHolder.tvName.setTextColor(activity.getResources().getColor(R.color.color_cost_5));
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
    }

    public interface OnItemClickListener {
        void OnItemClick(Team.Hero item, int position);
    }
}
