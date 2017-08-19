package com.efimchick.jexplorer.navigation.ui.structure;

import com.efimchick.jexplorer.navigation.Directory;
import com.efimchick.jexplorer.navigation.File;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StructureTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        FileSystemView fileSystemView = FileSystemView.getFileSystemView();

        File file = ((DirectoryTreeNode)value).getDirectory();
        Path path = Paths.get(file.getFullName());

        Icon icon;
        icon = fileSystemView.getSystemIcon(path.toFile());
        icon = icon != null ? icon : file instanceof Directory ? UIManager.getIcon("FileView.directoryIcon") : UIManager.getIcon("FileView.fileIcon");
        label.setIcon(icon);

        label.setIconTextGap(5);

        label.setVerticalAlignment(CENTER);
        label.setHorizontalAlignment(LEFT);

        label.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));

        return label;
    }
}