package fr.guillaume;

import fr.guillaume.data.MapStorage;
import fr.guillaume.math.graph.Graph;
import fr.guillaume.math.graph.GraphGenerator;
import fr.guillaume.math.graph.Node;
import fr.guillaume.math.graph.solving.BacktrackSolver;
import fr.guillaume.ui.views.LoadMapView;

import java.io.IOException;
import java.util.List;

public class AppMain {

    public static void main(String[] args) throws IOException {

        MapStorage storage = new MapStorage();
        storage.initStorage();

        LoadMapView loadView = new LoadMapView(storage);
        loadView.setVisible(true);
    }
}
