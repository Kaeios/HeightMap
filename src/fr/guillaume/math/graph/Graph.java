package fr.guillaume.math.graph;

import java.util.HashSet;
import java.util.Set;

public class Graph {

    private final Set<Node> nodes = new HashSet<>();

    private final int sizeX;
    private final int sizeY;

    public Graph(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public int[][] getWeightMap() {
        int[][] weightMap = new int[sizeX][sizeY];

        this.nodes.forEach(node -> {
            weightMap[node.getxPos()][node.getyPos()] = node.getDistance();
        });

        return weightMap;
    }

}
