package fr.guillaume.ui.rendering.tiles;


import fr.guillaume.ui.rendering.Renderer;

import java.awt.image.BufferedImage;

public abstract class TiledRenderer implements Renderer {

    protected final int sizeX;
    protected final int sizeY;

    protected final int marginX;
    protected final int marginY;

    protected final int tileSize;

    protected TiledRenderer(int sizeX, int sizeY, int marginX, int marginY, int tileSize) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.marginX = marginX;
        this.marginY = marginY;
        this.tileSize = tileSize;
    }

    protected TiledRenderer(int sizeX, int sizeY, int tileSize) {
        this(sizeX, sizeY, 0, 0, tileSize);
    }

    protected int getTilePositionFromXIndex(int pos) {
        return pos * tileSize + marginX;
    }

    protected int getTilePositionFromYIndex(int pos) {
        return pos * tileSize + marginY;
    }

    public BufferedImage getRawImage() {
        return new BufferedImage(marginX + sizeX * tileSize, marginY + sizeY * tileSize, BufferedImage.TYPE_INT_ARGB);
    }

}
