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
import com.zinzin.loltft.model.Unit;

import java.util.ArrayList;
import java.util.List;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.ViewHolder> {

    private Activity activity;
    private List<Unit> heroList = new ArrayList<>();
    private OnItemClickListener listener;

    public HeroAdapter(Activity activity, List<Unit> unitsList) {
        this.activity = activity;
        this.heroList = unitsList;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.units_item, viewGroup, false);
        return new HeroAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(heroList.get(position), position, listener);
        Unit unit = heroList.get(position);
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
        switch (unit.getTier()) {
            case "S":
                viewHolder.tvTier.setText("S");
                viewHolder.tvTier.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.border_background_s));
                break;
            case "A":
                viewHolder.tvTier.setText("A");
                viewHolder.tvTier.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.border_background_a));
                break;
            case "B":
                viewHolder.tvTier.setText("B");
                viewHolder.tvTier.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.border_background_b));
                break;
            case "C":
                viewHolder.tvTier.setText("C");
                viewHolder.tvTier.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.border_background_c));
                break;
            case "D":
                viewHolder.tvTier.setText("D");
                viewHolder.tvTier.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.border_background_d));
                break;
            case "E":
                viewHolder.tvTier.setText("E");
                viewHolder.tvTier.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.border_background_e));
                break;
            case "F":
                viewHolder.tvTier.setText("F");
                viewHolder.tvTier.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.border_background_f));
                break;

        }

    }

    public void updateList(List<Unit> list) {
        heroList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIconUnit, ivIcon1, ivIcon2, ivIcon3;
        TextView tvNameUnit, tvTier;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIconUnit = itemView.findViewById(R.id.iv_icon_unit);
            ivIcon1 = itemView.findViewById(R.id.iv_icon1);
            ivIcon2 = itemView.findViewById(R.id.iv_icon2);
            ivIcon3 = itemView.findViewById(R.id.iv_icon3);
            tvNameUnit = itemView.findViewById(R.id.tv_name);
            tvTier = itemView.findViewById(R.id.tv_tier);
        }

        void bind(final Unit item, final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.setClickable(false);
                    if (listener != null)
                        listener.OnItemClick(item, position);
                    notifyDataSetChanged();
                }
            });
        }
    }


    public interface OnItemClickListener {
        void OnItemClick(Unit item, int position);
    }
}
