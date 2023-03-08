package fr.guillaume.math.graph.solving;

import fr.guillaume.math.graph.Graph;
import fr.guillaume.math.graph.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BacktrackSolver extends Solver {

    private final Map<Node, List<Node>> cachedPaths = new HashMap<>();

    public BacktrackSolver(Graph graph, Node origin) {
        super(graph, origin);
    }

    @Override
    public List<Node> getShortestPathTo(Node node) {
        if(cachedPaths.containsKey(node)) {
            return cachedPaths.get(node);
        } else {
            //TODO Compute solution with backtrack & cache

        }
        return null;
    }

    @Override
    public int getShortestPathCostTo(Node node) {
        if(cachedPaths.containsKey(node)) {
            return countPathCost(cachedPaths.get(node));
        } else {
            return countPathCost(getShortestPathTo(node));
        }
    }

    @Override
    public boolean isFast() {
        return false;
    }

    public int countPathCost(List<Node> path) {
        int cost = 0;
        Node last = path.get(0);

        for (int i = 1; i < path.size(); i++) {
            cost += Math.max(0, last.getHeight() - path.get(i).getHeight());
        }

        return cost;
    }

}
