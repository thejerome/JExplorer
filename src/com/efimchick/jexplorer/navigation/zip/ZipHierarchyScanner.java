package com.efimchick.jexplorer.navigation.zip;

import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;

import static com.efimchick.jexplorer.navigation.zip.ZipDirectory.zipPathDivisor;

public class ZipHierarchyScanner {
    public static ZipDirectory scanZipFile(String filePath) throws IOException {
        java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(filePath);

        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        ZipHierarchy hierarchy = new ZipHierarchy();

        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();

            if (zipEntry.isDirectory()) {
                ZipDirectory zipDirectory = new ZipDirectory(zipEntry.getName());
                hierarchy.putDirectory(zipDirectory);
            } else {
                ZipFile file = new ZipFile(zipFile, zipEntry);
                hierarchy.putFile(file);
            }
        }
        return hierarchy.getRoot();
    }

    private static ZipDirectory producePhantomHierarchyIfNeeded(ZipEntry zipEntry) {
        if (fullNameContainsIntermediateDivisors(zipEntry.getName())) {
            String[] phantomHierarchySequence = zipEntry.getName().split(zipPathDivisor);

            ZipDirectory[] phantomParents = Arrays.stream(phantomHierarchySequence)
                    .limit(phantomHierarchySequence.length - 1)
                    .map(name -> new ZipDirectory(name))
                    .toArray(i -> new ZipDirectory[i]);

            for (int i = 1; i < phantomParents.length; i++) {
                phantomParents[i - 1].addSubDir(phantomParents[i]);
            }

            return phantomParents[0];
        }
        return null;
    }

    private static boolean fullNameContainsIntermediateDivisors(String fullName) {
        final int firstIndexOfDivisor = fullName.indexOf(zipPathDivisor);
        return firstIndexOfDivisor != fullName.length() - 1 && firstIndexOfDivisor != -1;
    }

    private static boolean isSubDir(ZipDirectory directory, ZipDirectory prevDirectory) {
        return directory != null && prevDirectory != null && directory.getFullName().startsWith(prevDirectory.getFullName());
    }

    private static boolean isFileOfDirectory(ZipFile file, ZipDirectory directory) {
        return file != null && directory != null && file.getFullName().startsWith(directory.getFullName());
    }
}
