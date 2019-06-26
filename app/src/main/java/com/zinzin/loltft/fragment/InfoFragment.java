package com.zinzin.loltft.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zinzin.loltft.R;
import com.zinzin.loltft.adapter.ItemAdapter;
import com.zinzin.loltft.model.Item;

public class InfoFragment extends Fragment {

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    int show_ads = 1;
    private LinearLayout llAds, llAdsShow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        llAds = view.findViewById(R.id.ll_ads);
        llAdsShow = view.findViewById(R.id.ll_ads_show);
        YoYo.with(Techniques.Bounce)
                .duration(1000)
                .repeat(100000)
                .playOn(llAds);
        llAdsShow.setVisibility(View.GONE);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tft_db").child("show_ads");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    show_ads = dataSnapshot.getValue(Integer.class);
                    switch (show_ads) {
                        case 0:
                            llAdsShow.setVisibility(View.GONE);
                            break;
                        case 1:
                            llAdsShow.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        return view;
    }
}
