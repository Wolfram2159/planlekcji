package com.wolfram.planlekcji.common.data;

import java.util.List;

public interface Group<T> {
    String getTitle();
    List<T> getList();
}
