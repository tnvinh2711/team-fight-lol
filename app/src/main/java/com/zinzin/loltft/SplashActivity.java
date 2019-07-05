package com.zinzin.loltft;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.zinzin.loltft.utils.IAP;
import com.zinzin.loltft.utils.Preference;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private boolean isPay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        YoYo.with(Techniques.Tada)
                .onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        final BillingClient billingClient = BillingClient.newBuilder(SplashActivity.this).enablePendingPurchases().setListener(new PurchasesUpdatedListener() {
                            @Override
                            public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
                            }
                        }).build();
                        billingClient.startConnection(new BillingClientStateListener() {
                            @Override
                            public void onBillingSetupFinished(BillingResult billingResult) {
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                    if (billingClient.isReady()) {
                                        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, new PurchaseHistoryResponseListener() {
                                            @Override
                                            public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> purchaseHistoryRecordList) {
                                                if (purchaseHistoryRecordList != null) {
                                                    for (PurchaseHistoryRecord purchaseHistoryRecord : purchaseHistoryRecordList) {
                                                        Log.e("json", purchaseHistoryRecord.getOriginalJson());
                                                        Gson gson = new Gson();
                                                        IAP iap = gson.fromJson(purchaseHistoryRecord.getOriginalJson(), IAP.class);
                                                        if (iap.getProductId().equals("zinzin_premium")) {
                                                            isPay = true;
                                                            Preference.save(SplashActivity.this, "isPay", isPay);
                                                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                            return;
                                                        } else {
                                                            isPay = false;
                                                        }
                                                    }
                                                } else {
                                                    isPay = false;
                                                }
                                                Preference.save(SplashActivity.this, "isPay", isPay);
                                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onBillingServiceDisconnected() {
                            }
                        });
                    }
                })
                .duration(1000)
                .repeat(1)
                .playOn(findViewById(R.id.iv_logo));
    }
}
