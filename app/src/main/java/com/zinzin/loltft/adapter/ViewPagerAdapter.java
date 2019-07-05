package com.zinzin.loltft.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zinzin.loltft.fragment.BuilderFragment;
import com.zinzin.loltft.fragment.HeroFragment;
import com.zinzin.loltft.fragment.IAPFragment;
import com.zinzin.loltft.fragment.InfoFragment;
import com.zinzin.loltft.fragment.ItemFragment;
import com.zinzin.loltft.fragment.RoundFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] childFragments;
    private HeroFragment heroFragment;
    private RoundFragment roundFragment;
    private ItemFragment itemFragment;
    private BuilderFragment builderFragment;
    private InfoFragment infoFragment;
    private IAPFragment iapFragment;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        heroFragment = HeroFragment.newInstance();
        roundFragment = RoundFragment.newInstance();
        itemFragment = ItemFragment.newInstance();
        builderFragment = BuilderFragment.newInstance();
        infoFragment = InfoFragment.newInstance();
        iapFragment = IAPFragment.newInstance();
        childFragments = new Fragment[] {
                itemFragment,
                roundFragment,
                heroFragment,
                builderFragment,
                infoFragment,
                iapFragment
        };
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
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
