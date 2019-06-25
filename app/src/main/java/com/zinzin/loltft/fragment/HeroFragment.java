package com.zinzin.loltft.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zinzin.loltft.DetailActivity;
import com.zinzin.loltft.R;
import com.zinzin.loltft.adapter.HeroAdapter;
import com.zinzin.loltft.model.Origin;
import com.zinzin.loltft.model.Type;
import com.zinzin.loltft.model.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HeroFragment extends Fragment {
    public static String TAG = HeroFragment.class.getSimpleName();
    private RecyclerView rvUnits;
    private HeroAdapter heroAdapter;
    List<Unit> heroList = new ArrayList<>();
    List<Origin> classList = new ArrayList<>();
    List<Origin> originList = new ArrayList<>();
    private List<Unit> heroListFilter = new ArrayList<>();
    private EditText edtSearch;
    private ImageView ivFilter;
    private Dialog mBottomSheetDialog;
    private boolean isFilter = false;
    private String[] listOrigin;
    private String[] listClass;

    private String tierSelected = "";
    private String costSelected = "";
    private String classSelected = "";
    private String originSelected = "";

    public static HeroFragment newInstance() {
        return new HeroFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hero, container, false);
        getData();
        initView(view);
        return view;
    }

    public void setData(List<Unit> heroes) {
        heroAdapter.updateList(heroes);
    }

    private void initView(View view) {
        rvUnits = view.findViewById(R.id.rcv_units);
        edtSearch = view.findViewById(R.id.edt_search);
        ivFilter = view.findViewById(R.id.iv_filter);
    }

    private void setUpBottomSheetDialog() {
        mBottomSheetDialog = new Dialog(getActivity(), R.style.AppTheme);
        mBottomSheetDialog.setCancelable(true);
        final View sheetView = getActivity().getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);

        final ChipCloud chipCloudStatus = sheetView.findViewById(R.id.chip_cloud_tier);
        final ChipCloud chipCloudCost = sheetView.findViewById(R.id.chip_cloud_cost);
        final ChipCloud chipCloudClass = sheetView.findViewById(R.id.chip_cloud_class);
        final ChipCloud chipCloudRace = sheetView.findViewById(R.id.chip_cloud_race);
        Button btnDone = sheetView.findViewById(R.id.btn_done);
        TextView tvReset = sheetView.findViewById(R.id.tv_reset);
        final String[] listCost = new String[]{"$1", "$2", "$3", "$4", "$5"};
        final String[] listStatus = new String[]{"S", "A", "B", "C", "D", "E", "F"};
        listOrigin = new String[originList.size()];
        listClass = new String[classList.size()];
        for (int i = 0; i < originList.size(); i++) {
            listOrigin[i] = originList.get(i).getName();
        }
        for (int i = 0; i < classList.size(); i++) {
            listClass[i] = classList.get(i).getName();
        }

        chipCloudStatus.addChips(listStatus);
        chipCloudCost.addChips(listCost);
        chipCloudClass.addChips(listClass);
        chipCloudRace.addChips(listOrigin);
        chipCloudStatus.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                tierSelected = listStatus[index];
            }

            @Override
            public void chipDeselected(int index) {
                tierSelected = "";
            }
        });
        chipCloudCost.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                costSelected = listCost[index];
            }

            @Override
            public void chipDeselected(int index) {
                costSelected = "";
            }
        });
        chipCloudClass.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                classSelected = (listClass[index]);
            }

            @Override
            public void chipDeselected(int index) {
                classSelected = "";
            }
        });
        chipCloudRace.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                originSelected = listOrigin[index];
            }

            @Override
            public void chipDeselected(int index) {
                originSelected = "";
            }
        });
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipCloudStatus.setMode(ChipCloud.Mode.NONE);
                chipCloudCost.setMode(ChipCloud.Mode.NONE);
                chipCloudClass.setMode(ChipCloud.Mode.NONE);
                chipCloudRace.setMode(ChipCloud.Mode.NONE);
                chipCloudRace.setMode(ChipCloud.Mode.NONE);
                chipCloudStatus.setMode(ChipCloud.Mode.SINGLE);
                chipCloudCost.setMode(ChipCloud.Mode.SINGLE);
                chipCloudClass.setMode(ChipCloud.Mode.SINGLE);
                chipCloudRace.setMode(ChipCloud.Mode.SINGLE);
                chipCloudRace.setMode(ChipCloud.Mode.SINGLE);
                tierSelected = "";
                costSelected = "";
                classSelected = "";
                originSelected = "";

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
                getListFilter();
            }
        });
        mBottomSheetDialog.setContentView(sheetView);
    }

    private void getListFilter() {
        heroListFilter.clear();
        if (!tierSelected.equals("") || !costSelected.equals("") || !classSelected.equals("") || !originSelected.equals("")) {
            isFilter = true;
            if (!tierSelected.equals("")) {
                for (int i = 0; i < heroList.size(); i++) {
                    if (heroList.get(i).getTier().equals(tierSelected))
                        heroListFilter.add(heroList.get(i));
                }
            } else {
                heroListFilter.addAll(heroList);
            }
            if (!costSelected.equals("")) {
                for (int i = heroListFilter.size() - 1; i >= 0; i--) {
                    if (!heroListFilter.get(i).getCost().equals(costSelected))
                        heroListFilter.remove(i);
                }
            }
            if (!classSelected.equals("")) {
                for (int i = heroListFilter.size() - 1; i >= 0; i--) {
                    List<Type> type = heroListFilter.get(i).getType();
                    if (type.size() == 2) {
                        if (!type.get(0).getName().equals(classSelected) && !type.get(1).getName().equals(classSelected)) {
                            heroListFilter.remove(i);
                        }
                    } else if (type.size() == 3) {
                        if (!type.get(0).getName().equals(classSelected) && !type.get(1).getName().equals(classSelected) && !type.get(2).getName().equals(classSelected)) {
                            heroListFilter.remove(i);
                        }
                    } else if (type.size() == 4) {
                        if (!type.get(0).getName().equals(classSelected) && !type.get(1).getName().equals(classSelected) && !type.get(2).getName().equals(classSelected) && !type.get(3).getName().equals(classSelected)) {
                            heroListFilter.remove(i);
                        }
                    }
                }
            }
            if (!originSelected.equals("")) {
                for (int i = heroListFilter.size() - 1; i >= 0; i--) {
                    List<Type> type = heroListFilter.get(i).getType();
                    if (type.size() == 2) {
                        if (!type.get(0).getName().equals(originSelected) && !type.get(1).getName().equals(originSelected)) {
                            heroListFilter.remove(i);
                        }
                    } else if (type.size() == 3) {
                        if (!type.get(0).getName().equals(originSelected) && !type.get(1).getName().equals(originSelected) && !type.get(2).getName().equals(originSelected)) {
                            heroListFilter.remove(i);
                        }
                    } else if (type.size() == 4) {
                        if (!type.get(0).getName().equals(originSelected) && !type.get(1).getName().equals(originSelected) && !type.get(2).getName().equals(originSelected) && !type.get(3).getName().equals(originSelected)) {
                            heroListFilter.remove(i);
                        }
                    }
                }
            }
            Set<Unit> set = new HashSet<>(heroListFilter);
            heroListFilter.clear();
            heroListFilter.addAll(set);
        } else {
            //reset All
            isFilter = false;
            heroListFilter.addAll(heroList);

        }
        Collections.sort(heroListFilter, new Comparator<Unit>() {
            @Override
            public int compare(Unit lhs, Unit rhs) {

                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
        heroAdapter.updateList(heroListFilter);
    }


    private void setUpFilter() {
        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                mBottomSheetDialog.show();
            }
        });
    }

    private void setUpEditText() {
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
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("name",item.getName());
                startActivity(intent);
            }
        });
    }

    private void getData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tft_db");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.child("unitList").getChildren()) {
                        Unit unit = ds.getValue(Unit.class);
                        heroList.add(unit);
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
                setUpRecycleView();
                setData(heroList);
                setUpEditText();
                setUpBottomSheetDialog();
                setUpFilter();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void filter(String text) {
        List<Unit> unitsBase = new ArrayList<>();
        if (isFilter) {
            unitsBase.addAll(heroListFilter);
        } else {
            unitsBase.addAll(heroList);
        }
        List<Unit> listUnitsFilter = new ArrayList();
        for (Unit units : unitsBase) {
            //filter theo tên
            if (units.getName().toLowerCase().contains(text.toLowerCase())) {
                listUnitsFilter.add(units);
            }
        }
        if (text.equals("")) {
            listUnitsFilter.clear();
            heroAdapter.updateList(unitsBase);
        } else {
            heroAdapter.updateList(listUnitsFilter);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        GridLayoutManager adapterManager = null;
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            adapterManager = new GridLayoutManager(getActivity(), 5);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            adapterManager = new GridLayoutManager(getActivity(), 3);
        }
        rvUnits.setLayoutManager(adapterManager);
        heroAdapter.notifyDataSetChanged();
    }

}
