package com.efimchick.jexplorer.navigation.ui.content;

import com.efimchick.jexplorer.navigation.Directory;
import com.efimchick.jexplorer.navigation.File;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileListCellRenderer extends DefaultListCellRenderer {


    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        FileSystemView fileSystemView = FileSystemView.getFileSystemView();

        File file = (File)value;
        Path path = Paths.get(file.getFullName());

        Icon icon;
        icon = fileSystemView.getSystemIcon(path.toFile());
        icon = icon != null ? icon : file instanceof Directory ? UIManager.getIcon("FileView.directoryIcon") : UIManager.getIcon("FileView.fileIcon");
        label.setIcon(icon);

        label.setIconTextGap(5);

        label.setVerticalAlignment(CENTER);
        label.setHorizontalAlignment(LEFT);

        label.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        return label;
    }
}
