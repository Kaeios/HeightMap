package fr.guillaume.math.graph;

import java.util.List;

public abstract class Solver {

    protected final Graph graph;
    protected final Node origin;

    public Solver(Graph graph, Node origin) {
        this.graph = graph;
        this.origin = origin;
    }

    public abstract List<Node> getShortestPathTo(Node node);
    public abstract int getShortestPathCostTo(Node node);

}
