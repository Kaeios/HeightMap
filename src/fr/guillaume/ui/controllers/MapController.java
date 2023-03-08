package fr.guillaume.ui.controllers;

import fr.guillaume.data.MapStorage;
import fr.guillaume.math.graph.DjikstraSolver;
import fr.guillaume.math.graph.Graph;
import fr.guillaume.math.graph.Node;
import fr.guillaume.ui.rendering.Placeable;
import fr.guillaume.ui.rendering.tiles.CursorRenderer;
import fr.guillaume.ui.rendering.tiles.PathRenderer;
import fr.guillaume.ui.rendering.tiles.PathWeightRenderer;
import fr.guillaume.ui.views.MapView;

import java.awt.event.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MapController implements MouseListener, MouseMotionListener, ActionListener {

    private final MapStorage storage;

    private final MapView view;
    private ViewState state = ViewState.EDIT;

    private int startX = -1;
    private int startY = -1;

    private int endX = -1;
    private int endY = -1;

    private Graph solution;
    private DjikstraSolver solver;
    private List<Node> path = new LinkedList<>();

    public MapController(MapStorage storage, MapView view) {
        this.storage = storage;
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent event) {
        int xPos = event.getX();
        int yPos = event.getY();

        if(xPos > 32 && xPos < 32 + 64*10 && yPos > 32 && yPos < 32 + 64 * 10) {
            int newCursorX = (xPos - 32) / 64;
            int newCursorY = (yPos - 32) / 64;

            if(this.state.equals(ViewState.EDIT)) {
                view.getHeightMap().getMap()[newCursorX][newCursorY] = view.getHeightSlider().getValue();
                view.repaint();
            } else if(this.state.equals(ViewState.SELECT_START)){
                this.startX = newCursorX;
                this.startY = newCursorY;

                this.solution = this.view.getHeightMap().getGraph();
                solver = new DjikstraSolver(this.solution,
                        this.solution.getNodes().stream().filter(node -> node.getxPos() == this.startX && node.getyPos() == this.startY).findAny().get()
                );

                setState(ViewState.SELECT_END);

                view.refreshMap();
                view.repaint();
            } else if(this.state.equals(ViewState.SELECT_END)) {
                this.endX = newCursorX;
                this.endY = newCursorY;

                this.path = solver.getShortestPathTo(this.solution.getNodes().stream().filter(node -> node.getxPos() == this.endX && node.getyPos() == this.endY).findAny().get());

                setState(ViewState.SHOW_PATH);
                this.view.getComputeButton().setEnabled(true);

                view.refreshMap();
                view.repaint();
            }

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) {
        handleMouse(e, this.getState().equals(ViewState.EDIT));
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

        if(xPos > 32 && xPos < 32 + 64*10 && yPos > 32 && yPos < 32 + 64 * 10) {
            newCursorX = (xPos - 32) / 64;
            newCursorY = (yPos - 32) / 64;
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
            setState(ViewState.SELECT_START);
            this.view.getEditButton().setEnabled(true);
            this.view.getHeightSlider().setEnabled(false);
            view.getComputeButton().setEnabled(false);
        } else if(e.getSource().equals(view.getEditButton())) {
            setState(ViewState.EDIT);
            this.view.getComputeButton().setEnabled(true);
            this.view.getHeightSlider().setEnabled(true);
            this.view.getEditButton().setEnabled(false);
        } else if(e.getSource().equals(view.getSaveButton())) {
            try {
                this.storage.saveMap(this.view.getHeightMap());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        view.refreshMap();
        view.repaint();
    }

    public ViewState getState() {
        return state;
    }

    public enum ViewState {
        EDIT("(!) Cliquez sur la carte pour placer des tuiles"),
        SELECT_START("(!) Sélectionnez un point de départ"),
        SELECT_END("(!) Sélectionnez un point de d'arrivé"),
        SHOW_PATH("(!) Sélectionnez une action à effectuer");

        private final String helpText;

        ViewState(String helpText) {
            this.helpText = helpText;
        }
    }

    public List<Placeable> getStateOverlay() {
        List<Placeable> overlays = new LinkedList<>();
        if(this.getState().equals(ViewState.SELECT_END)) {
            PathWeightRenderer weightRenderer = new PathWeightRenderer(solution.getWeightMap(), 64);
            overlays.add(weightRenderer);
        } else if(this.getState().equals(ViewState.SHOW_PATH)) {
            PathRenderer pathRenderer = new PathRenderer(this.path, this.view.getHeightMap().getSizeX(), this.view.getHeightMap().getSizeY(), 64);
            overlays.add(pathRenderer);
        }

        return overlays;
    }

    public void setState(ViewState state) {
        this.state = state;

        this.view.getHelpText().setText(state.helpText);
    }

}
