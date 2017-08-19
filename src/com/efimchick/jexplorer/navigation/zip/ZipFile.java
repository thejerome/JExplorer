package com.efimchick.jexplorer.navigation.zip;


import com.efimchick.jexplorer.I18n;
import com.efimchick.jexplorer.navigation.File;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;

import static com.efimchick.jexplorer.I18n.bundle;
import static com.efimchick.jexplorer.navigation.File.Type.IMAGE;
import static com.efimchick.jexplorer.navigation.File.Type.TEXT;
import static com.efimchick.jexplorer.navigation.File.TypeMapper.getExtension;
import static com.efimchick.jexplorer.navigation.File.TypeMapper.mapExtension;
import static com.efimchick.jexplorer.navigation.ui.Navigation.format;
import static com.efimchick.jexplorer.navigation.zip.ZipDirectory.endingZipPathDivisor;
import static com.efimchick.jexplorer.navigation.zip.ZipDirectory.zipPathDivisor;
import static java.util.Collections.emptyMap;

public class ZipFile implements File {
    private final ZipEntry zipEntry;
    private final String name;
    private final String fullName;
    private final String parentFullName;
    private final Type type;

    private final java.util.zip.ZipFile zip;

    public ZipFile(java.util.zip.ZipFile zip,  ZipEntry zipEntry) {
        this.zip = zip;
        Objects.requireNonNull(zipEntry);

        this.zipEntry = zipEntry;
        fullName = zipEntry.getName();
        name = fullName.substring(fullName.lastIndexOf(zipPathDivisor) + 1);
        parentFullName = fullName.substring(0, fullName.length() - name.length());

        type = mapExtension(getExtension(name));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ZipFile file = (ZipFile) o;

        return zipEntry.equals(file.zipEntry);
    }

    @Override
    public int hashCode() {
        return zipEntry.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getParentFullName() {
        return parentFullName;
    }

    @Override
    public Map<String, String> getProperties() {
        return ImmutableMap.<String, String>builder()
                .put(bundle.getString("name"), getName())
                .put(bundle.getString("size"), String.valueOf(zipEntry.getSize()))
                .put(bundle.getString("compressedSize"), String.valueOf(zipEntry.getCompressedSize()))
                .put(bundle.getString("lastModified"), String.valueOf(zipEntry.getLastModifiedTime()))
                .build();
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public InputStream getPreviewInputStream() {
        try {
            return zip.getInputStream(zipEntry);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Type getType() {
        return type;
    }
}
