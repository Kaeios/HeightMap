package fr.guillaume.math.graph.solving;

import fr.guillaume.math.graph.Graph;
import fr.guillaume.math.graph.Node;

import java.util.*;

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
        Stack<Node> path = new Stack<>();
        path.push(origin);
        maxCost = Integer.MAX_VALUE;
        int currentCost = 0;

        while(!path.isEmpty()) {
            System.out.println(path.size());
            Node head = path.peek();

            if(head == dest) {
                this.cachedPaths.put(dest, new ArrayList<>(path));
                maxCost = currentCost;
            }

            if(head.hasNext() && currentCost < maxCost) {
                Node next = head.next();

                if(path.contains(next)) continue;
                if(path.size() >= 2 && next.equals(path.get(path.size() - 2)))
                    continue;

                path.push(next);
                currentCost += Math.max(0, head.getHeight() - next.getHeight());
                System.out.println(currentCost);
            } else {
                head.setNextTryIndex(0);
                path.pop();
                if(path.isEmpty()) continue;
                currentCost -= Math.max(0, path.peek().getHeight() - head.getHeight());
            }
        }

        if(cachedPaths.get(dest) != null) {
            cachedPaths.get(dest).add(0, origin);
        }

    }

}
