package com.wolfram.planlekcji.ui.adapters.tree;

/**
 * @author Wolfram
 * @date 2019-10-14
 */
public class DirectoryNode extends TreeNode {

    private static final int VIEW_TYPE = 2;
    private static final int GRID_SPAN_COUNT = 1;

    public DirectoryNode() {
        super();
    }

    public DirectoryNode(String nodeName) {
        super(nodeName);
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE;
    }

    @Override
    public int getGridSpanCount() {
        if (childrenList != null && childrenList.size() != 0) {
            return childrenList.get(0).getGridSpanCount();
        }
        return GRID_SPAN_COUNT;
    }
}