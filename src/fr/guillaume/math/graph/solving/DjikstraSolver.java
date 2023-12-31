package fr.guillaume.math.graph.solving;

import fr.guillaume.math.graph.Edge;
import fr.guillaume.math.graph.Graph;
import fr.guillaume.math.graph.Node;

import java.util.*;

public class DjikstraSolver extends Solver {

    public DjikstraSolver(Graph graph, Node origin) {
        super(graph, origin);
        calculateShortestPathFromSource(graph, origin);
    }

    private Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private void calculateMinimumDistance(Node evaluationNode, int edgeWeight, Node sourceNode) {
        int sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeight < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeight);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    private Graph calculateShortestPathFromSource(Graph graph, Node source) {
        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Edge edge : currentNode.getEdges()) {
                Node adjacentNode = edge.getDestination();
                int edgeWeight = edge.getWeight();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    @Override
    public List<Node> getShortestPathTo(Node other) {
        if(other.getDistance() == Integer.MAX_VALUE) return new ArrayList<>();
        List<Node> path = new LinkedList<>(other.getShortestPath());
        path.add(other);
        return path;
    }

    @Override
    public int getShortestPathCostTo(Node other) {
        return other.getDistance();
    }

    @Override
    public boolean isFast() {
        return true;
    }

}
