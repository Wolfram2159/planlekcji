package com.wolfram.planlekcji.ui.bottomSheets.events;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.events.ViewPagerEventsFragmentViewModel;

import androidx.lifecycle.ViewModelProviders;

/**
 * @author Wolfram
 * @date 2019-08-28
 */
public class ActionEventBottomSheet extends CustomBottomSheet {

    private ViewPagerEventsFragmentViewModel viewModel;
    private boolean isEditedBtnClicked = false;

    @Override
    protected int getResource() {
        return R.layout.action_bottom_sheet;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isEditedBtnClicked) {
            viewModel.setModifiedEvent(null);
        }
    }

    @Override
    protected void customizeDialog() {
        MaterialButton edit = root.findViewById(R.id.action_edit_btn);
        MaterialButton delete = root.findViewById(R.id.action_delete_btn);

        viewModel = ViewModelProviders.of(getActivity()).get(ViewPagerEventsFragmentViewModel.class);

        edit.setOnClickListener(view -> {
            ModifyEventBottomSheet modifyEventBottomSheet = new ModifyEventBottomSheet();
            modifyEventBottomSheet.show(getFragmentManager(), "EditBottomSheet");
            isEditedBtnClicked = true;
            dismiss();
        });

        delete.setOnClickListener(view -> {
            viewModel.deleteEvent();
            dismiss();
        });
    }
}
