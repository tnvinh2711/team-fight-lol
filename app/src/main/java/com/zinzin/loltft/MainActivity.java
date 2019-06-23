package com.zinzin.loltft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.zinzin.loltft.adapter.ViewPagerAdapter;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }


    private void initUI() {
        final View view = findViewById(R.id.divider_view);
        final ViewPager viewPager = findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), this));
        final String[] colors = getResources().getStringArray(R.array.default_preview);
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
                        .title("Info")
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
}
