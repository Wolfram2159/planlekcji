package com.wolfram.planlekcji.common.enums;

public enum TextViewType {
    DayPicker(false, true),
    SubjectPicker(true, false);

    private final boolean isEditable;
    private final boolean isSelectedFirstValue;

    TextViewType(boolean isEditable, boolean isSelectedFirstValue) {
        this.isEditable = isEditable;
        this.isSelectedFirstValue = isSelectedFirstValue;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public boolean isSelectedFirstValue() {
        return isSelectedFirstValue;
    }
}
