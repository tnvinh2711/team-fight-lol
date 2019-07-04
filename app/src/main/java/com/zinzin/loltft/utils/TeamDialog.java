package com.zinzin.loltft.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zinzin.loltft.DetailActivity;
import com.zinzin.loltft.R;
import com.zinzin.loltft.adapter.TeamDialogAdapter;
import com.zinzin.loltft.model.Team;

public class TeamDialog extends Dialog {
    private static final String TAG = TeamDialog.class.getSimpleName();
    private Activity mActivity;
    private TextView tvName;
    private ImageView ivClose;
    private RecyclerView recyclerView;
    private Team team;

    public TeamDialog(Activity activity, Team team) {
        super(activity);
        this.mActivity = activity;
        this.team = team;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_team);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvName = findViewById(R.id.tv_name_team);
        recyclerView = findViewById(R.id.rcv_team);
        ivClose = findViewById(R.id.iv_close);
        GridLayoutManager adapterManager = new GridLayoutManager(mActivity, 2);
        TeamDialogAdapter teamAdapter = new TeamDialogAdapter(mActivity,team.getHeroList());
        teamAdapter.setListener(new TeamDialogAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Team.Hero item, int position) {
                Intent intent = new Intent(mActivity, DetailActivity.class);
                intent.putExtra("name", item.getName_hero());
                mActivity.startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(adapterManager);
        recyclerView.setAdapter(teamAdapter);
        tvName.setText(team.getName());
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}

