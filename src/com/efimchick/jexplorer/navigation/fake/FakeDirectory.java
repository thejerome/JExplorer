package com.efimchick.jexplorer.navigation.fake;

import com.efimchick.jexplorer.navigation.Directory;
import com.efimchick.jexplorer.navigation.File;
import com.efimchick.jexplorer.navigation.FileExtensionFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class FakeDirectory implements Directory {
    private final String name;
    private final Directory parent;
    private final List<Directory> subDirs = new ArrayList<>();
    private final List<File> files = new ArrayList<>();

    public FakeDirectory(String name){
        this(name, null);
    }

    private FakeDirectory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    public FakeDirectory createSubDir(String name){
        FakeDirectory fakeDirectory = new FakeDirectory(name, this);
        subDirs.add(fakeDirectory);
        return fakeDirectory;
    }
    public FakeFile createFile(String name, Map<String, String> properties){
        FakeFile fakeFile = new FakeFile(name, this, properties);
        files.add(fakeFile);
        return fakeFile;
    }

    @Override
    public List<? extends Directory> getSubDirs() {
        return subDirs;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, String> getProperties() {
        return getFiles(FileExtensionFilter.empty).stream().map(f -> f.getProperties())
                .flatMap(p -> p.entrySet().stream())
                .collect(toMap(
                        e -> e.getKey(),
                        e -> e.getValue(),
                        (v1, v2) -> String.join(", ", v1, v2)
                ));
    }

    @Override
    public List<? extends File> getFiles(FileExtensionFilter filter) {
        return Collections.unmodifiableList(
                files.stream()
                        .filter(f -> filter.test(f.getName()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public String toString() {
        return name;
    }

    public String getFullName(){
        return parent != null ?
                parent.getFullName() + pathDivisor + getName()
                : getName();
    }
}
