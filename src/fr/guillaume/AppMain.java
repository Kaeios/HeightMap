package fr.guillaume;

import fr.guillaume.data.MapStorage;
import fr.guillaume.ui.views.LoadMapView;

public class AppMain {

    public static void main(String[] args) {

        MapStorage storage = new MapStorage();
        storage.initStorage();
        storage.loadImportFolder();

        LoadMapView loadView = new LoadMapView(storage);
        loadView.setVisible(true);
    }
}
