package fr.guillaume.ui.rendering.tiles;

import fr.guillaume.ui.rendering.Placeable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PathWeightRenderer extends TiledRenderer implements Placeable {

    private final int[][] weightMap;

    public PathWeightRenderer(int[][] weightMap, int tileSize) {
        super(weightMap.length, weightMap[0].length, tileSize);

        this.weightMap = weightMap;
    }

    @Override
    public void place(Graphics graphics) {
        graphics.drawImage(render(), tileSize/2, tileSize/2, null);
    }

    @Override
    public Image render() {
        BufferedImage rendered = getRawImage();

        Graphics2D graphics = rendered.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(new Color(0xff, 0x52, 0x52));

        for (int xPos = 0; xPos < sizeX; xPos++) {
            for (int yPos = 0; yPos < sizeY; yPos++) {
                int textX = getTilePositionFromXIndex(xPos) + tileSize/2 - 4;
                int textY = getTilePositionFromYIndex(yPos) + tileSize/2 + 4;

                int weight = weightMap[xPos][yPos];

                graphics.drawString(weight == Integer.MAX_VALUE ? "X" : String.valueOf(weight), textX, textY);
            }
        }

        graphics.dispose();

        return rendered;
    }

}
