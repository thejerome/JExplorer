package com.efimchick.jexplorer.navigation.ui;

import javax.swing.*;
import java.awt.*;

public class JPanelScrollable extends JPanel implements Scrollable{
    public JPanelScrollable(LayoutManager layout) {
        super(layout);
    }

    public JPanelScrollable() {
        super();
    }

    public Dimension getPreferredScrollableViewportSize() {
        return super.getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 12;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 12;
    }

    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
