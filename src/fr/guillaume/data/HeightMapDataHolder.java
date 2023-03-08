package fr.guillaume.data;

import fr.guillaume.math.IntVector2D;
import fr.guillaume.math.graph.Graph;
import fr.guillaume.math.graph.GraphGenerator;

import java.awt.*;
import java.util.Arrays;

public class HeightMapDataHolder implements Cloneable {

    private static final int[][] DEFAULT_HEIGHT_MAP = new int[10][10];

    static {
        for (int[] ints : DEFAULT_HEIGHT_MAP) {
            Arrays.fill(ints, 1);
        }
    }

    private final String projectName;
    private Image thumbnail;

    private final IntVector2D size;
    private final int[][] map;

    public HeightMapDataHolder(String projectName, Image thumbnail, int[][] map) {
        this.projectName = projectName;
        this.thumbnail = thumbnail;
        this.map = map;
        this.size = new IntVector2D(map.length, map[0].length);
    }

    public HeightMapDataHolder(String projectName, Image thumbnail) {
       this(projectName, thumbnail, DEFAULT_HEIGHT_MAP);
    }

    public int[][] getMap() {
        return map;
    }

    public Graph getGraph() {
        GraphGenerator generator = new GraphGenerator(map);
        return generator.getNodes();
    }

    public String getProjectName() {
        return projectName;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    public HeightMapDataHolder clone() {
        return new HeightMapDataHolder(projectName, thumbnail, cloneArray(map));
    }

    public int getHeightAt(IntVector2D position) {
        return map[position.getX()][position.getY()];
    }

    public void setHeightAt(IntVector2D position, int height) {
        map[position.getX()][position.getY()] = height;
    }

    private static int[][] cloneArray(int[][] arr) {
        int [][] clone = new int[arr.length][];
        for(int i = 0; i < arr.length; i++)
            clone[i] = arr[i].clone();
        return clone;
    }

    public IntVector2D getSize() {
        return size;
    }

}
