package com.farmstedfortress.render;

/**
 * TileMap class generates a randomised 2D tile map with a circular shaped island, it also calculates a path through it.
 * It fills the map with textures and creates corresponding Tile objects for rendering.
 *
 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.farmstedfortress.path.PathCalculator;
import com.farmstedfortress.path.PathResult;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TileMap {
    private static final String WATER = "W";
    private static final String GROUND = "G";
    private static final String CENTER = "C";
    private static final String SPAWN_POINT = "S";
    private static final String BRIDGE = "B";
    private static final String PATH = "P";
    private int mapSize;
    private String[][] map;
    private Texture waterTexture, groundTexture, baseTexture, bridgeTexture, pathTexture;
    private LinkedList<Tile> baseTiles;
    private LinkedList<Tile> objectTiles;

    /**
     * Constructor for TileMap class. Generates the tile map, calculates a path through it,
     * and fills the map with tiles.
     */
    public TileMap() {
        mapSize = 120;
        waterTexture = new Texture("water.png");
        groundTexture = new Texture("sand.png");
        baseTexture = new Texture("stone.png");
        pathTexture = new Texture("purple.png");
        bridgeTexture = new Texture("bridge.png");

        baseTiles = new LinkedList<>();
        objectTiles = new LinkedList<>();

        boolean successfulPathFound = false;
        PathCalculator pathCalculator = new PathCalculator();
        List<PathResult> pathResults;

        while (!successfulPathFound) {
            map = new String[mapSize][mapSize];
            generateInitialMap();
            smoothMap(2);
            addAdditionalCenterTiles();

            pathResults = pathCalculator.calculatePath(map);

            if (pathResults != null) {
                for (PathResult pathResult : pathResults) {
                    if (pathResult.isSuccess()) {
                        successfulPathFound = true;
                        List<int[]> path = pathResult.getPath();
                        setPathPoints(path);
                    }
                }
            }

            if (!successfulPathFound) {
                System.out.println("Pathfinding timed out, generating a new map...");
            }
        }

        fillMapWithTiles();
        printMap();
    }

    /**
     * Sets the tile map with path points indicated by the "P" string in the map.
     * @param path List of int[] representing the x, y coordinates of the path.
     */
    public void setPathPoints(List<int[]> path) {
        int islandSize = 40;
        int islandStart = (mapSize - islandSize) / 2;
        int islandCenterX = islandStart + islandSize / 2;
        int islandCenterY = islandStart + islandSize / 2;

        for (int[] point : path) {
            int row = point[0];
            int col = point[1];
            if (row >= 0 && row < mapSize && col >= 0 && col < mapSize) {
                if (row == islandCenterX && col == islandCenterY) {
                    map[row][col] = CENTER; // set the tile to "P" to indicate it's part of the path
                } else {
                    map[row][col] = PATH; // set the tile to "P" to indicate it's part of the path
                }
            }
        }
    }


    /**
     * Returns a list of base tiles to be rendered.
     * @return List of Tile objects representing the base tiles.
     */
    public List<Tile> getBaseTiles() {
        return baseTiles;
    }
    /**
     * Returns a list of object tiles to be rendered.
     * @return List of Tile objects representing the object tiles.
     */
    public List<Tile> getObjectTiles() {
        return objectTiles;
    }

    /**
     * Renders the base and object tiles onto the screen.
     * @param batch SpriteBatch object for rendering the tiles.
     */
    public void render(SpriteBatch batch) {
        for (Tile tile : baseTiles) {
            tile.render(batch);
        }
        for (Tile tile : objectTiles) {
            tile.render(batch);
        }
    }

    /**
     * Generates the initial map with all water tiles.
     */
    private void generateInitialMap() {
        Random random = new Random();
        fillMapWithWater();
        createIsland(random);
    }

    /**
     * Fills the map with water tiles.
     */
    private void fillMapWithWater() {
        for (int row = 0; row < mapSize; row++) {
            for (int col = 0; col < mapSize; col++) {
                map[row][col] = WATER;
            }
        }
    }

    /**
     * Creates a circular island with ground tiles.
     * @param random Random object for creating random values.
     */
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

                if (row == islandCenterX && col == islandCenterY) {
                    map[row][col] = CENTER; // Set the 3x3 center tiles to "C"
                } else if (randValue < distanceFactor) {
                    map[row][col] = GROUND;
                } else {
                    map[row][col] = WATER;
                }
            }
        }
    }

    /**
     * Smooths the map by changing tiles with fewer than four ground tiles adjacent
     * to ground tiles to water tiles, and vice versa.
     * @param iterations Number of times the smoothing operation should be run.
     */
    private void smoothMap(int iterations) {
        for (int i = 0; i < iterations; i++) {
            map = createSmoothedMap(map);
        }
    }

    /**
     * Helper method for smoothMap method. Returns a new smoothed map based on the original map.
     * @param originalMap String[][] representing the original map.
     * @return String[][] representing the new smoothed map.
     */
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

                if (originalMap[row][col].equals(WATER) || originalMap[row][col].equals(GROUND)) {
                    if (groundCount > 4) {
                        newMap[row][col] = GROUND;
                    } else {
                        newMap[row][col] = WATER;
                    }
                } else {
                    newMap[row][col] = originalMap[row][col]; // Preserve other tile types
                }
            }
        }

        return newMap;
    }

    /**
     Adds additional spawn points in random locations on the map's edges.
     Chooses two sides of the map to spawn on, and for each side, finds the closest ground tile from the shore.
     Sets the tile at the closest ground tile to a spawn point.
     */
    private void addAdditionalCenterTiles() {
        Random random = new Random();
        int sidesToSpawn = 2;
        List<Integer> availableSides = new LinkedList<>(Arrays.asList(0, 1, 2, 3));

        for (int i = 0; i < sidesToSpawn; i++) {
            int sideIndex = random.nextInt(availableSides.size());
            int side = availableSides.get(sideIndex);
            availableSides.remove(sideIndex);

            int closestDistance = Integer.MAX_VALUE;
            int closestRow = -1, closestCol = -1;

            for (int row = 0; row < mapSize; row++) {
                for (int col = 0; col < mapSize; col++) {
                    if (map[row][col].equals(GROUND)) {
                        int distance;
                        switch (side) {
                            case 0: // Top side
                                distance = row;
                                break;
                            case 1: // Right side
                                distance = mapSize - 1 - col;
                                break;
                            case 2: // Bottom side
                                distance = mapSize - 1 - row;
                                break;
                            default: // Left side
                                distance = col;
                                break;
                        }

                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closestRow = row;
                            closestCol = col;
                        }
                    }
                }
            }

            if (closestRow != -1 && closestCol != -1) {
                map[closestRow][closestCol] = SPAWN_POINT;
            }
        }
    }

    /**
     * Fills the map with tiles for rendering.
     * Iterates over the map array, and depending on the tile type, adds a corresponding tile object to either
     * the baseTiles or objectTiles LinkedList.
     * For each tile, determines the x and y position to render it at, based on the row and column.
     * If a tile is a path tile, checks its adjacent tiles to see if any are water tiles, and if so, adds a bridge
     * tile to baseTiles instead of a path tile.
     */
    private void fillMapWithTiles() {
        for (int row = mapSize - 1; row >= 0; row--) {
            for (int col = mapSize - 1; col >= 0; col--) {
                float x = (row - col) * 64 / 2.0001f;
                float y = (col + row) * 32 / 2f;

                switch (map[row][col]) {
                    case PATH:
                        // Check for adjacent water blocks
                        int[][] neighbors = {{row - 1, col}, {row + 1, col}, {row, col - 1}, {row, col + 1}};
                        boolean adjacentWater = false;
                        for (int[] neighbor : neighbors) {
                            int neighborRow = neighbor[0];
                            int neighborCol = neighbor[1];
                            if (isValid(neighborRow, neighborCol) && map[neighborRow][neighborCol].equals(WATER)) {
                                adjacentWater = true;
                                break;
                            }
                        }
                        if (adjacentWater) {
                            map[row][col] = BRIDGE;
                            baseTiles.add(new Tile(bridgeTexture, new Vector2(row, col), new Vector2(x, y)));
                        } else {
                            baseTiles.add(new Tile(pathTexture, new Vector2(row, col), new Vector2(x, y)));
                        }
                        break;
                    case CENTER:
                        baseTiles.add(new Tile(baseTexture, new Vector2(row, col), new Vector2(x, y)));
                        break;
                    case GROUND:
                        baseTiles.add(new Tile(groundTexture, new Vector2(row, col), new Vector2(x, y)));
                        break;
                    case WATER:
                        baseTiles.add(new Tile(waterTexture, new Vector2(row, col), new Vector2(x, y)));
                        break;
                }
            }
        }
    }

    /**
     Checks if the specified row and column are valid coordinates within the map.
     @param row The row index.
     @param col The column index.
     @return True if the coordinates are valid, false otherwise.
     */
    private boolean isValid(int row, int col) {
        return row >= 0 && row < mapSize && col >= 0 && col < mapSize;
    }

    /**
     Prints the current state of the map to the console.
     */
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
