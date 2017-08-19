package com.efimchick.jexplorer.navigation;

import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.efimchick.jexplorer.I18n.bundle;

public interface Directory extends File {

    List<? extends File> getFiles() throws Exception;
    List<? extends Directory> getSubDirs() throws Exception;

    @Override
    default Type getType(){
        return Type.DIR;
    }

    @Override
    default Map<String, String> getProperties() {
        try {
            return ImmutableMap.<String, String>builder()
                    .put(bundle.getString("name"), getName())
                    .put(bundle.getString("filesInside"), String.valueOf(getFiles().size()))
                    .put(bundle.getString("subdirectoriesInside"), String.valueOf(getSubDirs().size()))
                    .build();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    };


}
