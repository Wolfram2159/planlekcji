package com.wolfram.planlekcji.adapters.tree;

import java.util.ArrayList;

public class Root extends TreeNode {

    public Root() {
        this.parent = null;
        this.childrenList = new ArrayList<>();
        this.nodeName = "root";
    }

    @Override
    public String getPath() {
        return this.nodeName;
    }
}
