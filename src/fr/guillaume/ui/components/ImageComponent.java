package fr.guillaume.ui.components;

import javax.swing.*;
import java.awt.*;

public class ImageComponent extends JPanel {

    private Image image;

    public ImageComponent(Image image) {
        super();
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawFullsizedImage(g, this, image);
    }

    public static void drawFullsizedImage(Graphics g, JComponent component, Image image) {
        g.drawImage(image, 0, 0, component.getWidth(), component.getHeight(), component);
    }

}
