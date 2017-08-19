package com.efimchick.jexplorer.navigation.ui.structure;

import com.efimchick.jexplorer.navigation.Directory;

import javax.swing.tree.TreeNode;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class DirectoryTreeNode implements TreeNode {

    private final Directory directory;
    private final DirectoryTreeNode parent;

    public DirectoryTreeNode(Directory directory, DirectoryTreeNode parent) {
        this.directory = directory;
        this.parent = parent;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return new DirectoryTreeNode(getDirectories().get(childIndex), this);
    }

    @Override
    public int getChildCount() {
        return getDirectories().size();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return getDirectories().indexOf(((DirectoryTreeNode)node).directory);
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration(
                getDirectories().stream().map(d -> new DirectoryTreeNode(d, this)).collect(toList())
        );
    }

    public Directory getDirectory() {
        return directory;
    }

    private List<? extends Directory> getDirectories() {
        try {
            return directory.getSubDirs();
        } catch (Exception e) {
            e.printStackTrace();
            return emptyList();
        }
    }

    @Override
    public String toString() {
        return directory.toString();
    }
}
