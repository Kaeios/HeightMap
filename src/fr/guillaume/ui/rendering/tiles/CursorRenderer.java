package fr.guillaume.ui.rendering.tiles;

import fr.guillaume.math.IntVector2D;
import fr.guillaume.ui.rendering.Placeable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class CursorRenderer extends TiledRenderer implements Placeable {

    private static Image CURSOR_IMAGE = null;

    static {
        try {
            CURSOR_IMAGE = ImageIO.read(
                    CursorRenderer.class.getResource(
                            "/assets/elements/cursor.png"
                    ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IntVector2D position;

    public CursorRenderer(IntVector2D position, int tileSize) {
        super(1, 1, 0, 0, tileSize);

        this.position = position;
    }

    @Override
    public Image render() {
        return CURSOR_IMAGE;
    }


    @Override
    public void place(Graphics graphics) {

        if(position == null) return;

        graphics.drawImage(render(),
                tileSize/2 + getTilePositionFromXIndex(position.getX()),
                tileSize/2 + getTilePositionFromYIndex(position.getY()),
                null
        );
    }

    public IntVector2D getPosition() {
        return position;
    }

    public void setPosition(IntVector2D position) {
        this.position = position;
    }

}
