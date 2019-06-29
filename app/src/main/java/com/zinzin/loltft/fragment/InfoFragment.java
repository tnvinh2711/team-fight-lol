package com.zinzin.loltft.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import com.zinzin.loltft.R;
import com.zinzin.loltft.adapter.ItemAdapter;
import com.zinzin.loltft.model.Item;
import com.zinzin.loltft.utils.Preference;

public class InfoFragment extends Fragment {

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    private InterstitialAd mInterstitialAd;
    private boolean isLoadAd;
    private int show_ads = 1;
    private LinearLayout llAds, llAdsShow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        loadAd();
        llAds = view.findViewById(R.id.ll_ads);
        llAdsShow = view.findViewById(R.id.ll_ads_show);
        llAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoadAd) {
                    mInterstitialAd.show();
                } else {
                    Toast.makeText(getActivity(), "Not have ads, please try later!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void loadAd() {
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-5796098881172039/2309686229");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                isLoadAd = true;
            }

            @Override
            public void onAdClosed() {
                isLoadAd = false;
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdFailedToLoad(int i) {
                isLoadAd = false;
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }
}
