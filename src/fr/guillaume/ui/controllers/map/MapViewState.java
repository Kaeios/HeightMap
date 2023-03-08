package fr.guillaume.ui.controllers.map;

public enum MapViewState {

    EDIT("(!) Cliquez sur la carte pour placer des tuiles"),
    SELECT_START("(!) Sélectionnez un point de départ"),
    SELECT_END("(!) Sélectionnez un point de d'arrivé"),
    SHOW_PATH("(!) Sélectionnez une action à effectuer");

    private final String helpText;

    MapViewState(String helpText) {
        this.helpText = helpText;
    }

    public String getHelpText() {
        return helpText;
    }

}
