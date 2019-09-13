package com.wolfram.planlekcji.ui.fragments.grades;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolfram.planlekcji.R;

import androidx.fragment.app.Fragment;

public class GradesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grades, container, false);
    }
}