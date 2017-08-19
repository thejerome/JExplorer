package com.efimchick.jexplorer.navigation.files;


import com.efimchick.jexplorer.navigation.File;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.util.Map;
import java.util.Objects;

import static com.efimchick.jexplorer.I18n.bundle;
import static java.util.Collections.emptyMap;

public class LocalFile implements File {
    private final Path path;
    private final Type type;

    public LocalFile(Path path) {
        Objects.requireNonNull(path);
        this.path = path;
        this.type = PathTypeMapper.getType(path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalFile file = (LocalFile) o;

        return path != null ? path.equals(file.path) : file.path == null;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }

    @Override
    public String toString() {
        return path.getFileName().toString();
    }

    @Override
    public String getName() {
        return path.getFileName().toString();
    }

    @Override
    public Map<String, String> getProperties() {
        try {
            return ImmutableMap.<String, String>builder()
                    .put(bundle.getString("name"), getName())
                    .put(bundle.getString("size"), String.valueOf(Files.size(path)))
                    .put(bundle.getString("lastModified"), String.valueOf(Files.getLastModifiedTime(path)))
                    .build();
        } catch (IOException | DateTimeException e) {
            e.printStackTrace();
            return emptyMap();
        }
    }

    @Override
    public String getFullName() {
        return path.toAbsolutePath().toString();
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public InputStream getPreviewInputStream() {
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
