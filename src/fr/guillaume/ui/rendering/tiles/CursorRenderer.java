package fr.guillaume.ui.rendering.tiles;

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

    private int cursorX = -1;
    private int cursorY = -1;

    public CursorRenderer(int cursorX, int cursorY, int tileSize) {
        super(1, 1, 0, 0, tileSize);

        this.cursorX = cursorX;
        this.cursorY = cursorY;
    }

    @Override
    public Image render() {
        return CURSOR_IMAGE;
    }


    @Override
    public void place(Graphics graphics) {

        if(cursorY == -1 || cursorX == -1) return;

        graphics.drawImage(render(),
                tileSize/2 + getTilePositionFromXIndex(cursorX),
                tileSize/2 + getTilePositionFromYIndex(cursorY),
                null
        );
    }

    public int getCursorX() {
        return cursorX;
    }

    public void setCursorX(int cursorX) {
        this.cursorX = cursorX;
    }

    public int getCursorY() {
        return cursorY;
    }

    public void setCursorY(int cursorY) {
        this.cursorY = cursorY;
    }

}
