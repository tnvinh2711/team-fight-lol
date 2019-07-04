package com.zinzin.loltft.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
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
import com.zinzin.loltft.OnDataReceiveCallback;
import com.zinzin.loltft.R;
import com.zinzin.loltft.adapter.TeamAdapter;
import com.zinzin.loltft.model.Team;
import com.zinzin.loltft.utils.Preference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IAPFragment extends Fragment {
    private BillingClient billingClient;
    private LinearLayout llAds, llAdsShow;
    private Button btnIap;
    private InterstitialAd mInterstitialAdClick;
    private boolean isLoadClickAd;

    public static IAPFragment newInstance() {
        return new IAPFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_iap, container, false);
        loadAd();
        llAds = view.findViewById(R.id.ll_ads);
        llAdsShow = view.findViewById(R.id.ll_ads_show);
        btnIap = view.findViewById(R.id.btn_iap);
        btnIap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpBillingClient();
            }
        });
        YoYo.with(Techniques.Bounce)
                .duration(1000)
                .repeat(100000)
                .playOn(llAds);
        YoYo.with(Techniques.Pulse)
                .duration(1000)
                .repeat(100000)
                .playOn(btnIap);
        llAdsShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoadClickAd) {
                    mInterstitialAdClick.show();
                    isLoadClickAd = false;
                } else {
                    Toast.makeText(getActivity(), "Không có quảng cáo, xin vui lòng mở sau", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void setUpBillingClient() {
        billingClient = BillingClient.newBuilder(getActivity()).enablePendingPurchases().setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
                if (purchases != null && purchases.size() > 0) {
                    Toast.makeText(getActivity(), "Purchase Success, Restart app to remove ADS", Toast.LENGTH_LONG).show();
                }
            }
        }).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingClient.isReady()) {
                    SkuDetailsParams params = SkuDetailsParams.newBuilder()
                            .setSkusList(Arrays.asList("zinzin_premium"))
                            .setType(BillingClient.SkuType.INAPP)
                            .build();
                    billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetailsList.get(0)).build();
                                billingClient.launchBillingFlow(getActivity(), billingFlowParams);
                            }
                        }
                    });
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
//                Toast.makeText(getActivity(), "disconnect billing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAd() {
        mInterstitialAdClick = new InterstitialAd(getActivity());
        mInterstitialAdClick.setAdUnitId("ca-app-pub-5796098881172039/2309686229");
        mInterstitialAdClick.loadAd(new AdRequest.Builder().build());
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
}
