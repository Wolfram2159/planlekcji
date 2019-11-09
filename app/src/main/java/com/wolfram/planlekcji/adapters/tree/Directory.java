package com.wolfram.planlekcji.adapters.tree;

import com.wolfram.planlekcji.database.room.entities.notes.TextNote;

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
        if (childrenList != null && childrenList.size() != 0) {
            return childrenList.get(0).getGridSpanCount();
        }
        return 1;
    }

    @Override
    public String getPath() {
        return parent.getPath() + " -> " + this.nodeName;
    }
}