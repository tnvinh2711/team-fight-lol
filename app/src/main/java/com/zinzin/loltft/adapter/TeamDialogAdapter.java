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
import com.zinzin.loltft.model.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamDialogAdapter extends RecyclerView.Adapter<TeamDialogAdapter.ViewHolder> {

    private Activity activity;
    private List<Team.Hero> heroList = new ArrayList<>();
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public TeamDialogAdapter(Activity activity, List<Team.Hero> heroList) {
        this.activity = activity;
        this.heroList = heroList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_team, viewGroup, false);
        return new TeamDialogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final Team.Hero hero = heroList.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void showDialogList(List<Team.Hero> heroList) {

    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIconUnit, ivItem1,ivItem2,ivItem3, ivItemCombine1_1,ivItemCombine1_2, ivItemCombine2_1,ivItemCombine2_2, ivItemCombine3_1,ivItemCombine3_2;
        TextView tvName, tvType;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvType = itemView.findViewById(R.id.tv_type);
            ivIconUnit = itemView.findViewById(R.id.iv_hero);
            ivItem1 = itemView.findViewById(R.id.iv_item_1);
            ivItem2 = itemView.findViewById(R.id.iv_item_2);
            ivItem3 = itemView.findViewById(R.id.iv_item_3);
            ivItemCombine1_1 = itemView.findViewById(R.id.iv_combine_1_1);
            ivItemCombine1_2 = itemView.findViewById(R.id.iv_combine_1_2);
            ivItemCombine2_1 = itemView.findViewById(R.id.iv_combine_2_1);
            ivItemCombine2_2 = itemView.findViewById(R.id.iv_combine_2_2);
            ivItemCombine3_1 = itemView.findViewById(R.id.iv_combine_3_1);
            ivItemCombine3_2 = itemView.findViewById(R.id.iv_combine_3_2);
        }

        void bind(final Team.Hero combine, final int position) {
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(Team.Hero item, int position);
    }
}
