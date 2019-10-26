package com.wolfram.planlekcji.adapters.tree;

import java.util.ArrayList;

public class Root extends TreeNode {

    public Root() {
        this.parent = null;
        this.childrenList = new ArrayList<>();
        this.nodeName = "PlanLekcji";
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public int getGridSpanCount() {
        return 1;
    }

    @Override
    public String getPath() {
        return this.nodeName;
    }
}
