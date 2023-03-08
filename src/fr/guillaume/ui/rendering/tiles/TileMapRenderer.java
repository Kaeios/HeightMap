package fr.guillaume.ui.rendering.tiles;

import fr.guillaume.data.HeightMapDataHolder;
import fr.guillaume.math.IntVector2D;
import fr.guillaume.ui.rendering.Placeable;
import fr.guillaume.ui.texturing.TileType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TileMapRenderer extends TiledRenderer implements Placeable {

    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 20;

    private static final BufferedImage[] TILES = new BufferedImage[16];
    private static final BufferedImage[] CORNER_TILES = new BufferedImage[16];

    static {
        for (int tileCode = 0; tileCode < TILES.length; tileCode++) {
            try {
                TILES[tileCode] = ImageIO.read(
                        TileMapRenderer.class.getResource(
                                "/assets/tiles/"+ tileCode +".png"
                        )
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int tileCode = 0; tileCode < CORNER_TILES.length; tileCode++) {
            try {
                CORNER_TILES[tileCode] = ImageIO.read(
                        TileMapRenderer.class.getResource(
                                "/assets/tiles/corners/"+ tileCode +".png"
                        )
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final HeightMapDataHolder mapData;

    public TileMapRenderer(HeightMapDataHolder mapData, int tileSize) {
        super(mapData.getSize(), tileSize);

        this.mapData = mapData;
    }

    public Image render() {

        BufferedImage rendered = getRawImage();
        Graphics2D graphics = rendered.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        for(int level = MIN_LEVEL; level <= MAX_LEVEL; level++) {

            Color color = getHeightMapColor((float) (level - MIN_LEVEL) / (MAX_LEVEL - MIN_LEVEL));

            for (int yPos = 0; yPos < mapData.getSize().getX(); yPos++) {
                for (int xPos = 0; xPos < mapData.getSize().getY(); xPos++) {
                    IntVector2D position = new IntVector2D(xPos, yPos);
                    int tileCode = 0;

                    int height = mapData.getHeightAt(position);
                    if(height < level) continue;

                    height = Math.min(level, height);

                    if(xPos - 1 < 0 || mapData.getMap()[xPos-1][yPos] < height)
                        tileCode |= TileType.WEST;

                    if(yPos - 1 < 0 || mapData.getMap()[xPos][yPos - 1] < height)
                        tileCode |= TileType.NORTH;

                    if(xPos + 1 >= mapData.getSize().getY() || mapData.getMap()[xPos + 1][yPos] < height)
                        tileCode |= TileType.EAST;

                    if(yPos + 1 >= mapData.getSize().getX() || mapData.getMap()[xPos][yPos + 1] < height)
                        tileCode |= TileType.SOUTH;

                    BufferedImage image = TILES[tileCode];

                    if(tileCode == 0) {

                        if(mapData.getMap()[xPos - 1][yPos - 1] < level)
                            tileCode |= TileType.CORNER_NORTH_WEST;

                        if(mapData.getMap()[xPos + 1][yPos - 1] < level)
                            tileCode |= TileType.CORNER_NORTH_EAST;

                        if(mapData.getMap()[xPos - 1][yPos + 1] < level)
                            tileCode |= TileType.CORNER_SOUTH_EAST;

                        if(mapData.getMap()[xPos + 1][yPos + 1] < level)
                            tileCode |= TileType.CORNER_SOUTH_WEST;

                        image = CORNER_TILES[tileCode];

                    }

                    int rule = AlphaComposite.SRC_OVER;
                    Composite comp = AlphaComposite.getInstance(rule , 1.0f );
                    graphics.setComposite(comp);

                    IntVector2D imagePosition = toPixelPosition(position);

                    graphics.drawImage(
                            colorImage(image, color),
                            imagePosition.getX(),
                            imagePosition.getY(),
                            null
                    );
                }
            }
        }

        graphics.dispose();

        return rendered;
    }

    public Color getHeightMapColor(float percent) {
        if(percent < 0.5f) {
            percent *= 2.0f;
            return gradientBetween(new Color(0x13, 0x5e, 0x01), new Color(0xf7, 0xf2, 0x8a), percent);
        } else {
            percent = (percent - 0.5f) * 2.0f;
            return gradientBetween(new Color(0xf7, 0xf2, 0x8a), new Color(0x31, 0x20, 0x10), percent);
        }
    }

    public static Color gradientBetween(Color start, Color end, float percent) {
        int red = (int) (start.getRed() + percent * (end.getRed() - start.getRed()));
        int green = (int) (start.getGreen() + percent * (end.getGreen() - start.getGreen()));
        int blue = (int) (start.getBlue() + percent * (end.getBlue() - start.getBlue()));

        return new Color(red, green, blue);
    }

    public static BufferedImage colorImage(BufferedImage image, Color color) {
        // Creating a new translucent image with the same size as the given image, and creating its graphics
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = img.createGraphics();

        // Getting the given color with 0 alpha (its needed)
        Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 150);
        graphics.drawImage(image, 0, 0, null);
        graphics.setComposite(AlphaComposite.SrcAtop);
        graphics.setColor(newColor);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.dispose();

        // Returning the created image
        return img;
    }

    @Override
    public void place(Graphics graphics) {
        graphics.drawImage(render(), tileSize/2, tileSize/2, null);
    }
}
