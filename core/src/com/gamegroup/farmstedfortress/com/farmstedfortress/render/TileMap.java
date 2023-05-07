package com.farmstedfortress.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TileMap {

    private static final String WATER = "w";
    private static final String GROUND = "g";
    private int mapSize;
    private String[][] map;
    private Texture waterTexture;
    private Texture groundTexture;
    private LinkedList<Tile> baseTiles;
    private LinkedList<Tile> objectTiles;

    public TileMap() {
        mapSize = 120;
        waterTexture = new Texture("water.png");
        groundTexture = new Texture("sand.png");
        baseTiles = new LinkedList<>();
        objectTiles = new LinkedList<>();
        map = new String[mapSize][mapSize];

        generateInitialMap();
        smoothMap(2);
        printMap();
        fillMapWithTiles();
    }

    public List<Tile> getBaseTiles() {
        return baseTiles;
    }

    public List<Tile> getObjectTiles() {
        return objectTiles;
    }


    public void render(SpriteBatch batch) {
        for (Tile tile : baseTiles) {
            tile.render(batch);
        }
        for (Tile tile : objectTiles) {
            tile.render(batch);
        }
    }

    private void generateInitialMap() {
        Random random = new Random();
        fillMapWithWater();
        createIsland(random);
    }

    private void fillMapWithWater() {
        for (int row = 0; row < mapSize; row++) {
            for (int col = 0; col < mapSize; col++) {
                map[row][col] = WATER;
            }
        }
    }

    private void createIsland(Random random) {
        int islandSize = 40;
        int islandStart = (mapSize - islandSize) / 2;
        int islandCenterX = islandStart + islandSize / 2;
        int islandCenterY = islandStart + islandSize / 2;
        int maxDistanceFromCenter = islandSize / 2;

        for (int row = islandStart; row < islandStart + islandSize; row++) {
            for (int col = islandStart; col < islandStart + islandSize; col++) {
                double distanceFromCenter = Math.sqrt(Math.pow(row - islandCenterX, 2) + Math.pow(col - islandCenterY, 2));
                double distanceFactor = 1.0 - (distanceFromCenter / maxDistanceFromCenter);
                double randValue = random.nextDouble();

                if (randValue < distanceFactor) {
                    map[row][col] = GROUND;
                } else {
                    map[row][col] = WATER;
                }
            }
        }
    }

    private void smoothMap(int iterations) {
        for (int i = 0; i < iterations; i++) {
            map = createSmoothedMap(map);
        }
    }

    private String[][] createSmoothedMap(String[][] originalMap) {
        String[][] newMap = new String[mapSize][mapSize];

        for (int row = 0; row < mapSize; row++) {
            for (int col = 0; col < mapSize; col++) {
                int groundCount = 0;

                for (int r = -1; r <= 1; r++) {
                    for (int c = -1; c <= 1; c++) {
                        int neighborRow = row + r;
                        int neighborCol = col + c;

                        if (neighborRow >= 0 && neighborRow < mapSize && neighborCol >= 0 && neighborCol < mapSize) {
                            if (originalMap[neighborRow][neighborCol].equals(GROUND)) {
                                groundCount++;
                            }
                        }
                    }
                }
                if (groundCount > 4) {
                    newMap[row][col] = GROUND;
                } else {
                    newMap[row][col] = WATER;
                }
            }
        }

        return newMap;
    }

    private void fillMapWithTiles() {
        for (int row = mapSize - 1; row >= 0; row--) {
            for (int col = mapSize - 1; col >= 0; col--) {
                float x = (row - col) * 64 / 2.0001f;
                float y = (col + row) * 32 / 2f;

                if (map[row][col].equals(GROUND)) {
                    baseTiles.add(new Tile(groundTexture, new Vector2(row, col), new Vector2(x, y)));
                }
                if (map[row][col].equals(WATER)) {
                    baseTiles.add(new Tile(waterTexture, new Vector2(row, col), new Vector2(x, y)));
                }
            }
        }
    }

    private void printMap() {
        for (String[] row : map) {
            for (String tile : row) {
                System.out.print(tile + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
