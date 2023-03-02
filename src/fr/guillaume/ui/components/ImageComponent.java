package fr.guillaume.ui.components;

import fr.guillaume.ui.rendering.OverlayRenderer;

import javax.swing.*;
import java.awt.*;

public class ImageComponent extends JPanel {

    private OverlayRenderer renderer;

    public ImageComponent() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawFullsizedImage(g, this, renderer.render());
    }

    public static void drawFullsizedImage(Graphics g, JComponent component, Image image) {
        g.drawImage(image, 0, 0, component.getWidth(), component.getHeight(), component);
    }

    public void setRenderer(OverlayRenderer renderer) {
        this.renderer = renderer;
    }

}
