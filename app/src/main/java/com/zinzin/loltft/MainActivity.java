package com.zinzin.loltft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.zinzin.loltft.adapter.ViewPagerAdapter;

import java.util.ArrayList;

import am.appwise.components.ni.NoInternetDialog;
import devlight.io.library.ntb.NavigationTabBar;


public class MainActivity extends AppCompatActivity {
    private NoInternetDialog noInternetDialog;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-5796098881172039~7278199612");
        mAdView = findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
        mAdView.loadAd(new AdRequest.Builder().build());
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
                mAdView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_up));
            }

            @Override
            public void onAdOpened() {
                mAdView.setVisibility(View.GONE);
                mAdView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdView.loadAd(new AdRequest.Builder().build());
                    }
                }, 1800000);
            }
        });
        initUI();
    }


    private void initUI() {
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        final View view = findViewById(R.id.divider_view);
        final ViewPager viewPager = findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        final String[] colors = getResources().getStringArray(R.array.color);
        view.setBackgroundColor(Color.parseColor(colors[2]));
        final NavigationTabBar navigationTabBar = findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.items_unselect),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.items))
                        .title("Items")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.creeps_unselect),
                        Color.parseColor(colors[1]))
                        .selectedIcon(getResources().getDrawable(R.drawable.creeps))
                        .title("Rounds")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.units_unselect),
                        Color.parseColor(colors[2]))
                        .selectedIcon(getResources().getDrawable(R.drawable.units))
                        .title("Heroes")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.builder_unselect),
                        Color.parseColor(colors[3]))
                        .selectedIcon(getResources().getDrawable(R.drawable.builder))
                        .title("Builder")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.info_unselect),
                        Color.parseColor(colors[4]))
                        .selectedIcon(getResources().getDrawable(R.drawable.info))
                        .title("Teams")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.info_unselect),
                        Color.parseColor(colors[5]))
                        .selectedIcon(getResources().getDrawable(R.drawable.info))
                        .title("Ads")
                        .build()
        );
        navigationTabBar.setBgColor(R.color.colorPrimaryDark);
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 2);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                view.setBackgroundColor(Color.parseColor(colors[position]));
            }

            @Override
            public void onPageSelected(int position) {
                view.setBackgroundColor(Color.parseColor(colors[position]));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        if (noInternetDialog != null && noInternetDialog.isShowing()) noInternetDialog.dismiss();
        super.onDestroy();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 3000);
    }
}
