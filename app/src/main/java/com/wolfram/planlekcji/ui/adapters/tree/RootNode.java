package com.wolfram.planlekcji.ui.adapters.tree;


public class RootNode extends TreeNode {

    private final static int VIEW_TYPE = 0;
    private final static int GRID_SPAN_COUNT = 1;

    public RootNode() {
        super();
        this.parent = null;
        this.nodeName = "PlanLekcji";
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE;
    }

    @Override
    public int getGridSpanCount() {
        return GRID_SPAN_COUNT;
    }
}
