package fr.guillaume.ui.rendering;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class OverlayRenderer implements Renderer {

    private final List<Placeable> overlays;

    private final int sizeX;
    private final int sizeY;

    public OverlayRenderer(List<Placeable> overlays, int sizeX, int sizeY) {
        this.overlays = overlays;

        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    @Override
    public Image render() {
        BufferedImage rendered = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = rendered.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f);
        graphics.setComposite(comp);

        overlays.forEach(overlay -> overlay.place(graphics));

        return rendered;
    }

}
