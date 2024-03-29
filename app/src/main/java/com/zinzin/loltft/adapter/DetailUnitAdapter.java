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
import com.zinzin.loltft.model.Type;
import com.zinzin.loltft.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DetailUnitAdapter extends RecyclerView.Adapter<DetailUnitAdapter.ViewHolder> {

    private Activity activity;
    private List<Type> infos = new ArrayList<>();

    public DetailUnitAdapter(Activity activity, List<Type> infos) {
        this.activity = activity;
        this.infos = infos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.detail_units_items, viewGroup, false);
        return new DetailUnitAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(infos.get(position));
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvType;
        TextView tvDes;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvType = itemView.findViewById(R.id.tv_type);
            tvDes = itemView.findViewById(R.id.tv_des);
        }

        void bind(final Type item) {
            Glide.with(activity).load(item.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivIcon);
            tvName.setText(item.getName());
            tvType.setText(item.getType());
            tvDes.setText(Utils.linkStringFromArray(item.getDes()));
        }
    }
}
