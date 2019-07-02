package com.zinzin.loltft.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zinzin.loltft.DetailActivity;
import com.zinzin.loltft.OnDataReceiveCallback;
import com.zinzin.loltft.R;
import com.zinzin.loltft.adapter.HeaderRecyclerViewSection;
import com.zinzin.loltft.adapter.HeaderTeamRecyclerViewSection;
import com.zinzin.loltft.model.Team;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class InfoFragment extends Fragment {
    public RecyclerView rcvTeam;
    private List<Team> teamList = new ArrayList<>();
    private SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        rcvTeam = view.findViewById(R.id.rcv_team);
        getData(new OnDataReceiveCallback() {
            @Override
            public void onDataReceived() {
                setUpRecycleView();
            }
        });
        return view;
    }

    private void getData(final OnDataReceiveCallback callback) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tft_db").child("teamList");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Team team = ds.getValue(Team.class);
                        teamList.add(team);
                    }
                }
                callback.onDataReceived();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void setUpRecycleView() {
        GridLayoutManager adapterManager = new GridLayoutManager(getActivity(), 2);

        for (Team team : teamList) {
            HeaderTeamRecyclerViewSection viewSection = new HeaderTeamRecyclerViewSection(getActivity(), team.getName(), team.getHeroList());
            viewSection.setListener(new HeaderTeamRecyclerViewSection.OnItemClickListener() {
                @Override
                public void OnItemClick(Team.Hero item, int position) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("name", item.getName_hero());
                    startActivity(intent);
                }
            });
            sectionAdapter.addSection(viewSection);
        }

        adapterManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (sectionAdapter.getSectionItemViewType(position) == SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER) {
                    return 2;
                }
                return 1;
            }
        });
        rcvTeam.setLayoutManager(adapterManager);
        rcvTeam.setAdapter(sectionAdapter);
    }

}
