package com.wolfram.planlekcji.adapters;

import android.os.Bundle;

import com.wolfram.planlekcji.ui.activities.SubjectsViewModel;
import com.wolfram.planlekcji.ui.fragments.SubjectsFragment;
import com.wolfram.planlekcji.utils.enums.Day;
import com.wolfram.planlekcji.utils.enums.ShortDay;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * @author Wolfram
 * @date 2019-08-10
 */
public class SubjectsPagerAdapter extends FragmentStatePagerAdapter {

    private SubjectsViewModel viewModel;

    public SubjectsPagerAdapter(FragmentManager fm, SubjectsViewModel viewModel) {
        super(fm);
        this.viewModel = viewModel;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new SubjectsFragment(viewModel);
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(SubjectsFragment.POSITION, i);
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