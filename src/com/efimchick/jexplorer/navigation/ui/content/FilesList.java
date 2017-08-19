package com.efimchick.jexplorer.navigation.ui.content;

import com.efimchick.jexplorer.navigation.File;
import com.efimchick.jexplorer.navigation.ui.Navigation;

import javax.swing.*;
import java.util.List;

public class FilesList extends JList<File> {


    private final Navigation navigation;

    public FilesList(Navigation navigation, List<? extends File> files) {
        super(new FileListModel(files));
        this.navigation = navigation;

        this.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        this.setVisibleRowCount(-1);
        this.setCellRenderer(new FileListCellRenderer());
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
