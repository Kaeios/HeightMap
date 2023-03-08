package fr.guillaume.ui.controllers.map;

import fr.guillaume.data.MapStorage;
import fr.guillaume.math.graph.Graph;
import fr.guillaume.math.graph.Node;
import fr.guillaume.math.graph.solving.Solver;
import fr.guillaume.math.graph.solving.SolverType;
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

public class MapController extends FullMouseController implements ActionListener {

    private final int tileSize;

    private final MapStorage storage;
    private final SolverType solverType = SolverType.DJIKSTRA;

    private final MapView view;
    private MapViewState state = MapViewState.EDIT;

    private int startX = -1;
    private int startY = -1;

    private int endX = -1;
    private int endY = -1;

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

        if(xPos > 32 && xPos < 32 + tileSize*this.view.getHeightMap().getSizeX()
                && yPos > 32 && yPos < 32 + tileSize *this.view.getHeightMap().getSizeY())
        {
            int newCursorX = (xPos - 32) / tileSize;
            int newCursorY = (yPos - 32) / tileSize;

            if(this.state.equals(MapViewState.EDIT)) {
                view.getHeightMap().getMap()[newCursorX][newCursorY] = view.getHeightSlider().getValue();
                view.repaint();
            } else if(this.state.equals(MapViewState.SELECT_START)){
                this.startX = newCursorX;
                this.startY = newCursorY;

                this.solution = this.view.getHeightMap().getGraph();
                this.solver = solverType.get(
                        this.solution,
                        this.solution.getNodes().stream().filter(node -> node.getxPos() == this.startX && node.getyPos() == this.startY).findAny().get()
                );

                setState(MapViewState.SELECT_END);

                view.refreshMap();
                view.repaint();
            } else if(this.state.equals(MapViewState.SELECT_END)) {
                this.endX = newCursorX;
                this.endY = newCursorY;

                this.path = solver.getShortestPathTo(this.solution.getNodes()
                        .stream()
                        .filter(node -> node.getxPos() == this.endX && node.getyPos() == this.endY)
                        .findAny()
                        .get()
                );

                setState(MapViewState.SHOW_PATH);
                this.view.getComputeButton().setEnabled(true);

                view.refreshMap();
                view.repaint();
            }

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

        int newCursorX;
        int newCursorY;

        boolean shouldPlace = false;

        if(xPos > 32 && xPos < 32 + tileSize * this.view.getHeightMap().getSizeX()
                && yPos > 32 && yPos < 32 + tileSize * this.view.getHeightMap().getSizeY()
        )
        {
            newCursorX = (xPos - 32) / tileSize;
            newCursorY = (yPos - 32) / tileSize;
            shouldPlace = placeTile;
        } else {
            newCursorX = -1;
            newCursorY = -1;
        }

        CursorRenderer cursor = view.getCursorRenderer();

        if(newCursorY == cursor.getCursorY() && cursor.getCursorX() == newCursorX) return;

        cursor.setCursorX(newCursorX);
        cursor.setCursorY(newCursorY);

        if(shouldPlace)
            view.getHeightMap().getMap()[newCursorX][newCursorY] = view.getHeightSlider().getValue();

        view.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(view.getComputeButton())) {
            setState(MapViewState.SELECT_START);
            this.view.getEditButton().setEnabled(true);
            this.view.getHeightSlider().setEnabled(false);
            view.getComputeButton().setEnabled(false);
        } else if(e.getSource().equals(view.getEditButton())) {
            setState(MapViewState.EDIT);
            this.view.getComputeButton().setEnabled(true);
            this.view.getHeightSlider().setEnabled(true);
            this.view.getEditButton().setEnabled(false);
        } else if(e.getSource().equals(view.getSaveButton())) {
            try {
                this.storage.saveMap(this.view.getHeightMap());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else if(e.getSource().equals(view.getLoadButton())) {
            this.view.setVisible(false);
            this.view.removeAll();
            this.view.setEnabled(false);

            LoadMapView loadView = new LoadMapView(this.storage);
            loadView.setVisible(true);
        }

        view.refreshMap();
        view.repaint();
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
            PathRenderer pathRenderer = new PathRenderer(this.path, this.view.getHeightMap().getSizeX(), this.view.getHeightMap().getSizeY(), 64);
            overlays.add(pathRenderer);
        }

        return overlays;
    }

    public void setState(MapViewState state) {
        this.state = state;
        this.view.getHelpText().setText(state.getHelpText());
    }

}
