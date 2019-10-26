package com.wolfram.planlekcji.adapters.tree;

/**
 * @author Wolfram
 * @date 2019-10-14
 */
public class Directory extends TreeNode {

    @Override
    public int getViewType() {
        return 2;
    }

    @Override
    public int getGridSpanCount() {
        return 3;
    }

    @Override
    public String getPath() {
        return parent.getPath() + " -> " + this.nodeName;
    }
}