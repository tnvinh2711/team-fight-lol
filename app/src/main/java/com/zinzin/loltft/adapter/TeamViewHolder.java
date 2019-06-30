package com.zinzin.loltft.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zinzin.loltft.R;

public class TeamViewHolder extends RecyclerView.ViewHolder {
    ImageView ivIconUnit, ivItem1,ivItem2,ivItem3, ivItemCombine1_1,ivItemCombine1_2, ivItemCombine2_1,ivItemCombine2_2, ivItemCombine3_1,ivItemCombine3_2;
    TextView tvName, tvType;

    public TeamViewHolder(View itemView) {
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
}
