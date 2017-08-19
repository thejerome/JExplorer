package com.efimchick.jexplorer.navigation.fake;

import com.efimchick.jexplorer.navigation.Directory;
import com.efimchick.jexplorer.navigation.File;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

import static java.util.Collections.emptyMap;

public class FakeFile implements File {
    private final String name;
    private final Directory parent;
    private final Map<String, String> properties;

    public FakeFile(String name, Directory parent, Map<String, String> properties) {
        this.name = name;
        this.parent = parent;
        this.properties = properties == null ? emptyMap() : ImmutableMap.copyOf(properties);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getFullName(){
        return parent != null ?
                parent.getFullName() + pathDivisor + getName()
                : getName();
    }
}
