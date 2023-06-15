package com.farmsteadfortress.render;

/**
 * TileMap class generates a randomised 2D tile map with a circular shaped island, it also calculates a path through it.
 * It fills the map with textures and creates corresponding Tile objects for rendering.
 */

import static com.badlogic.gdx.math.MathUtils.random;
import static com.farmsteadfortress.utils.Helpers.gridToWorldPosition;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.plants.Plant;
import com.farmsteadfortress.entities.plants.PlantFactory;
import com.farmsteadfortress.path.PathCalculator;
import com.farmsteadfortress.path.PathResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TileMap {
    private static final double DIFFICULTY = 0.5f;
    private static final String WATER = "W";
    private static final String GROUND = "G";
    private static final String WEIGHTED_GROUND = "WG";
    private static final String CENTER = "C";
    private static final String SPAWN_POINT = "S";
    private static final String PATH = "P";
    private static final String EXTRA_WEIGHTED_GROUND = "XWG";
    private static final int ISLAND_SIZE = 27;
    private List<int[]> path;
    private List<List<int[]>> successfulPaths;
    private List<PathResult> pathResults;
    private int mapSize;
    private String[][] map;
    private Texture waterTexture, waterBorder, waterFar, middleTexture, bridgeTexture, pathTextureTop, cropLandTexture, grassTexture, grassYellowTexture, grassBlueTexture, enemySpawnPointTexture, crystalTexture;
    private LinkedList<Tile> baseTiles;
    private LinkedList<Tile> objectTiles;

    /**
     * Constructor for TileMap class. Generates the tile map, calculates a path through it,
     * and fills the map with tiles.
     */
    public TileMap() {
        mapSize = 120;
        crystalTexture = new Texture("objects/crystal/crystal.png");
        cropLandTexture = new Texture("tiles/crop_land.png");
        cropLandTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        grassTexture = new Texture("tiles/grass.png");
        grassTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        grassYellowTexture = new Texture("tiles/grass_yellowish.png");
        grassYellowTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        grassBlueTexture = new Texture("tiles/grass_blueish.png");
        grassBlueTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        waterTexture = new Texture("tiles/water.png");
        waterTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        waterFar = new Texture("tiles/water_far.png");
        waterFar.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        waterBorder = new Texture("tiles/water_border.png");
        waterBorder.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        enemySpawnPointTexture = new Texture("tiles/middle.png");
        enemySpawnPointTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        middleTexture = new Texture("tiles/middle_purple.png");
        middleTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        pathTextureTop = new Texture("tiles/path.png");
        pathTextureTop.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        bridgeTexture = new Texture("tiles/bridge.png");
        bridgeTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        baseTiles = new LinkedList<>();
        objectTiles = new LinkedList<>();

        boolean successfulPathFound = false;
        PathCalculator pathCalculator = new PathCalculator();
        successfulPaths = new ArrayList<>();

        while (!successfulPathFound) {
            map = new String[mapSize][mapSize];
            generateInitialMap();
            smoothMap(2);
            addAdditionalCenterTiles();
            pathResults = pathCalculator.calculatePath(map);

            if (pathResults != null) {
                int successfulPathCount = 0;
                for (PathResult pathResult : pathResults) {
                    if (pathResult.isSuccess()) {
                        successfulPathCount++;
                        path = pathResult.getPath();
                        successfulPaths.add(path);
                        setPathPoints(path);
                    }
                }
                if (successfulPathCount == 3) {
                    successfulPathFound = true;
                }
            }
            if (!successfulPathFound) {
                System.out.println("Pathfinding timed out, generating a new map...");
            }
        }
        //printMap();
        fillMapWithTiles();
        fillMapWithObjects();
    }

    /**
     * Sets the tile map with path points indicated by the "P" string in the map.
     *
     * @param path List of int[] representing the x, y coordinates of the path.
     */
    public void setPathPoints(List<int[]> path) {
        int islandStart = (mapSize - ISLAND_SIZE) / 2;
        int islandCenterX = islandStart + ISLAND_SIZE / 2;
        int islandCenterY = islandStart + ISLAND_SIZE / 2;

        for (int[] point : path) {
            int row = point[0];
            int col = point[1];
            if (row >= 0 && row < mapSize && col >= 0 && col < mapSize) {
                if (row == islandCenterX && col == islandCenterY) {
                    map[row][col] = CENTER; // set the tile to "P" to indicate it's part of the path
                } else if (!Objects.equals(map[row][col], SPAWN_POINT)) {
                    map[row][col] = PATH; // set the tile to "P" to indicate it's part of the path
                }
            }
        }
    }

    /**
     * Retrieves the tile at the specified world position.
     *
     * @param worldPosition The world position to check.
     * @return The tile at the specified position, or null if no tile is found.
     */
    public Tile getTileAt(Vector2 worldPosition) {
        for (Tile tile : baseTiles) {
            if (tile.containsWorldPosition(worldPosition)) {
                return tile;
            }
        }
        return null;
    }

    /**
     * Returns a list of base tiles to be rendered.
     *
     * @return List of Tile objects representing the base tiles.
     */
    public List<Tile> getBaseTiles() {
        return baseTiles;
    }

    /**
     * Returns a list of object tiles to be rendered.
     *
     * @return List of Tile objects representing the object tiles.
     */
    public List<Tile> getObjectTiles() {
        return objectTiles;
    }

    /**
     * Returns a list of object tiles to be rendered.
     *
     * @return A list of Tile objects representing the object tiles.
     */
    public List<List<int[]>> getEnemyPaths() {
        return successfulPaths;
    }

    /**
     * Renders the base and object tiles onto the screen.
     *
     * @param batch SpriteBatch object for rendering the tiles.
     */
    public void render(SpriteBatch batch) {
        batch.begin();
        for (Tile tile : baseTiles) {
            tile.render(batch);
        }
        for (Tile tile : objectTiles) {
            tile.render(batch);
        }
        batch.end();
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
     *
     * @param random Random object for creating random values.
     */
    private void createIsland(Random random) {
        int islandStart = (mapSize - ISLAND_SIZE) / 2;
        int islandCenterX = islandStart + ISLAND_SIZE / 2;
        int islandCenterY = islandStart + ISLAND_SIZE / 2;
        int maxDistanceFromCenter = ISLAND_SIZE / 2;

        for (int row = islandStart; row < islandStart + ISLAND_SIZE; row++) {
            for (int col = islandStart; col < islandStart + ISLAND_SIZE; col++) {
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
     *
     * @param iterations Number of times the smoothing operation should be run.
     */
    private void smoothMap(int iterations) {
        for (int i = 0; i < iterations; i++) {
            map = createSmoothedMap(map);
        }
    }

    /**
     * Helper method for smoothMap method. Returns a new smoothed map based on the original map.
     *
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
                            if (originalMap[neighborRow][neighborCol].equals(GROUND) || originalMap[neighborRow][neighborCol].equals(WEIGHTED_GROUND)) {
                                groundCount++;
                            }
                        }
                    }
                }

                if (originalMap[row][col].equals(WATER) || originalMap[row][col].equals(GROUND) || originalMap[row][col].equals(WEIGHTED_GROUND)) {
                    if (groundCount > 4) {
                        if (random.nextDouble() < DIFFICULTY) {
                            if (originalMap[row][col].equals(WEIGHTED_GROUND) && random.nextDouble() < 0.95) {
                                newMap[row][col] = EXTRA_WEIGHTED_GROUND;
                            } else {
                                newMap[row][col] = WEIGHTED_GROUND;
                            }
                        } else {
                            newMap[row][col] = GROUND;
                        }
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
     * Adds additional spawn points in random locations on the map's edges.
     * Chooses two sides of the map to spawn on, and for each side, finds the closest ground tile from the shore.
     * Sets the tile at the closest ground tile to a spawn point.
     */
    private void addAdditionalCenterTiles() {
        Random random = new Random();
        int sidesToSpawn = 3;
        List<Integer> availableSides = new LinkedList<>(Arrays.asList(0, 1, 2, 3));

        for (int i = 0; i < sidesToSpawn; i++) {
            int sideIndex = random.nextInt(availableSides.size());
            int side = availableSides.get(sideIndex);
            availableSides.remove(sideIndex);

            int closestDistance = Integer.MAX_VALUE;
            int closestRow = -1, closestCol = -1;

            for (int row = 0; row < mapSize; row++) {
                for (int col = 0; col < mapSize; col++) {
                    if (map[row][col].equals(GROUND) || map[row][col].equals(WEIGHTED_GROUND)) {
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
                float x = (row - col) * Tile.TILE_SIZE / 2.0001f;
                float y = (col + row) * Tile.TILE_SIZE / 4f;

                // Check for adjacent water blocks
                int[][] neighbors = {{row - 1, col}, {row + 1, col}, {row, col - 1}, {row, col + 1}};
                boolean adjacentWater = false;
                for (int[] neighbour : neighbors) {
                    int neighborRow = neighbour[0];
                    int neighborCol = neighbour[1];
                    if (isValid(neighborRow, neighborCol) && map[neighborRow][neighborCol].equals(WATER)) {
                        adjacentWater = true;
                        break;
                    }
                }

                // Check for adjacent non-water blocks
                boolean adjacentNonWater = false;
                for (int[] neighbour : neighbors) {
                    int neighborRow = neighbour[0];
                    int neighborCol = neighbour[1];
                    if (isValid(neighborRow, neighborCol) && !map[neighborRow][neighborCol].equals(WATER)) {
                        adjacentNonWater = true;
                        break;
                    }
                }

                // Check for water blocks 4 blocks away from non-water blocks
                boolean nearNonWater = false;
                for (int distance = 1; distance <= 4; distance++) {
                    int[][] farNeighbors = {
                            {row - distance, col}, {row + distance, col},
                            {row, col - distance}, {row, col + distance},
                            {row - distance, col - distance}, {row + distance, col + distance},
                            {row - distance, col + distance}, {row + distance, col - distance}
                    };
                    for (int[] farNeighbor : farNeighbors) {
                        int farNeighborRow = farNeighbor[0];
                        int farNeighborCol = farNeighbor[1];
                        if (isValid(farNeighborRow, farNeighborCol) && !map[farNeighborRow][farNeighborCol].equals(WATER)) {
                            nearNonWater = true;
                            break;
                        }
                    }
                }

                switch (map[row][col]) {
                    case PATH:
                        if (adjacentWater) {
                            baseTiles.add(new Tile(bridgeTexture, new Vector2(row, col), new Vector2(x, y), Tile.TileType.BRIDGE));
                        } else {
                            baseTiles.add(new Tile(pathTextureTop, new Vector2(row, col), new Vector2(x, y), Tile.TileType.PATH));
                        }
                        break;

                    case CENTER:
                        baseTiles.add(new Tile(middleTexture, new Vector2(row, col), new Vector2(x, y), Tile.TileType.CENTER));
                        break;
                    case EXTRA_WEIGHTED_GROUND:
                    case GROUND:
                    case WEIGHTED_GROUND:
                        Texture groundSelectedTexture;
                        Tile.TileType tileType = null;
                        int randomNum = new Random().nextInt(100) + 1;
                        if (randomNum <= 80) {
                            groundSelectedTexture = grassTexture;
                            tileType = Tile.TileType.GRASS;
                        } else if (randomNum <= 90) {
                            groundSelectedTexture = grassYellowTexture;
                            tileType = Tile.TileType.GRASS_YELLOW;
                        } else if (randomNum <= 95) {
                            groundSelectedTexture = grassBlueTexture;
                            tileType = Tile.TileType.GRASS_BLUE;
                        } else {
                            groundSelectedTexture = grassTexture;
                            tileType = Tile.TileType.ROCK;
                        }
                        baseTiles.add(new Tile(groundSelectedTexture, new Vector2(row, col), new Vector2(x, y), tileType));
                        break;
                    case WATER:
                        Texture selectedTexture;
                        if (adjacentNonWater) {
                            selectedTexture = waterBorder;
                        } else if (nearNonWater) {
                            selectedTexture = waterTexture;
                        } else {
                            selectedTexture = waterFar;
                        }
                        baseTiles.add(new Tile(selectedTexture, new Vector2(row, col), new Vector2(x, y), Tile.TileType.WATER));
                        break;

                    case SPAWN_POINT:
                        baseTiles.add(new Tile(enemySpawnPointTexture, new Vector2(row, col), new Vector2(x, y), Tile.TileType.SPAWN_POINT));
                        break;
                }
            }
        }
    }

    /**
     * Fills the map with objects.
     * Randomly places objects on crop land tiles.
     */
    private void fillMapWithObjects() {


        Random random = new Random();
        for (Tile baseTile : baseTiles) {
            Plant plant = PlantFactory.createPlant(Plant.PlantType.FERN, baseTile, null, null);
            if (baseTile.getTileType().equals(Tile.TileType.GRASS) || baseTile.getTileType().equals(Tile.TileType.GRASS_BLUE) || baseTile.getTileType().equals(Tile.TileType.GRASS_YELLOW)) {
                int randomNumber = random.nextInt(10);
                if (randomNumber < 1) {
                    baseTile.setPlant(plant);
                }
            } else if (baseTile.getTileType().equals(Tile.TileType.CENTER)) {
                Vector2 objectPosition = new Vector2(baseTile.getPosition().x, baseTile.getPosition().y + Tile.TILE_SIZE / 2);

            }
        }
    }

    /**
     * Checks if the specified row and column are valid coordinates within the map.
     *
     * @param row The row index.
     * @param col The column index.
     * @return True if the coordinates are valid, false otherwise.
     */
    private boolean isValid(int row, int col) {
        return row >= 0 && row < mapSize && col >= 0 && col < mapSize;
    }

    /**
     * Prints the current state of the map to the console.
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

    /**
     * @return the map.
     */
    public String[][] getMap() {
        return map;
    }

    /**
     * Retrieves the position of the center tile in the map.
     *
     * @return The position of the center tile as a Vector2 object,
     * or null if no center tile is found.
     */
    public Vector2 getCenterTilePos() {
        for (int row = 0; row < mapSize; row++) {
            for (int col = 0; col < mapSize; col++) {
                if (map[row][col] == CENTER) {
                    return gridToWorldPosition(row, col, Tile.TILE_SIZE, (float) Tile.TILE_SIZE / 2);
                }
            }
        }
        return null;
    }
}