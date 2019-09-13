package com.wolfram.planlekcji.adapters;

import android.os.Bundle;

import com.wolfram.planlekcji.ui.fragments.events.PagerAdapterFragment;
import com.wolfram.planlekcji.utils.enums.Day;
import com.wolfram.planlekcji.utils.enums.ShortDay;

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
    public Fragment getItem(int i) {
        Fragment fragment = new PagerAdapterFragment();
        Bundle args = new Bundle();
        args.putInt(PagerAdapterFragment.POSITION, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return Day.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ShortDay.values()[position].toString();
    }
}
