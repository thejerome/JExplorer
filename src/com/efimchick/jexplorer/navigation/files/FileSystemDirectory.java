package com.efimchick.jexplorer.navigation.files;

import com.efimchick.jexplorer.navigation.Directory;
import com.efimchick.jexplorer.navigation.File;

import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class FileSystemDirectory implements Directory {

    private List<LocalDirectory> rootDirs = new ArrayList<>();


    public FileSystemDirectory() {
        FileSystems.getDefault().getRootDirectories().forEach(
                p -> rootDirs.add(new LocalDirectory(p))
        );
    }

    @Override
    public List<? extends File> getFiles() {
        return emptyList();
    }

    @Override
    public List<? extends Directory> getSubDirs() {
        return rootDirs;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getFullName() {
        return "";
    }

    @Override
    public String toString() {
        return getName();
    }
}
