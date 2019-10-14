package com.wolfram.planlekcji.adapters.tree;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Ignore;

/**
 * @author Wolfram
 * @date 2019-10-14
 */
public abstract class TreeNode {
    @Ignore
    protected List<TreeNode> childrenList;
    @Ignore
    protected String nodeName;
    @Ignore
    protected TreeNode parent;

    public void addChildren(TreeNode treeNode, String nodeName) {
        if (childrenList == null) {
            childrenList = new ArrayList<>();
        }
        treeNode.setNodeName(nodeName);
        treeNode.setParent(this);
        childrenList.add(treeNode);
    }

    public List<TreeNode> getChildrenList() {
        return childrenList;
    }

    public String getNodeName() {
        return nodeName;
    }

    public TreeNode getParent() {
        return parent;
    }

    protected void setParent(TreeNode parent) {
        this.parent = parent;
    }

    protected void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public abstract String getPath();
}
