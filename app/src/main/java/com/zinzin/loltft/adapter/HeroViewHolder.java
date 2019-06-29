package com.zinzin.loltft.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zinzin.loltft.R;
import com.zinzin.loltft.model.Unit;

public class HeroViewHolder extends RecyclerView.ViewHolder {
    ImageView ivIconUnit, ivIcon1, ivIcon2, ivIcon3;
    TextView tvNameUnit;

    public HeroViewHolder(View itemView) {
        super(itemView);
        ivIconUnit = itemView.findViewById(R.id.iv_icon_unit);
        ivIcon1 = itemView.findViewById(R.id.iv_icon1);
        ivIcon2 = itemView.findViewById(R.id.iv_icon2);
        ivIcon3 = itemView.findViewById(R.id.iv_icon3);
        tvNameUnit = itemView.findViewById(R.id.tv_name);
    }
}
