package com.zinzin.loltft.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zinzin.loltft.DetailActivity;
import com.zinzin.loltft.R;
import com.zinzin.loltft.adapter.UnitBuilderSynergyAdapter;
import com.zinzin.loltft.adapter.UnitsBottomAdapter;
import com.zinzin.loltft.adapter.UnitsBuilderAdapter;
import com.zinzin.loltft.model.Origin;
import com.zinzin.loltft.model.Unit;
import com.zinzin.loltft.model.UnitsInfo;
import com.zinzin.loltft.utils.Contants;
import com.zinzin.loltft.utils.Preference;
import com.zinzin.loltft.utils.Utils;
import com.zinzin.loltft.view.CustomLayoutManager;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoBuilderFragment extends Fragment {
    public static String TAG = InfoBuilderFragment.class.getSimpleName();
    private RecyclerView rcvChoose, rcvSynergy;
    private Button btnAdd;
    private Button btnReset;
    private Dialog mBottomSheetDialog;
    private UnitsBuilderAdapter unitsBuilderAdapter;
    private UnitsBottomAdapter unitsBottomAdapter;
    private UnitBuilderSynergyAdapter unitBuilderSynergyAdapter;
    private List<Unit> unitsList = new ArrayList<>();
    private List<Unit> unitsChoose = new ArrayList<>();
    private List<String> listNameChoose = new ArrayList<>();
    private List<Origin> originList = new ArrayList<>();
    private List<Origin> classList = new ArrayList<>();
    List<UnitsInfo> unitsInfos = new ArrayList<>();

    public static InfoBuilderFragment newInstance() {
        return new InfoBuilderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_info_builder, container, false);
        initView(view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        },100);
        return view;
    }
    private void getData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tft_db").child("unit");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.child("unitList").getChildren()) {
                        Unit unit = ds.getValue(Unit.class);
                        unitsList.add(unit);
                    }
                    for (DataSnapshot ds : dataSnapshot.child("classList").getChildren()) {
                        Origin origin = ds.getValue(Origin.class);
                        classList.add(origin);
                    }
                    for (DataSnapshot ds : dataSnapshot.child("originList").getChildren()) {
                        Origin origin = ds.getValue(Origin.class);
                        originList.add(origin);
                    }
                }
                setUpBottomSheet();
                setUpBtnAdd();
                setUpBtnReset();
                setUpRcvChoose();
                setUpRcvSynergy();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
    private void initView(View view) {
        rcvChoose = view.findViewById(R.id.rcv_choose);
        rcvSynergy = view.findViewById(R.id.rcv_synergy);
        btnAdd = view.findViewById(R.id.btn_add);
        btnReset = view.findViewById(R.id.btn_reset);
        mBottomSheetDialog = new Dialog(getActivity(), R.style.AppTheme);
    }

    private void setUpRcvSynergy() {
        LinearLayoutManager layoutManager2
                = new CustomLayoutManager(getActivity());
        rcvSynergy.setLayoutManager(layoutManager2);
        unitBuilderSynergyAdapter = new UnitBuilderSynergyAdapter(getActivity(), unitsInfos);
        rcvSynergy.setAdapter(unitBuilderSynergyAdapter);
        setDataSynergy();
    }

    private void setDataSynergy() {
        unitsInfos.clear();
        Map<String, Integer> duplicates = new HashMap<String, Integer>();

        for (Unit units : unitsChoose) {
            if (duplicates.containsKey(units.getType().get(0).getName())) {
                duplicates.put(units.getType().get(0).getName(), duplicates.get(units.getType().get(0).getName()) + 1);
            } else {
                duplicates.put(units.getType().get(0).getName(), 1);
            }
            if (duplicates.containsKey(units.getType().get(1).getName())) {
                duplicates.put(units.getType().get(1).getName(), duplicates.get(units.getType().get(1).getName()) + 1);
            } else {
                duplicates.put(units.getType().get(1).getName(), 1);
            }
            if (units.getType().size() == 3) {
                if (duplicates.containsKey(units.getType().get(2).getName())) {
                    duplicates.put(units.getType().get(2).getName(), duplicates.get(units.getType().get(2).getName()) + 1);
                } else {
                    duplicates.put(units.getType().get(2).getName(), 1);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
            Log.e("duplicatesRace", entry.getKey() + " " + entry.getValue() + "");
            for (Origin origin : originList) {
                if (origin.getName().equals(entry.getKey())) {
                    UnitsInfo unitsInfo = null;
                    switch (origin.getDes().size()){
                        case 1:
                            int bonus1_1 = Character.getNumericValue(origin.getDes().get(0).charAt(1));
                            if(entry.getValue() >= bonus1_1){
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", origin.getDes().get(0), entry.getValue());
                            } else {
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", "", entry.getValue());
                            }
                            break;
                        case 2:
                            int bonus2_1 = Character.getNumericValue(origin.getDes().get(0).charAt(1));
                            int bonus2_2 = Character.getNumericValue(origin.getDes().get(1).charAt(1));
                            if(entry.getValue() < bonus2_1){
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", "", entry.getValue());
                            }
                            if(entry.getValue()>= bonus2_1 && entry.getValue() < bonus2_2){
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", origin.getDes().get(0), entry.getValue());
                            }
                            if(entry.getValue() >= bonus2_2){
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", getBonus(origin.getDes(),2), entry.getValue());
                            }
                            break;
                        case 3:
                            int bonus3_1 = Character.getNumericValue(origin.getDes().get(0).charAt(1));
                            int bonus3_2 = Character.getNumericValue(origin.getDes().get(1).charAt(1));
                            int bonus3_3 = Character.getNumericValue(origin.getDes().get(2).charAt(1));
                            if(entry.getValue() < bonus3_1){
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", "", entry.getValue());
                            }
                            if(entry.getValue()>= bonus3_1 && entry.getValue() < bonus3_2){
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", origin.getDes().get(0), entry.getValue());
                            }
                            if(entry.getValue() >= bonus3_2 && entry.getValue() < bonus3_3){
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", getBonus(origin.getDes(),2), entry.getValue());
                            }
                            if(entry.getValue() >= bonus3_3){
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", getBonus(origin.getDes(),3), entry.getValue());
                            }
                            break;
                    }
                    unitsInfos.add(unitsInfo);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
            Log.e("duplicatesClass", entry.getKey() + " " + entry.getValue() + "");
            for (Origin class_ : classList) {
                if (class_.getName().equals(entry.getKey())) {
                    UnitsInfo unitsInfo = null;
                    switch (class_.getDes().size()){
                        case 1:
                            int bonus1_1 = Character.getNumericValue(class_.getDes().get(0).charAt(1));
                            if(entry.getValue() >= bonus1_1){
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", class_.getDes().get(0), entry.getValue());
                            } else {
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", "", entry.getValue());
                            }
                            break;
                        case 2:
                            int bonus2_1 = Character.getNumericValue(class_.getDes().get(0).charAt(1));
                            int bonus2_2 = Character.getNumericValue(class_.getDes().get(1).charAt(1));
                            if(entry.getValue() < bonus2_1){
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", "", entry.getValue());
                            }
                            if(entry.getValue()>= bonus2_1 && entry.getValue() < bonus2_2){
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", class_.getDes().get(0), entry.getValue());
                            }
                            if(entry.getValue() >= bonus2_2){
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", getBonus(class_.getDes(),2), entry.getValue());
                            }
                            break;
                        case 3:
                            int bonus3_1 = Character.getNumericValue(class_.getDes().get(0).charAt(1));
                            int bonus3_2 = Character.getNumericValue(class_.getDes().get(1).charAt(1));
                            int bonus3_3 = Character.getNumericValue(class_.getDes().get(2).charAt(1));
                            if(entry.getValue() < bonus3_1){
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", "", entry.getValue());
                            }
                            if(entry.getValue()>= bonus3_1 && entry.getValue() < bonus3_2){
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", class_.getDes().get(0), entry.getValue());
                            }
                            if(entry.getValue() >= bonus3_2 && entry.getValue() < bonus3_3){
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", getBonus(class_.getDes(),2), entry.getValue());
                            }
                            if(entry.getValue() >= bonus3_3){
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", getBonus(class_.getDes(),3), entry.getValue());
                            }
                            break;
                    }
                    unitsInfos.add(unitsInfo);
                }
            }
        }
        unitBuilderSynergyAdapter.notifyDataSetChanged();
    }

    private void setUpBtnReset() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnReset.setVisibility(View.GONE);
                unitsChoose.clear();
                listNameChoose.clear();
                unitsBuilderAdapter.notifyDataSetChanged();
                for (Unit units : unitsList) {
                    if (units.isClick()) units.setClick(false);
                }
                unitsBottomAdapter.notifyDataSetChanged();
                unitsInfos.clear();
                unitBuilderSynergyAdapter.notifyDataSetChanged();
                removePreference();
            }
        });
    }

    private void setUpRcvChoose() {
        GridLayoutManager adapterManager = new GridLayoutManager(getActivity(), 5);
        rcvChoose.setLayoutManager(adapterManager);
        unitsBuilderAdapter = new UnitsBuilderAdapter(getActivity(), unitsChoose);
        rcvChoose.setAdapter(unitsBuilderAdapter);
        unitsBuilderAdapter.setListener(new UnitsBuilderAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Unit item, int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("name",item.getName());
                startActivity(intent);
            }
        });
    }

    private void setUpBottomSheet() {
        final View sheetView = getActivity().getLayoutInflater().inflate(R.layout.bottom_sheet_units, null);
        Button btnDone = sheetView.findViewById(R.id.btn_done);
        RecyclerView rvUnits = sheetView.findViewById(R.id.rcv_units);
        EditText edtSearch = sheetView.findViewById(R.id.edt_search);
        edtSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        GridLayoutManager adapterManager = new GridLayoutManager(getActivity(), 4);
        rvUnits.setLayoutManager(adapterManager);
        if(!Preference.getString(getActivity(),Contants.KEY_BUILDER_LIST_CHOOSE_NAME).equals("")){
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {}.getType();
            listNameChoose = gson.fromJson(Preference.getString(getActivity(),Contants.KEY_BUILDER_LIST_CHOOSE_NAME), type);
            unitsChoose.clear();
            for (String name : listNameChoose) {
                for (Unit unit : unitsList) {
                    if (unit.getName().equals(name)) {
                        unit.setClick(true);
                        unitsChoose.add(unit);
                    }
                }
            }
        }
        unitsBottomAdapter = new UnitsBottomAdapter(getActivity(), unitsList);
        rvUnits.setAdapter(unitsBottomAdapter);
        unitsBottomAdapter.setListener(new UnitsBottomAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Unit item, int position) {
                if (!item.isClick()) {
                    if (unitsChoose.size() < 10) {
                        item.setClick(true);
                        unitsChoose.add(item);
                    } else {
                        Toast.makeText(getActivity(), "Max units", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    item.setClick(false);
                    unitsChoose.remove(item);
                }
                unitsBottomAdapter.notifyItemChanged(position);
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                unitsBuilderAdapter.notifyDataSetChanged();
                if (unitsChoose.size() > 0) {
                    btnReset.setVisibility(View.VISIBLE);
                    setDataSynergy();
                    savePreference();
                } else {
                    removePreference();
                    btnReset.setVisibility(View.GONE);
                }
            }
        });
        mBottomSheetDialog.setContentView(sheetView);

    }

    private void savePreference() {
        listNameChoose.clear();
        for (Unit unit: unitsChoose){
            listNameChoose.add(unit.getName());
        }
        Preference.save(getActivity(), Contants.KEY_BUILDER_LIST_CHOOSE_NAME,Utils.convertObjToJson(listNameChoose));
    }
    private void removePreference() {
        Preference.remove(getActivity(), Contants.KEY_BUILDER_LIST_CHOOSE_NAME);

    }

    private void filter(String text) {
        List<Unit> unitsBase = new ArrayList<>();
        unitsBase.addAll(unitsList);
        List<Unit> listUnitsFilter = new ArrayList();
        for (Unit units : unitsBase) {
            //filter theo tên
            if (units.getName().toLowerCase().contains(text.toLowerCase())) {
                listUnitsFilter.add(units);
            }
        }
        if (text.equals("")) {
            listUnitsFilter.clear();
            unitsBottomAdapter.updateList(unitsBase);
        } else {
            unitsBottomAdapter.updateList(listUnitsFilter);
        }
    }

    private void setUpBtnAdd() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.show();
            }
        });
    }

    private String getBonus(List<String> bonus, int size){
        StringBuilder stringBonus = new StringBuilder();
        for(int i = 0; i < size; i++){
            stringBonus.append(bonus.get(i)).append("\n");
        }
        return stringBonus.toString().substring(0,stringBonus.toString().length()-1);
    }
}
