package fr.guillaume;

import fr.guillaume.data.HeightMapDataHolder;
import fr.guillaume.data.MapStorage;
import fr.guillaume.ui.views.MapView;

import javax.swing.*;
import java.io.IOException;

public class AppMain {

    public static final MapStorage STORAGE = new MapStorage();

    public static void main(String[] args) throws IOException {

        STORAGE.initStorage();
        HeightMapDataHolder map = STORAGE.getMaps().get(0);

        JFrame mainFrame = new MapView(map);

        mainFrame.setVisible(true);
    }
}
