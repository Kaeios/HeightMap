package fr.guillaume.ui.rendering.tiles;

import fr.guillaume.data.MapStorage;
import fr.guillaume.ui.rendering.Renderer;

import java.awt.*;

public class ThumbnailRenderer implements Renderer {

    private final MapStorage storage;
    private final String projectId;

    public ThumbnailRenderer(MapStorage storage, String projectId) {
        this.storage = storage;
        this.projectId = projectId;
    }

    @Override
    public Image render() {
        return storage.getMap(projectId).getThumbnail();
    }

}
