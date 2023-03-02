package fr.guillaume.ui.views;

import fr.guillaume.data.HeightMapDataHolder;
import fr.guillaume.ui.components.ImageComponent;
import fr.guillaume.ui.controllers.MapController;
import fr.guillaume.ui.rendering.OverlayRenderer;
import fr.guillaume.ui.rendering.Placeable;
import fr.guillaume.ui.rendering.tiles.CursorRenderer;
import fr.guillaume.ui.rendering.tiles.TileCanvasRenderer;
import fr.guillaume.ui.rendering.tiles.TileMapRenderer;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class MapView extends JFrame {

    private static final int TILE_SIZE = 64;

    private final HeightMapDataHolder heightMap;

    private final MapController controller;

    private JSlider heightSlider;

    private JButton editButton;
    private JButton computeButton;
    private JButton saveButton;

    private CursorRenderer cursorRenderer;
    private ImageComponent mapComponent;

    public MapView(HeightMapDataHolder heightMap) {
        super();

        this.heightMap = heightMap;
        this.controller = new MapController(this);

        setupWindow();
        renderView();
    }

    public void setupWindow() {
        setTitle("HeightMap : " + this.heightMap.getProjectName());
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setResizable(false);
    }

    public void renderView() {
        setupMapComponent();
        refreshMap();
        setupHeightSlider();
        setupComputeButton();
        setupEditButton();
        setupSaveButton();
    }

    private void setupHeightSlider() {
        heightSlider = new JSlider(JSlider.VERTICAL, 1, 20, 1);
        heightSlider.setPaintLabels(true);
        heightSlider.setPaintTicks(true);
        heightSlider.setPaintTrack(true);
        heightSlider.setEnabled(true);
        heightSlider.setBounds(720, 32, 50, 64 * 10);
        heightSlider.setMinorTickSpacing(1);
        heightSlider.setMajorTickSpacing(1);
        add(heightSlider);
    }

    private void setupComputeButton() {
        computeButton = new JButton("Calculer");
        computeButton.setBounds(32, 680, 132, 50);
        add(computeButton);
        computeButton.addActionListener(controller);
    }

    private void setupSaveButton() {
        saveButton = new JButton("Sauvegarder");
        saveButton.setBounds(132 * 2 + 32 * 3, 680, 132, 50);
        add(saveButton);
        saveButton.addActionListener(controller);
    }

    private void setupEditButton() {
        editButton = new JButton("Modifier");
        editButton.setBounds( 132 + 32 * 2, 680, 132, 50);
        editButton.setEnabled(false);
        add(editButton);
        editButton.addActionListener(controller);
    }

    public void refreshMap() {
        List<Placeable> overlays = new LinkedList<>();

        TileCanvasRenderer canvasRenderer = new TileCanvasRenderer(heightMap.getSizeX(), heightMap.getSizeY(), TILE_SIZE);
        TileMapRenderer mapRenderer = new TileMapRenderer(heightMap, TILE_SIZE);

        overlays.add(canvasRenderer);
        overlays.add(mapRenderer);
        overlays.addAll(controller.getStateOverlay());
        overlays.add(cursorRenderer);

        OverlayRenderer renderer = new OverlayRenderer(overlays, 64 * 10 + 32, 64 * 10 + 32);
        mapComponent.setRenderer(renderer);
    }

    private void setupMapComponent() {
        cursorRenderer = new CursorRenderer(3, 4, TILE_SIZE);

        mapComponent = new ImageComponent();
        mapComponent.setBounds(0, 0, 64 * 10 + 32, 64 * 10 + 32);
        add(mapComponent);

        mapComponent.addMouseListener(controller);
        mapComponent.addMouseMotionListener(controller);
    }

    public MapController getController() {
        return controller;
    }

    public JSlider getHeightSlider() {
        return heightSlider;
    }

    public void setHeightSlider(JSlider heightSlider) {
        this.heightSlider = heightSlider;
    }

    public JButton getComputeButton() {
        return computeButton;
    }

    public void setComputeButton(JButton computeButton) {
        this.computeButton = computeButton;
    }

    public CursorRenderer getCursorRenderer() {
        return cursorRenderer;
    }

    public void setCursorRenderer(CursorRenderer cursorRenderer) {
        this.cursorRenderer = cursorRenderer;
    }

    public HeightMapDataHolder getHeightMap() {
        return heightMap;
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }
}
