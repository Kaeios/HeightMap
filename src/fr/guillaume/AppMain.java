package fr.guillaume;

import fr.guillaume.data.MapStorage;
import fr.guillaume.ui.views.LoadMapView;

import java.io.IOException;

public class AppMain {

    public static void main(String[] args) throws IOException {

        MapStorage storage = new MapStorage();
        storage.initStorage();

        LoadMapView loadView = new LoadMapView(storage);
        loadView.setVisible(true);
    }
}
