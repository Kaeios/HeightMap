package fr.guillaume.math.graph.solving;

import fr.guillaume.math.graph.Graph;
import fr.guillaume.math.graph.Node;

public enum SolverType {

    DJIKSTRA(DjikstraSolver::new),
    BACKTRACK(BacktrackSolver::new);

    private final SolverSupplier supplier;

    SolverType(SolverSupplier supplier) {
        this.supplier = supplier;
    }

    public Solver get(Graph graph, Node origin) {
        return supplier.supply(graph, origin);
    }

}
