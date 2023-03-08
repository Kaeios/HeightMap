package fr.guillaume.ui.rendering.tiles;


import fr.guillaume.math.IntVector2D;
import fr.guillaume.ui.rendering.Renderer;

import java.awt.image.BufferedImage;

public abstract class TiledRenderer implements Renderer {

    protected final IntVector2D size;
    protected final IntVector2D margin;

    protected final int tileSize;

    protected TiledRenderer(IntVector2D size, IntVector2D margin, int tileSize) {
        this.size = size;
        this.margin = margin;
        this.tileSize = tileSize;
    }

    protected TiledRenderer(IntVector2D size, int tileSize) {
        this(size, new IntVector2D(0, 0), tileSize);
    }

    protected IntVector2D toPixelPosition(IntVector2D position) {
        return new IntVector2D(getTilePositionFromXIndex(position.getX()), getTilePositionFromYIndex(position.getY()));
    }

    protected int getTilePositionFromXIndex(int pos) {
        return pos * tileSize + margin.getX();
    }

    protected int getTilePositionFromYIndex(int pos) {
        return pos * tileSize + this.margin.getY();
    }

    public BufferedImage getRawImage() {
        return new BufferedImage(
                margin.getX() + size.getX() * tileSize,
                margin.getY() + size.getY() * tileSize,
                BufferedImage.TYPE_INT_ARGB
        );
    }

}
