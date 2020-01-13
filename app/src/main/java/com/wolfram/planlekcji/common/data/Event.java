package com.wolfram.planlekcji.common.data;

public class Event<T> {
    private T value;
    private boolean used;

    public Event() {
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
