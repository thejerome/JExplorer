package com.efimchick.jexplorer.navigation;

import com.google.common.collect.ImmutableMap;

import java.io.InputStream;
import java.util.Map;

import static com.efimchick.jexplorer.navigation.File.Type.*;

public interface File {

    String pathDivisor = "/";

    String getName();
    Map<String,String> getProperties();

    String getFullName();

    default boolean isPreviewable(){
        return getType() == TEXT || getType() == IMAGE;
    }

    default InputStream getPreviewInputStream() {
        return null;
    }

    default Type getType(){
        return Type.OTHER;
    }

    enum Type{
        DIR, IMAGE, TEXT, OTHER
    }

    class TypeMapper{
        private static Map<String, File.Type> extensionMap = ImmutableMap.<String, File.Type>builder()
                .put(".txt", TEXT)
                .put(".log", TEXT)
                .put(".xml", TEXT)
                .put(".xsl", TEXT)
                .put(".dtd", TEXT)
                .put(".xsd", TEXT)
                .put(".java", TEXT)
                .put(".js", TEXT)
                .put(".css", TEXT)
                .put(".cpp", TEXT)
                .put(".htm", TEXT)
                .put(".properties", TEXT)
                .put(".iml", TEXT)

                .put(".jpeg", IMAGE)
                .put(".jpg", IMAGE)
                .put(".gif", IMAGE)
                .put(".png", IMAGE)
                .put(".bmp", IMAGE)
                .build();
        public static Type mapExtension(String extension){
            if (extension == null) return OTHER;
            Type type = extensionMap.get(extension.toLowerCase());
            if (type == null)
                type = OTHER;
            return type;
        }

        public static String getExtension(String fileName){
            final int extensionStart = fileName.lastIndexOf(".");
            if (extensionStart != -1) {
                return fileName.substring(extensionStart);
            } else return null;
        }
    }
}
