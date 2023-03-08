package fr.guillaume.data;

import fr.guillaume.ui.rendering.tiles.TileMapRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MapStorage {

    private static final File DATA_FOLDER = new File("saved_maps/");
    private static final String THUMBNAIL_FILE_NAME = "thumbnail.png";
    private static final String MAP_FILE_NAME = "map.dat";

    private final Map<String, HeightMapDataHolder> loadedMaps = new LinkedHashMap<>();

    public MapStorage() {

    }

    public void initStorage() throws IOException {
        if(!DATA_FOLDER.exists())
            DATA_FOLDER.mkdirs();

        for (File mapFolder : DATA_FOLDER.listFiles()) {

            String mapName = mapFolder.getName();

            File thumbnail = new File(mapFolder, THUMBNAIL_FILE_NAME);

            if(!thumbnail.exists()) // TODO Fallback to default thumbnail
                continue;

            File mapFile = new File(mapFolder, MAP_FILE_NAME);
            if(!mapFile.exists()) continue;

            BufferedReader reader = new BufferedReader(new FileReader(mapFile));

            int[][] heightMap = new int[10][10];

            List<String> lines = reader.lines().collect(Collectors.toList());
            for (int i = 0; i < lines.size(); i++) {
                heightMap[i] = Arrays.stream(lines.get(i).split(" ")).mapToInt(Integer::parseInt).toArray();
            }

            Image thumbnailImage = ImageIO.read(thumbnail);

            HeightMapDataHolder newMap = new HeightMapDataHolder(mapName, thumbnailImage, heightMap);
            this.loadedMaps.put(newMap.getProjectName(), newMap);
        }
    }

    public void saveMap(HeightMapDataHolder map) throws IOException {
        this.loadedMaps.put(map.getProjectName(), map);
        File mapFolder = new File(DATA_FOLDER, map.getProjectName());

        if(!mapFolder.exists())
            mapFolder.mkdirs();

        Image newThumbnail = new TileMapRenderer(map, 64).render();
        map.setThumbnail(newThumbnail);

        ImageIO.write((BufferedImage) map.getThumbnail(), "PNG", new File(mapFolder, THUMBNAIL_FILE_NAME));

        File mapFile = new File(mapFolder, MAP_FILE_NAME);
        if(!mapFolder.exists())
            mapFile.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(mapFile));
        for (int[] line : map.getMap()) {
            writer.write(Arrays.stream(line).mapToObj(String::valueOf).collect(Collectors.joining(" ")) + "\n");
        }
        writer.flush();
        writer.close();
    }

    public HeightMapDataHolder getMap(String id) {
        return loadedMaps.get(id).clone();
    }

    public List<HeightMapDataHolder> getMaps() {
        return new LinkedList<>(loadedMaps.values());
    }

}
