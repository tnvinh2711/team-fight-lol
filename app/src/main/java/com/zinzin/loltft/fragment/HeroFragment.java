package com.zinzin.loltft.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zinzin.loltft.DetailActivity;
import com.zinzin.loltft.OnDataReceiveCallback;
import com.zinzin.loltft.R;
import com.zinzin.loltft.adapter.HeaderRecyclerViewSection;
import com.zinzin.loltft.model.Origin;
import com.zinzin.loltft.model.Type;
import com.zinzin.loltft.model.Unit;
import com.zinzin.loltft.utils.Preference;
import com.zinzin.loltft.utils.WelcomeDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class HeroFragment extends Fragment {
    public static String TAG = HeroFragment.class.getSimpleName();
    private RecyclerView rvUnits;
    private SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
    private LinearLayout llLoading;
    private List<Unit> heroList = new ArrayList<>();
    private List<Unit> heroList_s = new ArrayList<>();
    private List<Unit> heroList_a = new ArrayList<>();
    private List<Unit> heroList_b = new ArrayList<>();
    private List<Unit> heroList_c = new ArrayList<>();
    private List<Unit> heroList_d = new ArrayList<>();
    private List<Unit> heroList_e = new ArrayList<>();
    private List<Unit> heroList_f = new ArrayList<>();
    private List<Origin> classList = new ArrayList<>();
    private List<Origin> originList = new ArrayList<>();
    private List<Unit> heroListFilter = new ArrayList<>();
    private EditText edtSearch;
    private ImageView ivFilter, ivInfo;
    private Dialog mBottomSheetDialog;
    private boolean isFilter = false;
    private String[] listOrigin;
    private String[] listClass;

    private String tierSelected = "";
    private String costSelected = "";
    private String classSelected = "";
    private String originSelected = "";

    private WelcomeDialog welcomeDialog;
    private boolean isLoadAd, isLoadClickAd;
    private int clickitem = 0;
    private InterstitialAd mInterstitialAd, mInterstitialAdClick;

    public static HeroFragment newInstance() {
        return new HeroFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hero, container, false);
        initView(view);
        loadAd();
        setUpDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData(new OnDataReceiveCallback() {
                    @Override
                    public void onDataReceived() {
                        llLoading.setVisibility(View.GONE);
                        rvUnits.setVisibility(View.VISIBLE);
                        setUpRecycleView();
                        setUpEditText();
                        setUpBottomSheetDialog();
                        setUpFilter();
                    }
                });
            }
        }, 200);
        return view;
    }

    private void loadAd() {
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAdClick = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-5796098881172039/1121152663");
        mInterstitialAdClick.setAdUnitId("ca-app-pub-5796098881172039/2309686229");
        mInterstitialAdClick.loadAd(new AdRequest.Builder().build());
        if (Preference.getBoolean(getActivity(), "firstrun", true)) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        } else {
            long timeOld = Preference.getLong(getActivity(), "Time", 0);
            long timeNew = System.currentTimeMillis();
            if (timeOld != 0 && timeNew - timeOld >= 21600000) {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            } else {
                if (!Preference.getBoolean(getActivity(), "LoadAds", false)) {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                isLoadAd = true;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Preference.getBoolean(getActivity(), "LoadAds", false);
                isLoadAd = false;
            }

        });
        mInterstitialAdClick.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                isLoadClickAd = true;
            }

            @Override
            public void onAdClosed() {
                mInterstitialAdClick.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdFailedToLoad(int i) {
                mInterstitialAdClick.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private void initView(View view) {
        rvUnits = view.findViewById(R.id.rcv_units);
        edtSearch = view.findViewById(R.id.edt_search);
        ivFilter = view.findViewById(R.id.iv_filter);
        ivInfo = view.findViewById(R.id.iv_info);
        llLoading = view.findViewById(R.id.ll_loading);
        llLoading.setVisibility(View.VISIBLE);
        rvUnits.setVisibility(View.GONE);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (welcomeDialog != null && !welcomeDialog.isShowing()) welcomeDialog.show();
            }
        });
    }

    private void setUpDialog() {
        welcomeDialog = new WelcomeDialog(getActivity(), new WelcomeDialog.DialogCallBack() {
            @Override
            public void onClickOpen() {
                if (isLoadClickAd) {
                    mInterstitialAdClick.show();
                    isLoadClickAd = false;
                } else {
                    Toast.makeText(getActivity(), "Không có quảng cáo, xin vui lòng mở sau", Toast.LENGTH_SHORT).show();
                }
            }
        });
        welcomeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        getListSection(heroListFilter);
        sectionAdapter.notifyDataSetChanged();
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

    private void getListSection(List<Unit> heroList) {
        heroList_s.clear();
        heroList_a.clear();
        heroList_b.clear();
        heroList_c.clear();
        heroList_d.clear();
        heroList_e.clear();
        heroList_f.clear();
        for (Unit unit : heroList) {
            switch (unit.getTier()) {
                case "S":
                    heroList_s.add(unit);
                    break;
                case "A":
                    heroList_a.add(unit);
                    break;
                case "B":
                    heroList_b.add(unit);
                    break;
                case "C":
                    heroList_c.add(unit);
                    break;
                case "D":
                    heroList_d.add(unit);
                    break;
                case "E":
                    heroList_e.add(unit);
                    break;
                case "F":
                    heroList_f.add(unit);
                    break;

            }
        }
    }

    private void setUpRecycleView() {
        GridLayoutManager adapterManager = new GridLayoutManager(getActivity(), 3);
        getListSection(heroList);
        HeaderRecyclerViewSection viewSection_S = new HeaderRecyclerViewSection(getActivity(), "Tier S", heroList_s, R.color.color_tier_s);
        viewSection_S.setListener(new HeaderRecyclerViewSection.OnItemClickListener() {
            @Override
            public void OnItemClick(Unit item, int position) {
                clickitem++;
                if (isLoadAd && clickitem > 1) {
                    Preference.save(getActivity(), "firstrun", false);
                    Preference.save(getActivity(), "Time", System.currentTimeMillis());
                    Preference.save(getActivity(), "LoadAds", true);
                    mInterstitialAd.show();
                    isLoadAd = false;
                } else {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("name", item.getName());
                    startActivity(intent);
                }
            }
        });
        HeaderRecyclerViewSection viewSection_A = new HeaderRecyclerViewSection(getActivity(), "Tier A", heroList_a, R.color.color_tier_a);
        viewSection_A.setListener(new HeaderRecyclerViewSection.OnItemClickListener() {
            @Override
            public void OnItemClick(Unit item, int position) {
                clickitem++;
                if (isLoadAd && clickitem > 1) {
                    Preference.save(getActivity(), "firstrun", false);
                    Preference.save(getActivity(), "Time", System.currentTimeMillis());
                    Preference.save(getActivity(), "LoadAds", true);
                    mInterstitialAd.show();
                    isLoadAd = false;
                } else {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("name", item.getName());
                    startActivity(intent);
                }
            }
        });
        HeaderRecyclerViewSection viewSection_B = new HeaderRecyclerViewSection(getActivity(), "Tier B", heroList_b, R.color.color_tier_b);
        viewSection_B.setListener(new HeaderRecyclerViewSection.OnItemClickListener() {
            @Override
            public void OnItemClick(Unit item, int position) {
                clickitem++;
                if (isLoadAd && clickitem > 1) {
                    Preference.save(getActivity(), "firstrun", false);
                    Preference.save(getActivity(), "Time", System.currentTimeMillis());
                    Preference.save(getActivity(), "LoadAds", true);
                    mInterstitialAd.show();
                    isLoadAd = false;
                } else {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("name", item.getName());
                    startActivity(intent);
                }
            }
        });
        HeaderRecyclerViewSection viewSection_C = new HeaderRecyclerViewSection(getActivity(), "Tier C", heroList_c, R.color.color_tier_c);
        viewSection_C.setListener(new HeaderRecyclerViewSection.OnItemClickListener() {
            @Override
            public void OnItemClick(Unit item, int position) {
                clickitem++;
                if (isLoadAd && clickitem > 1) {
                    Preference.save(getActivity(), "firstrun", false);
                    Preference.save(getActivity(), "Time", System.currentTimeMillis());
                    Preference.save(getActivity(), "LoadAds", true);
                    mInterstitialAd.show();
                    isLoadAd = false;
                } else {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("name", item.getName());
                    startActivity(intent);
                }
            }
        });
        HeaderRecyclerViewSection viewSection_D = new HeaderRecyclerViewSection(getActivity(), "Tier D", heroList_d, R.color.color_tier_d);
        viewSection_D.setListener(new HeaderRecyclerViewSection.OnItemClickListener() {
            @Override
            public void OnItemClick(Unit item, int position) {
                clickitem++;
                if (isLoadAd && clickitem > 1) {
                    Preference.save(getActivity(), "firstrun", false);
                    Preference.save(getActivity(), "Time", System.currentTimeMillis());
                    Preference.save(getActivity(), "LoadAds", true);
                    mInterstitialAd.show();
                    isLoadAd = false;
                } else {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("name", item.getName());
                    startActivity(intent);
                }
            }
        });
        HeaderRecyclerViewSection viewSection_E = new HeaderRecyclerViewSection(getActivity(), "Tier E", heroList_e, R.color.color_tier_e);
        viewSection_E.setListener(new HeaderRecyclerViewSection.OnItemClickListener() {
            @Override
            public void OnItemClick(Unit item, int position) {
                clickitem++;
                if (isLoadAd && clickitem > 1) {
                    Preference.save(getActivity(), "firstrun", false);
                    Preference.save(getActivity(), "Time", System.currentTimeMillis());
                    Preference.save(getActivity(), "LoadAds", true);
                    mInterstitialAd.show();
                    isLoadAd = false;
                } else {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("name", item.getName());
                    startActivity(intent);
                }
            }
        });
        HeaderRecyclerViewSection viewSection_F = new HeaderRecyclerViewSection(getActivity(), "Tier F", heroList_f, R.color.color_tier_f);
        viewSection_F.setListener(new HeaderRecyclerViewSection.OnItemClickListener() {
            @Override
            public void OnItemClick(Unit item, int position) {
                clickitem++;
                if (isLoadAd && clickitem > 1) {
                    Preference.save(getActivity(), "firstrun", false);
                    Preference.save(getActivity(), "Time", System.currentTimeMillis());
                    Preference.save(getActivity(), "LoadAds", true);
                    mInterstitialAd.show();
                    isLoadAd = false;
                } else {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("name", item.getName());
                    startActivity(intent);
                }

            }
        });
        sectionAdapter.addSection(viewSection_S);
        sectionAdapter.addSection(viewSection_A);
        sectionAdapter.addSection(viewSection_B);
        sectionAdapter.addSection(viewSection_C);
        sectionAdapter.addSection(viewSection_D);
        sectionAdapter.addSection(viewSection_E);
        sectionAdapter.addSection(viewSection_F);
        adapterManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (sectionAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        int orientation = getResources().getConfiguration().orientation;
                        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            return 5;
                        } else {
                            return 3;
                        }
                    default:
                        return 1;
                }
            }
        });
        rvUnits.setLayoutManager(adapterManager);
        rvUnits.setAdapter(sectionAdapter);
    }

    private void getData(final OnDataReceiveCallback callback) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tft_db").child("unit");
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
                callback.onDataReceived();
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
            getListSection(unitsBase);
            sectionAdapter.notifyDataSetChanged();
        } else {
            getListSection(listUnitsFilter);
            sectionAdapter.notifyDataSetChanged();
        }
    }

}
