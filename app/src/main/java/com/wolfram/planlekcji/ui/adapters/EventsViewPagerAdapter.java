package com.wolfram.planlekcji.ui.adapters;

import android.os.Bundle;

import com.wolfram.planlekcji.ui.fragments.events.PagerAdapterFragment;
import com.wolfram.planlekcji.common.enums.Day;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * @author Wolfram
 * @date 2019-08-10
 */
public class EventsViewPagerAdapter extends FragmentStatePagerAdapter {


    public EventsViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PagerAdapterFragment();
        Bundle args = new Bundle();
        args.putInt(PagerAdapterFragment.POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return Day.getDaysCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Day.getShortNameOfDay(position);
    }
}
