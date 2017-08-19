package com.efimchick.jexplorer.navigation.zip;

import java.util.HashMap;
import java.util.Map;

public class ZipHierarchy {

    Map<String, ZipDirectory> nameDirMap = new HashMap<>();
    private ZipDirectory root;

    public ZipHierarchy() {
        root = new ZipRoot();
        nameDirMap.put(root.getFullName(), root);
    }

    public void putDirectory(ZipDirectory zipDirectory) {
        nameDirMap.put(zipDirectory.getFullName(), zipDirectory);
        final ZipDirectory parent = getOrCreateParent(zipDirectory);
        parent.addSubDir(zipDirectory);
    }

    public void putFile(ZipFile zipFile) {
        final String parentName = zipFile.getParentFullName();
        final ZipDirectory parentDirectory;
        if (!nameDirMap.containsKey(parentName)){
            parentDirectory = new ZipDirectory(parentName);
            putDirectory(parentDirectory);
        } else {
            parentDirectory = nameDirMap.get(parentName);
        }
        parentDirectory.addFile(zipFile);
    }

    private ZipDirectory getOrCreateParent(ZipDirectory zipDirectory){
        final String parentName = zipDirectory.getParentFullName();
        final ZipDirectory parentDirectory;
        if (!nameDirMap.containsKey(parentName)){
            parentDirectory = new ZipDirectory(parentName);
            putDirectory(parentDirectory);
        } else {
            parentDirectory = nameDirMap.get(parentName);
        }
        return parentDirectory;
    }

    public ZipDirectory getRoot() {
        return root;
    }
}
