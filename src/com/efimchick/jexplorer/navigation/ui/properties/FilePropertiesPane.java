package com.efimchick.jexplorer.navigation.ui.properties;

import com.efimchick.jexplorer.navigation.File;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;

public class FilePropertiesPane extends JPanel {
    public FilePropertiesPane(File file) {
        Objects.requireNonNull(file);
        final Map<String, String> properties = file.getProperties();

        setLayout(new GridLayout(properties.size(), 2));

        properties.entrySet().forEach(
                e -> {
                    add(new JLabel(e.getKey()));
                    add(new JLabel(e.getValue()));
                }
        );
    }


}
