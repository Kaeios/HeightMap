package fr.guillaume.math.graph;

import fr.guillaume.math.IntVector2D;

import java.util.Arrays;
import java.util.List;

public class GraphGenerator {

    private static final List<IntVector2D> POSSIBLE_MOVES = Arrays.asList(
            new IntVector2D(0, 1),
            new IntVector2D(1, 0),
            new IntVector2D(0, -1),
            new IntVector2D(-1, 0)
    );

    private final int sizeX;
    private final int sizeY;

    private final Node[][] nodes;

    public GraphGenerator(int[][] heightMap) {

        this.sizeX = heightMap.length;
        this.sizeY = heightMap[0].length;

        this.nodes = new Node[sizeX][sizeY];
        for (int xPos = 0; xPos < sizeX; xPos++) {
            for (int yPos = 0; yPos < sizeY; yPos++) {
                nodes[xPos][yPos] = new Node(xPos, yPos, heightMap[xPos][yPos]);
            }
        }

        createConnections();
    }

    public void createConnections() {
        for (int xPos = 0; xPos < sizeX; xPos++) {
            for (int yPos = 0; yPos < sizeY; yPos++) {
                Node node = nodes[xPos][yPos];

                IntVector2D currentPos = new IntVector2D(xPos, yPos);

                for (IntVector2D move : POSSIBLE_MOVES) {
                    currentPos.add(move);

                    if(isInGrid(currentPos) && node.canConnectTo(nodes[currentPos.getX()][currentPos.getY()])) {
                        node.connectTo(nodes[currentPos.getX()][currentPos.getY()]);
                    }

                    currentPos.subtract(move);
                }
            }
        }
    }

    public boolean isInGrid(IntVector2D position) {
        return position.getX() >= 0 && position.getX() < this.sizeX && position.getY() >= 0 && position.getY() < this.sizeY;
    }

    public Graph getNodes() {
        Graph graph = new Graph(sizeX, sizeY);

        for (int xPos = 0; xPos < this.nodes.length; xPos++) {
            for (int yPos = 0; yPos < this.nodes[xPos].length; yPos++) {
                graph.getNodes().add(this.nodes[xPos][yPos]);
            }
        }

        return graph;
    }

}
