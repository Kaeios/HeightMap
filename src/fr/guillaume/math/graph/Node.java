package fr.guillaume.math.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Node {

    private final int xPos;
    private final int yPos;

    private final int height;

    private List<Node> shortestPath = new LinkedList<>();
    private final List<Edge> edges = new ArrayList<>();

    private int distance = Integer.MAX_VALUE;

    public Node(int xPos, int yPos, int height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void connectTo(Node other) {
        int edgeWeight = Math.max(0, other.height - this.height);

        this.edges.add(new Edge(this, other, edgeWeight));
    }

    public boolean canConnectTo(Node other) {
        int delatHeight = Math.abs(other.height - this.height);

        return delatHeight <= 3;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }
}
