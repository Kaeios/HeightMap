package fr.guillaume.ui.views;

import fr.guillaume.data.HeightMapDataHolder;
import fr.guillaume.data.MapStorage;
import fr.guillaume.ui.components.ImageComponent;
import fr.guillaume.ui.components.RenderedComponent;
import fr.guillaume.ui.rendering.tiles.ThumbnailRenderer;

import javax.swing.*;
import java.util.List;

public class LoadMapView extends JFrame {

    private final MapStorage storage;

    public LoadMapView(MapStorage storage) {
        super();

        this.storage = storage;

        setupWindow();
        renderView();
    }

    private void setupWindow() {
        setTitle("Charger une carte");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 800);
        setResizable(false);
    }

    private void renderView() {
        List<HeightMapDataHolder> savedMaps = storage.getMaps();
        for (int i = 0; i < savedMaps.size(); i++) {
            HeightMapDataHolder savedMap = savedMaps.get(i);
            RenderedComponent component = new RenderedComponent();
            component.setRenderer(new ThumbnailRenderer(storage, savedMap.getProjectName()));
            component.setBounds(30 + i * 230, 30, 200, 200);
            add(component);

            JButton button = new JButton("Charger");
            button.setBounds(30 + i*230, 230, 200, 30);

            button.addActionListener(event -> {
                MapView mapEditor = new MapView(storage.getMap(savedMap.getProjectName()), storage, this);
                setVisible(false);
                mapEditor.setVisible(true);
            });
            add(button);
        }
    }

}
