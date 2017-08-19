package com.efimchick.jexplorer.navigation.files;

import com.efimchick.jexplorer.navigation.Directory;
import com.efimchick.jexplorer.navigation.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class LocalDirectory implements Directory {
    private final Path path;

    public LocalDirectory(Path path) {
        this.path = path;
    }

    public List<LocalDirectory> scanForSubDirectories() {
        try {
            return Files.list(path)
                    .filter(Files::isDirectory)
                    .filter(f -> !f.getFileName().toString().equals("."))
                    .filter(f -> !f.getFileName().toString().equals(".."))
                    .map(LocalDirectory::new)
                    .collect(toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<LocalFile> scanForFiles() {
        try {
            return Files.list(path)
                    .filter(Files::isRegularFile)
                    .filter(f -> !f.getFileName().toString().equals("."))
                    .filter(f -> !f.getFileName().toString().equals(".."))
                    .map(LocalFile::new)
                    .collect(toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        Path fileName = path.getFileName();
        return fileName == null ? path.toString() : fileName.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalDirectory localDirectory = (LocalDirectory) o;

        return path != null ? path.equals(localDirectory.path) : localDirectory.path == null;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }

    @Override
    public List<? extends Directory> getSubDirs() {
        return scanForSubDirectories();
    }

    @Override
    public String getName() {
        return path.getFileName().toString();
    }

    @Override
    public String getFullName() {
        return path.toAbsolutePath().toString();
    }

    @Override
    public List<? extends File> getFiles() {
        return scanForFiles();
    }
}
