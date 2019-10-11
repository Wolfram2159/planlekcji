package com.wolfram.planlekcji.ui.bottomSheets.notes;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.notes.NotesFragmentViewModel;

import androidx.lifecycle.ViewModelProviders;

/**
 * @author Wolfram
 * @date 2019-10-11
 */
public class AddImageBottomSheet extends CustomBottomSheet {

    private NotesFragmentViewModel viewModel;

    @Override
    protected int getResource() {
        return R.layout.notes_image_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        viewModel = ViewModelProviders.of(getActivity()).get(NotesFragmentViewModel.class);


    }
}
