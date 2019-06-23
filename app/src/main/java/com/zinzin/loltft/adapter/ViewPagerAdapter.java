package com.zinzin.loltft.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zinzin.loltft.fragment.BuilderFragment;
import com.zinzin.loltft.fragment.HeroFragment;
import com.zinzin.loltft.fragment.InfoFragment;
import com.zinzin.loltft.fragment.ItemFragment;
import com.zinzin.loltft.fragment.RoundFragment;
import com.zinzin.loltft.model.Detail;
import com.zinzin.loltft.model.Item;
import com.zinzin.loltft.model.Round;
import com.zinzin.loltft.model.Unit;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] childFragments;
    private HeroFragment heroFragment;
    private RoundFragment roundFragment;
    private ItemFragment itemFragment;
    private BuilderFragment builderFragment;
    private InfoFragment infoFragment;
    List<Unit> heroList = new ArrayList<>();
    List<Item> itemList = new ArrayList<>();
    List<Round> roundList = new ArrayList<>();
    List<Detail> detailList = new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        heroFragment = HeroFragment.newInstance();
        roundFragment = RoundFragment.newInstance();
        itemFragment = ItemFragment.newInstance();
        builderFragment = BuilderFragment.newInstance();
        infoFragment = InfoFragment.newInstance();
        childFragments = new Fragment[] {
                itemFragment,
                roundFragment,
                heroFragment,
                builderFragment,
                infoFragment
        };
        getData();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
    private void getData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("tft_db");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("unitList").getChildren()) {
                    Unit unit = ds.getValue(Unit.class);
                    heroList.add(unit);
                }
                for (DataSnapshot ds : dataSnapshot.child("itemList").getChildren()) {
                    Item item = ds.getValue(Item.class);
                    itemList.add(item);
                }
                for (DataSnapshot ds : dataSnapshot.child("roundList").getChildren()) {
                    Round round = ds.getValue(Round.class);
                    roundList.add(round);
                }
                heroFragment.setData(heroList);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = getItem(position).getClass().getName();
        return title.subSequence(title.lastIndexOf(".") + 1, title.length());
    }
}
