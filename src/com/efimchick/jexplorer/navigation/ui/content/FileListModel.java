package com.efimchick.jexplorer.navigation.ui.content;

import com.efimchick.jexplorer.navigation.File;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileListModel implements ListModel<File> {

    private List<? extends File> files;

    private Set<ListDataListener> listeners = new HashSet<>();

    public FileListModel(List<? extends File> files) {
        this.files = files;
    }

    @Override
    public int getSize() {
        return files.size();
    }

    @Override
    public File getElementAt(int index) {
        return files.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
}
