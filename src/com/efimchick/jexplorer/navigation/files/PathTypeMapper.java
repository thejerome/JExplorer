package com.efimchick.jexplorer.navigation.files;

import com.efimchick.jexplorer.navigation.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static com.efimchick.jexplorer.navigation.File.Type.*;

public class PathTypeMapper {

    public static File.Type getType(Path path){
        Objects.requireNonNull(path);
        if (!Files.isRegularFile(path)){
            return DIR;
        }

        String contentType = null;
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (contentType != null) {
            if (contentType.contains("text")) return TEXT;
            if (contentType.contains("image")) return IMAGE;
        }

        final String fileName = path.getFileName().toString();
        final String extension = File.TypeMapper.getExtension(fileName);
        final File.Type type = File.TypeMapper.mapExtension(extension);

        return type == null ? OTHER : type;
    }
}
