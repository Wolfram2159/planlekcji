package com.wolfram.planlekcji.common.enums;

public enum ViewType {
    DayPicker(false, true),
    SubjectPicker(true, false),
    NoEditableSubjectPicker(false, false),
    DatePicker(false),
    TimePicker(false);

    private final boolean isEditable;
    private final boolean isSelectedFirstValue;

    ViewType(boolean isEditable) {
        this.isEditable = isEditable;
        this.isSelectedFirstValue = false;
    }

    ViewType(boolean isEditable, boolean isSelectedFirstValue) {
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
