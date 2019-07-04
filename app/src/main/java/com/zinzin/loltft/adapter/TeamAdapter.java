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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zinzin.loltft.R;
import com.zinzin.loltft.model.Team;
import com.zinzin.loltft.utils.TeamDialog;

import java.util.ArrayList;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    private Activity activity;
    private List<Team> teamList = new ArrayList<>();

    public TeamAdapter(Activity activity, List<Team> teamList) {
        this.activity = activity;
        this.teamList = teamList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.header_team_layout, viewGroup, false);
        return new TeamAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.bind(teamList.get(position), position);
        viewHolder.headerTitle.setText(teamList.get(position).getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogList(teamList.get(position));
            }
        });
    }

    private void showDialogList(Team team) {
        TeamDialog teamDialog = new TeamDialog(activity,team);
        teamDialog.setCancelable(true);
        teamDialog.show();
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            headerTitle = itemView.findViewById(R.id.header_id);
        }

        void bind(final Team combine, final int position) {
        }
    }
}
