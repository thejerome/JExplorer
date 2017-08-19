package com.efimchick.jexplorer.navigation.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePane extends JPanelScrollable {

    private BufferedImage image;

    public ImagePane(BufferedImage image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
          g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }
}
