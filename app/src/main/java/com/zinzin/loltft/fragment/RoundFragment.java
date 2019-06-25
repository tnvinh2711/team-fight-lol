package com.zinzin.loltft.fragment;

import android.os.Bundle;
import android.os.Handler;
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
import com.zinzin.loltft.R;
import com.zinzin.loltft.adapter.CreepsAdapter;
import com.zinzin.loltft.adapter.ItemAdapter;
import com.zinzin.loltft.model.Item;
import com.zinzin.loltft.model.Round;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoundFragment extends Fragment {
    public static String TAG = RoundFragment.class.getSimpleName();
    private List<Round> creepList = new ArrayList<>();
    private RecyclerView rcvCreep;
    private CreepsAdapter creepsAdapter;

    public static RoundFragment newInstance() {
        return new RoundFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creeps, container, false);
        initView(view);
        setUpRcvCreep();
        return view;
    }



    private void initView(View view) {
        rcvCreep = view.findViewById(R.id.rcv_creeps);
    }

    private void setUpRcvCreep() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvCreep.setLayoutManager(layoutManager);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        },100);
    }

    public void getData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tft_db").child("roundList");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Round round = ds.getValue(Round.class);
                        creepList.add(round);
                    }
                }
                creepsAdapter = new CreepsAdapter(getActivity(), creepList);
                rcvCreep.setAdapter(creepsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
