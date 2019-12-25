package com.wolfram.planlekcji.ui.adapters.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wolfram
 * @date 2019-10-14
 */
public abstract class TreeNode implements TreeAdapter.TreeAdapterParent {

    protected List<TreeNode> childrenList;
    protected String nodeName;
    protected TreeNode parent;

    public TreeNode() {
        childrenList = new ArrayList<>();
    }

    public void addChildren(TreeNode treeNode, String nodeName) {
        treeNode.setNodeName(nodeName);
        treeNode.setParent(this);
        childrenList.add(treeNode);
    }

    public void clearChildrens() {
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

    public String getPath() {
        if (parent == null) {
            return this.nodeName;
        } else {
            return this.parent.getPath() + " -> " + this.nodeName;
        }
    }
}
