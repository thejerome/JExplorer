package com.efimchick.jexplorer.navigation;

import java.util.function.Predicate;

public class FileExtensionFilter implements Predicate<String> {

    private final String extension;

    public FileExtensionFilter(String extension) {
        this.extension = "." + extension;
    }

    @Override
    public boolean test(String fileName) {
        return fileName == null ? true : fileName.toLowerCase().endsWith(extension);
    }

    public static final Empty empty = new Empty();

    public static class Empty extends FileExtensionFilter{

        public Empty() {
            super("");
        }

        @Override
        public boolean test(String fileName) {
            return true;
        }
    }
}
