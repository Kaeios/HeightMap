package fr.guillaume.ui.rendering.tiles;

import fr.guillaume.ui.rendering.Placeable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TileCanvasRenderer extends TiledRenderer implements Placeable {

    public TileCanvasRenderer(int sizeX, int sizeY, int tileSize) {
        super(sizeX, sizeY, tileSize/2, tileSize/2, tileSize);
    }

    @Override
    public Image render() {
        BufferedImage rendered = getRawImage();

        Graphics2D graphics = rendered.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(Color.BLACK);

        for (int yPos = 0; yPos < this.sizeY; yPos++) {
            graphics.drawString(
                    String.valueOf((yPos + 1)),
                    tileSize/4 - 4,
                    getTilePositionFromYIndex(yPos) + tileSize/2 + 4);
        }

        char letter = 'A';
        for (int xPos = 0; xPos < this.sizeX; xPos++) {
            graphics.drawString(
                    String.valueOf(letter),
                    getTilePositionFromXIndex(xPos) + tileSize/2 - 4,
                    tileSize/4 + 4);
            letter++;
        }

        graphics.dispose();

        return rendered;
    }

    @Override
    public void place(Graphics graphics) {
        graphics.drawImage(render(), 0, 0, null);
    }

}
