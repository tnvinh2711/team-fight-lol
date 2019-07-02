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
import com.zinzin.loltft.model.Builder;
import com.zinzin.loltft.model.Item;
import com.zinzin.loltft.model.Unit;

import java.util.ArrayList;
import java.util.List;

public class BuilderAdapter extends RecyclerView.Adapter<BuilderAdapter.ViewHolder> {

    private Activity activity;
    private List<Builder> builderList = new ArrayList<>();
    private OnItemClickListener listener;

    public BuilderAdapter(Activity activity,List<Builder> builderList) {
        this.activity = activity;
        this.builderList = builderList;
    }
    public void updateList(List<Builder> list) {
        builderList = list;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_save_team, viewGroup, false);
        return new BuilderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.bind(builderList.get(position), position);
        final Builder builder = builderList.get(position);
        viewHolder.tvName.setText(builder.getName_team());
        viewHolder.tvType.setText(builder.getType());
        UnitsBuilderAdapter unitsBuilderAdapter = new UnitsBuilderAdapter(activity, builder.getUnitList());
        GridLayoutManager adapterManager = new GridLayoutManager(activity, 5);
        viewHolder.rcvUnit.setLayoutManager(adapterManager);
        viewHolder.rcvUnit.setAdapter(unitsBuilderAdapter);
        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnEditClick(builder.getName_team(), position);
            }
        });
        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnDeteleClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return builderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType;
        ImageView ivEdit;
        ImageView ivDelete;
        RecyclerView rcvUnit;

        public ViewHolder(View itemView) {
            super(itemView);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivDelete = itemView.findViewById(R.id.iv_close);
            tvName = itemView.findViewById(R.id.tv_name);
            tvType = itemView.findViewById(R.id.tv_type);
            rcvUnit = itemView.findViewById(R.id.rcv_unit);
        }

        void bind(final Builder builder, final int position) {
        }
    }

    public interface OnItemClickListener {
        void OnEditClick(String name, int position);
        void OnDeteleClick(int position);
    }
}
