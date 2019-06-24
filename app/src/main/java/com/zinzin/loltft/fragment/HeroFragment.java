package com.zinzin.loltft.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zinzin.loltft.R;
import com.zinzin.loltft.adapter.HeroAdapter;
import com.zinzin.loltft.model.Detail;
import com.zinzin.loltft.model.Item;
import com.zinzin.loltft.model.Round;
import com.zinzin.loltft.model.Unit;

import java.util.ArrayList;
import java.util.List;

public class HeroFragment extends Fragment {
    public static String TAG = HeroFragment.class.getSimpleName();
    private RecyclerView rvUnits;
    private HeroAdapter heroAdapter;
    List<Unit> heroList = new ArrayList<>();
    List<Round> roundList = new ArrayList<>();
    public static HeroFragment newInstance() {
        return new HeroFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hero, container, false);
        initView(view);
        getData();
        return view;
    }

    private void initView(View view) {
        rvUnits = view.findViewById(R.id.rcv_units);
        setUpRecycleView();
    }

    public void setData(List<Unit> heroes) {
        heroAdapter.updateList(heroes);
    }
    private void setUpRecycleView() {
        GridLayoutManager adapterManager;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            adapterManager = new GridLayoutManager(getActivity(), 5);
        } else {
            adapterManager = new GridLayoutManager(getActivity(), 3);
        }
        rvUnits.setLayoutManager(adapterManager);
        heroAdapter = new HeroAdapter(getActivity(), heroList);
        rvUnits.setAdapter(heroAdapter);
        heroAdapter.setListener(new HeroAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Unit item, int position) {
            }
        });
    }
    private void getData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tft_db");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("unitList").getChildren()) {
                    Unit unit = ds.getValue(Unit.class);
                    heroList.add(unit);
                }
                setData(heroList);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
