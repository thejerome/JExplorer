package com.efimchick.jexplorer.navigation.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.*;

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
