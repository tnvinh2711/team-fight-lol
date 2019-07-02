package com.zinzin.loltft.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zinzin.loltft.BuildTeamActivity;
import com.zinzin.loltft.OnDataReceiveCallback;
import com.zinzin.loltft.R;
import com.zinzin.loltft.adapter.BuilderAdapter;
import com.zinzin.loltft.model.Builder;
import com.zinzin.loltft.model.Unit;
import com.zinzin.loltft.utils.Contants;
import com.zinzin.loltft.utils.Preference;
import com.zinzin.loltft.utils.Utils;
import com.zinzin.loltft.view.CustomLayoutManager;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuilderFragment extends Fragment {
    public static String TAG = BuilderFragment.class.getSimpleName();
    private RecyclerView rcvChoose;
    private ImageView btnAdd;
    private BuilderAdapter builderAdapter;
    private List<Unit> unitsList = new ArrayList<>();
    private List<Builder> builders = new ArrayList<>();
    private List<String>  listNameChoose = new ArrayList<>();


    public static BuilderFragment newInstance() {
        return new BuilderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_builder, container, false);

        initView(view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData(new OnDataReceiveCallback() {
                    @Override
                    public void onDataReceived() {
                        getDataPreferrence();
                    }
                });
            }
        }, 100);
        return view;
    }

    private void getDataPreferrence() {
        builders.clear();
        if (!Preference.getString(getActivity(), Contants.KEY_BUILDER_LIST_CHOOSE_NAME).equals("")) {
            Gson gson = new Gson();
            builders = gson.fromJson(Preference.getString(getActivity(), Contants.KEY_BUILDER_LIST_CHOOSE_NAME), new TypeToken<List<Builder>>() {
            }.getType());
            listNameChoose.clear();
            for(Builder builder:builders){
                List<String> nameList = builder.getHero_name();
                List<Unit> unitsChoose = new ArrayList<>();
                for (Unit unit: unitsList){
                    for (String name: nameList){
                        if(name.equals(unit.getName())){
                            unitsChoose.add(unit);
                        }
                    }
                }
                builder.setUnitList(unitsChoose);
            }
            setUpRcvChoose();
            Log.e("doneRCVBUILDER","doneRCVBUILDER");
        }
    }

    private void getData(final OnDataReceiveCallback callback) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tft_db").child("unit");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.child("unitList").getChildren()) {
                        Unit unit = ds.getValue(Unit.class);
                        unitsList.add(unit);
                    }
                }
                callback.onDataReceived();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void initView(View view) {
        rcvChoose = view.findViewById(R.id.rcv_choose);
        rcvChoose.setVisibility(View.GONE);
        btnAdd = view.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuildTeamActivity.class);
                intent.putExtra("name","");
                startActivityForResult(intent, 909);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) getDataPreferrence();
    }

    private void setUpRcvChoose() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvChoose.setLayoutManager(layoutManager);
        builderAdapter = new BuilderAdapter(getActivity(), builders);
        rcvChoose.setAdapter(builderAdapter);
        builderAdapter.setListener(new BuilderAdapter.OnItemClickListener() {
            @Override
            public void OnEditClick(String name, int position) {
                Intent intent = new Intent(getActivity(), BuildTeamActivity.class);
                intent.putExtra("name",name);
                startActivityForResult(intent, 909);
            }

            @Override
            public void OnDeteleClick(int position) {
                builders.remove(position);
                builderAdapter.updateList(builders);
                Preference.save(getActivity(), Contants.KEY_BUILDER_LIST_CHOOSE_NAME, Utils.convertObjToJson(builders));
            }
        });
        rcvChoose.setVisibility(View.VISIBLE);
        Log.e("doneRCVBUILDER","doneRCVBUILDER");
    }
}
