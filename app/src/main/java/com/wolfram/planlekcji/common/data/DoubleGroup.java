package com.wolfram.planlekcji.common.data;

import java.util.List;

public interface DoubleGroup<T, V> {
    String getTitle();
    List<T> getFirstList();
    List<V> getSecondList();
}
