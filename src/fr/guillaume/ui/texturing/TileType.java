package fr.guillaume.ui.texturing;

public class TileType {

    public static final int NORTH = 0b1000;
    public static final int EAST = 0b0100;
    public static final int SOUTH = 0b0010;
    public static final int WEST = 0b0001;

    public static final int CORNER_NORTH_WEST = 0b1000;
    public static final int CORNER_NORTH_EAST = 0b0100;
    public static final int CORNER_SOUTH_EAST = 0b0010;
    public static final int CORNER_SOUTH_WEST = 0b0001;

    public static int getTileId(int... types) {
        int result = 0;

        for (int type : types) {
            result |= type;
        }

        return result;
    }

}
