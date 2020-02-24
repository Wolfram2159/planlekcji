package com.wolfram.planlekcji.ui.adapters.tree;

import androidx.annotation.Nullable;

import com.wolfram.planlekcji.database.room.entities.SubjectEntity;

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

    public TreeNode(String nodeName) {
        childrenList = new ArrayList<>();
        this.nodeName = nodeName;
    }

    public TreeNode getNodeFromTree(TreeNode searchingNode) {
        TreeNode root = getRoot();
        List<TreeNode> allNodes = getAllNodes(root);
        return searchNode(allNodes, searchingNode);
    }

    private TreeNode getRoot() {
        TreeNode parent = this;
        while (!parent.isRoot()) {
            parent = parent.getParent();
        }
        return parent;
    }

    public boolean isRoot() {
        return this instanceof RootNode;
    }

    private List<TreeNode> getAllNodes(TreeNode root) {
        List<TreeNode> allNodes = new ArrayList<>();
        allNodes.add(root);
        allNodes.addAll(root.getAllChildrens());
        return allNodes;
    }

    private List<TreeNode> getAllChildrens() {
        List<TreeNode> childrenList = getChildrenList();
        List<TreeNode> childrens = new ArrayList<>(childrenList);
        if (childrenList.size() != 0) {
            for (TreeNode children : childrenList) {
                List<TreeNode> subChildrens = children.getAllChildrens();
                childrens.addAll(subChildrens);
            }
        }
        return childrens;
    }

    private TreeNode searchNode(List<TreeNode> allNodes, TreeNode searchingNode) {
        for (TreeNode nodeFromTree : allNodes) {
            if (nodeFromTree.equals(searchingNode)) return nodeFromTree;
        }
        return getRoot();
    }

    public boolean equals(TreeNode treeNode) {
        String thisPath = getPath();
        String nodePath = treeNode.getPath();
        return thisPath.equals(nodePath);
    }

    public void addChildren(TreeNode treeNode) {
        treeNode.setParent(this);
        childrenList.add(treeNode);
    }

    protected boolean isSubjectNode() {
        return this instanceof SubjectNode;
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
