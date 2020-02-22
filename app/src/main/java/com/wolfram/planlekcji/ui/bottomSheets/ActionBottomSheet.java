package com.wolfram.planlekcji.ui.bottomSheets;

import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActionBottomSheet extends CustomBottomSheet implements View.OnClickListener {

    private OnActionListener onActionListener;
    @BindView(R.id.action_edit_btn)
    MaterialButton editButton;
    @BindView(R.id.action_delete_btn)
    MaterialButton deleteButton;

    public interface OnActionListener {
        void onObjectModify();

        void onObjectDelete();
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    @Override
    protected int getResource() {
        return R.layout.action_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        ButterKnife.bind(this, root);

        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (onActionListener != null) {
            switch (view.getId()) {
                case R.id.action_edit_btn:
                    onActionListener.onObjectModify();
                    break;
                case R.id.action_delete_btn:
                    onActionListener.onObjectDelete();
                    break;
            }
        }
        dismiss();
    }
}
