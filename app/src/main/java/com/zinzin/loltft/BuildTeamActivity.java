package com.zinzin.loltft;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.zinzin.loltft.adapter.UnitBuilderSynergyAdapter;
import com.zinzin.loltft.adapter.UnitsBottomAdapter;
import com.zinzin.loltft.adapter.UnitsBuilderAdapter;
import com.zinzin.loltft.model.Builder;
import com.zinzin.loltft.model.Origin;
import com.zinzin.loltft.model.Unit;
import com.zinzin.loltft.model.UnitsInfo;
import com.zinzin.loltft.utils.Contants;
import com.zinzin.loltft.utils.Preference;
import com.zinzin.loltft.utils.Utils;
import com.zinzin.loltft.view.CustomLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTeamActivity extends AppCompatActivity {
    public static String TAG = BuildTeamActivity.class.getSimpleName();
    private RecyclerView rcvChoose, rcvSynergy;
    private Button btnAdd;
    private Button btnReset;
    private TextView tvSave;
    private EditText edtName;
    private String name;
    private int idEdit = -998;
    private Dialog mBottomSheetDialog;
    private UnitsBuilderAdapter unitsBuilderAdapter;
    private UnitsBottomAdapter unitsBottomAdapter;
    private UnitBuilderSynergyAdapter unitBuilderSynergyAdapter;
    private List<Unit> unitsList = new ArrayList<>();
    private List<Unit> unitsChoose = new ArrayList<>();
    private List<String> listNameChoose = new ArrayList<>();
    private List<Origin> originList = new ArrayList<>();
    private List<Origin> classList = new ArrayList<>();
    private List<UnitsInfo> unitsInfos = new ArrayList<>();
    private List<Builder> builders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_builder);
        name = getIntent().getStringExtra("name");
        initView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData(new OnDataReceiveCallback() {
                    @Override
                    public void onDataReceived() {
                        getDataFromPreferrence();
                        setUpBottomSheet();
                        setUpBtnAdd();
                        setUpBtnReset();
                        setUpRcvChoose();
                        setUpRcvSynergy();
                        setUpEdtName();
                    }
                });
            }
        }, 100);
    }

    private void getDataFromPreferrence() {
        if (!Preference.getString(this, Contants.KEY_BUILDER_LIST_CHOOSE_NAME).equals("")) {
            Gson gson = new Gson();
            builders = gson.fromJson(Preference.getString(this, Contants.KEY_BUILDER_LIST_CHOOSE_NAME), new TypeToken<List<Builder>>() {
            }.getType());
            unitsChoose.clear();
            listNameChoose.clear();
            for (int i = 0; i < builders.size(); i++) {
                if (builders.get(i).getName_team().equals(name)) {
                    idEdit = i;
                    listNameChoose = builders.get(i).getHero_name();
                }
            }
            for (String name : listNameChoose) {
                for (Unit unit : unitsList) {
                    if (unit.getName().equals(name)) {
                        unit.setClick(true);
                        unitsChoose.add(unit);
                    }
                }
            }
        }
    }

    private void setUpEdtName() {
        edtName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtName.setText(name);
        edtName.setSelection(edtName.getText().length());
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString();
            }
        });
    }

    private void getData(final OnDataReceiveCallback callback) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tft_db_test").child("unit");
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
                callback.onDataReceived();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void initView() {
        rcvChoose = findViewById(R.id.rcv_choose);
        tvSave = findViewById(R.id.tv_save);
        rcvSynergy = findViewById(R.id.rcv_synergy);
        btnAdd = findViewById(R.id.btn_add);
        edtName = findViewById(R.id.edt_name);
        btnReset = findViewById(R.id.btn_reset);
        mBottomSheetDialog = new Dialog(this, R.style.AppTheme);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTeam();
            }
        });
    }

    private void setUpRcvSynergy() {
        LinearLayoutManager layoutManager2
                = new CustomLayoutManager(this);
        rcvSynergy.setLayoutManager(layoutManager2);
        unitBuilderSynergyAdapter = new UnitBuilderSynergyAdapter(this, unitsInfos);
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
            for (Origin origin : originList) {
                if (origin.getName().equals(entry.getKey())) {
                    UnitsInfo unitsInfo = null;
                    switch (origin.getDes().size()) {
                        case 1:
                            int bonus1_1 = Character.getNumericValue(origin.getDes().get(0).charAt(1));
                            if (entry.getValue() >= bonus1_1) {
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", origin.getDes().get(0), entry.getValue());
                            } else {
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", "", entry.getValue());
                            }
                            break;
                        case 2:
                            int bonus2_1 = Character.getNumericValue(origin.getDes().get(0).charAt(1));
                            int bonus2_2 = Character.getNumericValue(origin.getDes().get(1).charAt(1));
                            if (entry.getValue() < bonus2_1) {
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", "", entry.getValue());
                            }
                            if (entry.getValue() >= bonus2_1 && entry.getValue() < bonus2_2) {
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", origin.getDes().get(0), entry.getValue());
                            }
                            if (entry.getValue() >= bonus2_2) {
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", getBonus(origin.getDes(), 2), entry.getValue());
                            }
                            break;
                        case 3:
                            int bonus3_1 = Character.getNumericValue(origin.getDes().get(0).charAt(1));
                            int bonus3_2 = Character.getNumericValue(origin.getDes().get(1).charAt(1));
                            int bonus3_3 = Character.getNumericValue(origin.getDes().get(2).charAt(1));
                            if (entry.getValue() < bonus3_1) {
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", "", entry.getValue());
                            }
                            if (entry.getValue() >= bonus3_1 && entry.getValue() < bonus3_2) {
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", origin.getDes().get(0), entry.getValue());
                            }
                            if (entry.getValue() >= bonus3_2 && entry.getValue() < bonus3_3) {
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", getBonus(origin.getDes(), 2), entry.getValue());
                            }
                            if (entry.getValue() >= bonus3_3) {
                                unitsInfo = new UnitsInfo(origin.getUrl(), entry.getKey(), "Origin", getBonus(origin.getDes(), 3), entry.getValue());
                            }
                            break;
                    }
                    unitsInfos.add(unitsInfo);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
            for (Origin class_ : classList) {
                if (class_.getName().equals(entry.getKey())) {
                    UnitsInfo unitsInfo = null;
                    switch (class_.getDes().size()) {
                        case 1:
                            int bonus1_1 = Character.getNumericValue(class_.getDes().get(0).charAt(1));
                            if (entry.getValue() >= bonus1_1) {
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", class_.getDes().get(0), entry.getValue());
                            } else {
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", "", entry.getValue());
                            }
                            break;
                        case 2:
                            int bonus2_1 = Character.getNumericValue(class_.getDes().get(0).charAt(1));
                            int bonus2_2 = Character.getNumericValue(class_.getDes().get(1).charAt(1));
                            if (entry.getValue() < bonus2_1) {
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", "", entry.getValue());
                            }
                            if (entry.getValue() >= bonus2_1 && entry.getValue() < bonus2_2) {
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", class_.getDes().get(0), entry.getValue());
                            }
                            if (entry.getValue() >= bonus2_2) {
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", getBonus(class_.getDes(), 2), entry.getValue());
                            }
                            break;
                        case 3:
                            int bonus3_1 = Character.getNumericValue(class_.getDes().get(0).charAt(1));
                            int bonus3_2 = Character.getNumericValue(class_.getDes().get(1).charAt(1));
                            int bonus3_3 = Character.getNumericValue(class_.getDes().get(2).charAt(1));
                            if (entry.getValue() < bonus3_1) {
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", "", entry.getValue());
                            }
                            if (entry.getValue() >= bonus3_1 && entry.getValue() < bonus3_2) {
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", class_.getDes().get(0), entry.getValue());
                            }
                            if (entry.getValue() >= bonus3_2 && entry.getValue() < bonus3_3) {
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", getBonus(class_.getDes(), 2), entry.getValue());
                            }
                            if (entry.getValue() >= bonus3_3) {
                                unitsInfo = new UnitsInfo(class_.getUrl(), entry.getKey(), "Class", getBonus(class_.getDes(), 3), entry.getValue());
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
        if(idEdit != -998) btnReset.setVisibility(View.VISIBLE);
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
            }
        });
    }

    private void setUpRcvChoose() {
        GridLayoutManager adapterManager = new GridLayoutManager(this, 5);
        rcvChoose.setLayoutManager(adapterManager);
        unitsBuilderAdapter = new UnitsBuilderAdapter(this, unitsChoose);
        rcvChoose.setAdapter(unitsBuilderAdapter);
        unitsBuilderAdapter.setListener(new UnitsBuilderAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Unit item, int position) {
                Intent intent = new Intent(BuildTeamActivity.this, DetailActivity.class);
                intent.putExtra("name", item.getName());
                startActivity(intent);
            }
        });
    }

    private void setUpBottomSheet() {
        final View sheetView = this.getLayoutInflater().inflate(R.layout.bottom_sheet_units, null);
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
        GridLayoutManager adapterManager = new GridLayoutManager(this, 4);
        rvUnits.setLayoutManager(adapterManager);
        unitsBottomAdapter = new UnitsBottomAdapter(this, unitsList);
        rvUnits.setAdapter(unitsBottomAdapter);
        unitsBottomAdapter.setListener(new UnitsBottomAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Unit item, int position) {
                if (!item.isClick()) {
                    if (unitsChoose.size() < 10) {
                        item.setClick(true);
                        unitsChoose.add(item);
                    } else {
                        Toast.makeText(BuildTeamActivity.this, "Max units", Toast.LENGTH_SHORT).show();
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
                    btnReset.setVisibility(View.GONE);
                }
            }
        });
        mBottomSheetDialog.setContentView(sheetView);

    }

    private void savePreference() {

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

    private String getBonus(List<String> bonus, int size) {
        StringBuilder stringBonus = new StringBuilder();
        for (int i = 0; i < size; i++) {
            stringBonus.append(bonus.get(i)).append("\n");
        }
        return stringBonus.toString().substring(0, stringBonus.toString().length() - 1);
    }

    private void saveTeam() {
//        if(idEdit!= -998) builders.remove(idEdit);
        if (name.equals("")) {
            Toast.makeText(this, "Name must be filled", Toast.LENGTH_SHORT).show();
            return;
        }
        listNameChoose.clear();
        for (Unit unit : unitsChoose) {
            listNameChoose.add(unit.getName());
        }
        if (listNameChoose.size() == 0) {
            Toast.makeText(this, "Please choose Hero", Toast.LENGTH_SHORT).show();
            return;
        }
        Builder teamName = new Builder();
        teamName.setHero_name(listNameChoose);
        teamName.setName_team(name);
        for (int i = 0; i < builders.size(); i++) {
            if (name.equals(builders.get(i).getName_team()) && idEdit == -998) {
                Toast.makeText(this, "Name is existed", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        List<String> typeList = new ArrayList<>();
        for (UnitsInfo unitsInfo : unitsInfos) {
            if (!unitsInfo.getDes().equals("")) {
                typeList.add(unitsInfo.getCount() + " " + unitsInfo.getName());
            }
        }
        if(typeList.size()>0) teamName.setType(Utils.linkStringFromArray2(typeList));
        if (idEdit == -998) {
            builders.add(teamName);
        } else {
            builders.set(idEdit, teamName);
        }
        Preference.save(this, Contants.KEY_BUILDER_LIST_CHOOSE_NAME, Utils.convertObjToJson(builders));
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

}
