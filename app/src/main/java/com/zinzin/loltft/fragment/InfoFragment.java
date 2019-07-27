package com.zinzin.loltft.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zinzin.loltft.OnDataReceiveCallback;
import com.zinzin.loltft.R;
import com.zinzin.loltft.adapter.TeamAdapter;
import com.zinzin.loltft.model.Team;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends Fragment {
    public RecyclerView rcvTeam;
    private List<Team> teamList = new ArrayList<>();

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
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tft_db_test").child("teamList2");
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
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvTeam.setLayoutManager(layoutManager);
        TeamAdapter teamAdapter = new TeamAdapter(getActivity(), teamList);
        rcvTeam.setAdapter(teamAdapter);
    }

}
