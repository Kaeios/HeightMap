package fr.guillaume.math.graph.solving;

import fr.guillaume.math.graph.Edge;
import fr.guillaume.math.graph.Graph;
import fr.guillaume.math.graph.Node;

import java.util.*;

public class BacktrackSolver extends Solver {

    private final Map<Node, List<Node>> cachedPaths = new HashMap<>();

    public BacktrackSolver(Graph graph, Node origin) {
        super(graph, origin);
    }

    @Override
    public List<Node> getShortestPathTo(Node node) {
        if(!cachedPaths.containsKey(node))
            performBackTrack(node);
        return cachedPaths.get(node);
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

    private void performBackTrack(Node dest) {
        Stack<Node> path = new Stack<>();
        path.push(origin);
        isValid(path, 0, Integer.MAX_VALUE, dest);
    }

    private boolean isValid(Stack<Node> path, int currentPathCost, int maxCost, Node dest) {
        if(path.isEmpty()) return false;
        if(currentPathCost >= maxCost) return false;


        Node head = path.peek();

        if(head.equals(dest)) {
            cachedPaths.put(dest, new ArrayList<>(path));
            return isValid(path, currentPathCost, currentPathCost, dest);
        }

        for (Edge edge : head.getEdges()) {
            if(path.contains(edge.getDestination())) {
                continue;
            }
            path.push(edge.getDestination());

            if(isValid(path, currentPathCost + edge.getWeight(), maxCost, dest)) {
                return true;
            }

            path.pop();
        }

        return false;
    }

}
