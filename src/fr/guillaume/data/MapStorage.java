package fr.guillaume.data;

import fr.guillaume.math.IntVector2D;
import fr.guillaume.ui.rendering.tiles.TileMapRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

//TODO Change file format
public class MapStorage {

    private static final File DATA_FOLDER = new File("saved_maps/");
    private static final String THUMBNAIL_FILE_NAME = "thumbnail.png";
    private static final String MAP_FILE_NAME = "map.dat";

    private final Map<String, HeightMapDataHolder> loadedMaps = new LinkedHashMap<>();

    public MapStorage() {

    }

    public void initStorage() {
        if(!DATA_FOLDER.exists())
            DATA_FOLDER.mkdirs();

        for (File mapFolder : DATA_FOLDER.listFiles()) {

            String mapName = mapFolder.getName();

            File thumbnail = new File(mapFolder, THUMBNAIL_FILE_NAME);

            if(!thumbnail.exists()) // TODO Fallback to default thumbnail
                continue;

            File mapFile = new File(mapFolder, MAP_FILE_NAME);
            if(!mapFile.exists()) continue;

            try (BufferedReader reader = new BufferedReader(new FileReader(mapFile))){
                int[][] heightMap = new int[10][10];

                List<String> lines = reader.lines().collect(Collectors.toList());
                for (int i = 0; i < lines.size(); i++) {
                    heightMap[i] = Arrays.stream(lines.get(i).split(" ")).mapToInt(Integer::parseInt).toArray();
                }

                Image thumbnailImage = ImageIO.read(thumbnail);

                HeightMapDataHolder newMap = new HeightMapDataHolder(mapName, thumbnailImage, heightMap);
                this.loadedMaps.put(newMap.getProjectName(), newMap);
            } catch (IOException e) {
                e.printStackTrace();
            }

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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(mapFile))){
            for (int[] line : map.getMap()) {
                writer.write(Arrays.stream(line).mapToObj(String::valueOf).collect(Collectors.joining(" ")) + "\n");
            }

            writer.flush();
        }
    }

    public void saveExportedMap(HeightMapDataHolder map) throws IOException {
        File mapFolder = new File(DATA_FOLDER, map.getProjectName());

        if(!mapFolder.exists())
            mapFolder.mkdirs();

        File mapFile = new File(mapFolder, "exported.txt");
        if(!mapFolder.exists())
            mapFile.createNewFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(mapFile))){
            writer.write((map.getSize().getX()-2) + "\n");
            writer.write((map.getSize().getY()-2) + "\n");
            for (int xPos = 1; xPos < map.getSize().getX() - 1; xPos++) {
                for (int yPos = 1; yPos < map.getSize().getY() - 1; yPos++) {
                    writer.write(map.getHeightAt(new IntVector2D(xPos, yPos)) + "\n");
                }
            }

            writer.flush();
        }
    }

    public void loadImportedMap(File mapFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(mapFile))){
            int[][] heightMap = new int[10][10];

            for (int xPos = 0; xPos < heightMap.length; xPos++) {
                for (int yPos = 0; yPos < heightMap[0].length; yPos++) {
                    heightMap[xPos][yPos] = 20;
                }
            }

            List<String> lines = reader.lines().collect(Collectors.toList());
            int sizeX = Integer.parseInt(lines.get(0));
            int sizeY = Integer.parseInt(lines.get(1));

            for (int xPos = 0; xPos < sizeX; xPos++) {
                for (int yPos = 0; yPos < sizeY; yPos++) {
                    heightMap[xPos + 1][yPos + 1] = Integer.parseInt(lines.get(2 + yPos + xPos * sizeY));
                }
            }

//            Image thumbnailImage = ImageIO.read(thumbnail);

            HeightMapDataHolder newMap = new HeightMapDataHolder(mapFile.getName().split("\\.")[0], null, heightMap);
            this.loadedMaps.put(newMap.getProjectName(), newMap);

            saveMap(newMap);
            saveExportedMap(newMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(HeightMapDataHolder map) {
        this.loadedMaps.remove(map.getProjectName());
        File mapFolder = new File(DATA_FOLDER, map.getProjectName());
        if(!mapFolder.exists()) return;
        deleteFolder(mapFolder);
        mapFolder.delete();
    }

    public HeightMapDataHolder getMap(String id) {
        return loadedMaps.get(id).clone();
    }

    public List<HeightMapDataHolder> getMaps() {
        return new LinkedList<>(loadedMaps.values());
    }

    private static void deleteFolder(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteFolder(f);
            }
        }
        file.delete();
    }

    public void loadImportFolder() {
        File importFolder = new File("import/");
        for (File importFile : importFolder.listFiles()) {
            loadImportedMap(importFile);
            importFile.delete();
        }
    }

}
