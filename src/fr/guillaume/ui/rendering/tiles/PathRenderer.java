package fr.guillaume.ui.rendering.tiles;

import fr.guillaume.math.IntVector2D;
import fr.guillaume.math.graph.Node;
import fr.guillaume.ui.rendering.Placeable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;


public class PathRenderer extends TiledRenderer implements Placeable {

    private final List<Node> path;


    public PathRenderer(List<Node> path, int sizeX, int sizeY, int tileSize) {
        super(sizeX, sizeY, tileSize);

        this.path = path;
    }

    @Override
    public Image render() {

        BufferedImage rendered = getRawImage();

        Graphics2D graphics = rendered.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setStroke(new BasicStroke(tileSize/4.0f));
        graphics.setColor(new Color(0xff, 0x52, 0x52));

        Iterator<Node> pathIterator = path.iterator();

        Node lastNode = pathIterator.next();

        for (int i = 0; i < this.path.size() - 1; i++) {
            Node current = pathIterator.next();

            IntVector2D currentPosition = current.getPosition();
            IntVector2D lastPosition = lastNode.getPosition();

            int centerCurrentX = getTilePositionFromXIndex(currentPosition.getX()) + tileSize/2;
            int centerCurrentY = getTilePositionFromYIndex(currentPosition.getY()) + tileSize/2;

            int centerNextX = getTilePositionFromXIndex(lastPosition.getX()) + tileSize/2;
            int centerNextY = getTilePositionFromYIndex(lastPosition.getY()) + tileSize/2;

            graphics.drawLine(centerNextX, centerNextY, centerCurrentX, centerCurrentY);

            lastNode = current;
        }

        graphics.dispose();

        return rendered;
    }

    @Override
    public void place(Graphics graphics) {
        graphics.drawImage(render(), tileSize/2, tileSize/2, null);
    }

}
