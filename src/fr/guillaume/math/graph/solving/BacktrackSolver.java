package fr.guillaume.math.graph.solving;

import fr.guillaume.math.graph.Edge;
import fr.guillaume.math.graph.Graph;
import fr.guillaume.math.graph.Node;

import java.util.*;
import java.util.stream.Collectors;

public class BacktrackSolver extends Solver {

    private final Map<Node, List<Node>> cachedPaths = new HashMap<>();

    private int maxCost = Integer.MAX_VALUE;

    public BacktrackSolver(Graph graph, Node origin) {
        super(graph, origin);
    }

    @Override
    public List<Node> getShortestPathTo(Node node) {
        if(!cachedPaths.containsKey(node))
            computeBackTrack(node);
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

    public void computeBackTrack(Node dest) {
        Stack<Edge> path = new Stack<>();
        path.push(origin.next());
        maxCost = Integer.MAX_VALUE;
        int currentCost = 0;

        while(!path.isEmpty()) {
            Edge head = path.peek();

            if(head.getDestination() == dest) {
                this.cachedPaths.put(dest, path.stream().map(Edge::getDestination).collect(Collectors.toList()));
                maxCost = currentCost;
            }

            if(head.getDestination().hasNext() && currentCost < maxCost) {
                Edge next = head.getDestination().next();

                if(path.size() >= 2 && next.getDestination().equals(path.get(path.size() - 2).getDestination()))
                    continue;

                path.push(next);
                currentCost += next.getWeight();
            } else {
                head.getDestination().setNextTryIndex(0);
                path.pop();
                currentCost -= head.getWeight();
            }
        }

        if(cachedPaths.get(dest) != null) {
            cachedPaths.get(dest).add(0, origin);
        }

    }

}
