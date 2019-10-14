package com.wolfram.planlekcji.adapters.tree;

/**
 * @author Wolfram
 * @date 2019-10-14
 */
public class Directory extends TreeNode {
    @Override
    public String getPath() {
        if (this.parent == null){
            return "root";
        }else {
            return parent.getPath() + "->" + this.nodeName;
        }
    }
}