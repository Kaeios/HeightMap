package fr.guillaume.ui.controllers.map;

import fr.guillaume.data.MapStorage;
import fr.guillaume.math.IntVector2D;
import fr.guillaume.math.graph.Graph;
import fr.guillaume.math.graph.Node;
import fr.guillaume.math.graph.solving.Solver;
import fr.guillaume.math.graph.solving.SolverType;
import fr.guillaume.ui.components.buttons.ButtonHandler;
import fr.guillaume.ui.controllers.FullMouseController;
import fr.guillaume.ui.rendering.Placeable;
import fr.guillaume.ui.rendering.tiles.CursorRenderer;
import fr.guillaume.ui.rendering.tiles.PathRenderer;
import fr.guillaume.ui.rendering.tiles.PathWeightRenderer;
import fr.guillaume.ui.views.LoadMapView;
import fr.guillaume.ui.views.MapView;

import java.awt.event.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MapController extends FullMouseController {

    private final int tileSize;

    private final MapStorage storage;
    private final SolverType solverType = SolverType.BACKTRACK;

    private final MapView view;
    private MapViewState state = MapViewState.EDIT;

    private IntVector2D startPosition;
    private IntVector2D endPosition;

    private Graph solution;
    private Solver solver;
    private List<Node> path = new LinkedList<>();

    public MapController(MapStorage storage, MapView view, int tileSize) {
        this.storage = storage;
        this.view = view;
        this.tileSize = tileSize;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        int xPos = event.getX();
        int yPos = event.getY();

        // Si le curseur n'est pas sur la carte on ne fait rien
        if(xPos <= 32 || xPos >= 32 + tileSize*this.view.getHeightMap().getSize().getX()
                || yPos <= 32 || yPos >= 32 + tileSize *this.view.getHeightMap().getSize().getY()) return;

        // On récupère la position de la case sur laquelle se trouve la souris
        IntVector2D cursorPosition = new IntVector2D((xPos - 32) / tileSize, (yPos - 32) / tileSize);

        if(this.state.equals(MapViewState.EDIT)) {
            view.getHeightMap().setHeightAt(cursorPosition, view.getHeightSlider().getValue());
            view.repaint();
        } else if(this.state.equals(MapViewState.SELECT_START)){
            this.startPosition = cursorPosition;

            this.solution = this.view.getHeightMap().getGraph();
            this.solver = solverType.get(
                    this.solution,
                    this.solution.getNodes()
                            .stream()
                            .filter(node -> node.getPosition().equals(this.startPosition)).findAny().get()
            );

            setState(MapViewState.SELECT_END);

            view.refreshMap();
            view.repaint();
        } else if(this.state.equals(MapViewState.SELECT_END)) {
            this.endPosition = cursorPosition;

            this.path = solver.getShortestPathTo(this.solution.getNodes()
                    .stream()
                    .filter(node -> node.getPosition().equals(this.endPosition))
                    .findAny()
                    .get()
            );

            setState(MapViewState.SHOW_PATH);
            this.view.getComputeButton().setEnabled(true);

            view.refreshMap();
            view.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        handleMouse(e, this.getState().equals(MapViewState.EDIT));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        handleMouse(e, false);
    }

    private void handleMouse(MouseEvent event, boolean placeTile) {
        int xPos = event.getX();
        int yPos = event.getY();

        IntVector2D newCursor = null;

        boolean shouldPlace = false;

        if(xPos > 32 && xPos < 32 + tileSize * this.view.getHeightMap().getSize().getX()
                && yPos > 32 && yPos < 32 + tileSize * this.view.getHeightMap().getSize().getY()
        )
        {
            newCursor = new IntVector2D((xPos - 32) / tileSize, (yPos - 32) / tileSize);
            shouldPlace = placeTile;
        }

        CursorRenderer cursor = view.getCursorRenderer();

        if(cursor.getPosition() == null && newCursor == null) return;
        if(cursor.getPosition() != null && cursor.getPosition().equals(newCursor)) return;

        cursor.setPosition(newCursor);

        if(shouldPlace)
            view.getHeightMap().setHeightAt(newCursor, view.getHeightSlider().getValue());

        view.repaint();
    }

    @ButtonHandler(targetId = "compute")
    public void onPressComputeButton() {
        setState(MapViewState.SELECT_START);
        this.view.getEditButton().setEnabled(true);
        this.view.getHeightSlider().setEnabled(false);
        view.getComputeButton().setEnabled(false);

        this.view.refreshMap();
        this.view.repaint();
    }

    @ButtonHandler(targetId = "edit")
    public void onPressEditButton() {
        setState(MapViewState.EDIT);
        this.view.getComputeButton().setEnabled(true);
        this.view.getHeightSlider().setEnabled(true);
        this.view.getEditButton().setEnabled(false);

        this.view.refreshMap();
        this.view.repaint();
    }

    @ButtonHandler(targetId = "save")
    public void onPressSaveButton() {
        try {
            this.storage.saveMap(this.view.getHeightMap());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @ButtonHandler(targetId = "load")
    public void onPressLoadButton() {
        this.view.setVisible(false);
        this.view.removeAll();
        this.view.setEnabled(false);

        LoadMapView loadView = new LoadMapView(this.storage);
        loadView.setVisible(true);
    }

    public MapViewState getState() {
        return state;
    }

    public List<Placeable> getStateOverlay() {
        List<Placeable> overlays = new LinkedList<>();
        if(this.getState().equals(MapViewState.SELECT_END) && solver != null && solver.isFast()) {
            PathWeightRenderer weightRenderer = new PathWeightRenderer(solution.getWeightMap(), 64);
            overlays.add(weightRenderer);
        } else if(this.getState().equals(MapViewState.SHOW_PATH)) {
            if(this.path.isEmpty()) return overlays;
            PathRenderer pathRenderer = new PathRenderer(this.path, this.view.getHeightMap().getSize(), 64);
            overlays.add(pathRenderer);
        }

        return overlays;
    }

    public void setState(MapViewState state) {
        this.state = state;
        this.view.getHelpText().setText(state.getHelpText());
    }

}
