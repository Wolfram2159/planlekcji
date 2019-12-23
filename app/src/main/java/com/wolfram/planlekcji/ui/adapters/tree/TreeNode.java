package com.wolfram.planlekcji.ui.adapters.tree;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Ignore;

/**
 * @author Wolfram
 * @date 2019-10-14
 */
public abstract class TreeNode implements TreeAdapter.TreeAdapterParent{
    @Ignore
    protected List<TreeNode> childrenList;
    @Ignore
    protected String nodeName;
    @Ignore
    protected TreeNode parent;

    public TreeNode() {
        childrenList = new ArrayList<>();
    }

    public void addChildren(TreeNode treeNode, String nodeName) {
        treeNode.setNodeName(nodeName);
        treeNode.setParent(this);
        childrenList.add(treeNode);
    }

    public void clearChildrens(){
        childrenList.clear();
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

    // TODO: 2019-12-23 write implementation of this method there
    public abstract String getPath();
}
