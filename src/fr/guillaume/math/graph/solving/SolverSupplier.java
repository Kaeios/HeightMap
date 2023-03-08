package fr.guillaume.math.graph.solving;

import fr.guillaume.math.graph.Graph;
import fr.guillaume.math.graph.Node;

public interface SolverSupplier {

    Solver supply(Graph graph, Node origin);

}
