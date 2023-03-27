package fr.guillaume.ui.views;

import fr.guillaume.data.HeightMapDataHolder;
import fr.guillaume.data.MapStorage;
import fr.guillaume.ui.components.RenderedComponent;
import fr.guillaume.ui.rendering.tiles.ThumbnailRenderer;

import javax.swing.*;
import java.io.IOException;
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

        JTextField nameField = new JTextField("Carte-" + ((int)(Math.random() * 100)));
        nameField.setBounds(30, 700, 230, 30);
        add(nameField);

        JButton createButton = new JButton("Créer");
        createButton.setBounds(260, 700, 150, 30);
        createButton.addActionListener(event -> {
            HeightMapDataHolder map = new HeightMapDataHolder(nameField.getText(), null);
            try {
                storage.saveMap(map);
                setVisible(false);
                MapView mapEditor = new MapView(map, storage, this);
                mapEditor.setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        add(createButton);

        List<HeightMapDataHolder> savedMaps = storage.getMaps();
        for (int i = 0; i < savedMaps.size(); i++) {
            HeightMapDataHolder savedMap = savedMaps.get(i);
            RenderedComponent component = new RenderedComponent();
            component.setRenderer(new ThumbnailRenderer(storage, savedMap.getProjectName()));
            component.setBounds(30 + i%4 * 230, 30 + (i/4) * 300, 200, 200);
            add(component);

            JButton loadButton = new JButton("Charger");
            loadButton.setBounds(30 + (i%4)*230, 230 + (i/4) * 300, 200, 30);

            loadButton.addActionListener(event -> {
                MapView mapEditor = new MapView(storage.getMap(savedMap.getProjectName()), storage, this);
                setVisible(false);
                mapEditor.setVisible(true);
            });
            add(loadButton);

            JButton deleteButton = new JButton("Supprimer");
            deleteButton.setBounds(30 + (i%4)*230, 260 + (i/4) * 300, 200, 30);
            deleteButton.addActionListener(event -> {
                removeAll();
                setVisible(false);
                storage.delete(savedMap);
                LoadMapView newView = new LoadMapView(storage);
                newView.setVisible(true);
            });

            add(deleteButton);
        }
    }

}
