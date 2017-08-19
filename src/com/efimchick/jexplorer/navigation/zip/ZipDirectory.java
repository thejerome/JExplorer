package com.efimchick.jexplorer.navigation.zip;

import com.efimchick.jexplorer.navigation.Directory;
import com.efimchick.jexplorer.navigation.File;
import com.efimchick.jexplorer.navigation.FileExtensionFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.unmodifiableList;

public class ZipDirectory implements Directory{
    public static final String zipPathDivisor = "/";
    public static final String endingZipPathDivisor = zipPathDivisor + "$";

    private final String fullName;
    private final String name;
    private final String parentFullName;

    private List<ZipDirectory> subDirectories = new ArrayList<>();
    private List<ZipFile> files = new ArrayList<>();

    public ZipDirectory(String fullName) {
        Objects.requireNonNull(fullName);

        this.fullName = fullName;
        String fullNameWithNoEndDivisor = fullName.replaceFirst(endingZipPathDivisor, "");
        name = fullNameWithNoEndDivisor.substring(fullNameWithNoEndDivisor.lastIndexOf(zipPathDivisor) + 1);
        parentFullName = fullNameWithNoEndDivisor.substring(0, fullNameWithNoEndDivisor.length() - name.length());
    }

    public boolean addSubDir(ZipDirectory subDir){
        return subDirectories.add(subDir);
    }

    public boolean addFile(ZipFile zipFile){
        return files.add(zipFile);
    }

    @Override
    public List<? extends File> getFiles(FileExtensionFilter filter) {
        return unmodifiableList(
                files.stream()
                        .filter(f -> filter.test(f.getName()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<? extends Directory> getSubDirs() {
        return unmodifiableList(subDirectories);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    public String getParentFullName() {
        return parentFullName;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String hierarchyAsString() {
        StringBuilder printOutBuilder = new StringBuilder();
        printOutBuilder.append(toString()).append(System.lineSeparator());
        subDirectories.stream().forEach(sd -> sd.fillPrintOutBuilder(printOutBuilder, 1));
        return printOutBuilder.toString();
    }

    private void fillPrintOutBuilder(StringBuilder builder, int depth){
        IntStream.range(0, depth).forEach(i -> builder.append("\t"));
        builder.append(toString()).append(System.lineSeparator());
        subDirectories.stream().forEach(sd -> sd.fillPrintOutBuilder(builder, depth + 1));
    }
}
