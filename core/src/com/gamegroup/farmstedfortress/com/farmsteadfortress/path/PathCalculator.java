package com.farmsteadfortress.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Class that calculates the shortest path between two points on a given map using A* algorithm.
 */
public class PathCalculator {
    /**
     * Maximum allowed time for pathfinding, in milliseconds.
     */
    private static final long MAX_EXECUTION_TIME_MS = 500;
    private Map<String, Double> terrainWeights;

    public void clearTerrainWeights() {
        terrainWeights.clear();
    }

    public PathCalculator() {
        terrainWeights = new HashMap<>();
        terrainWeights.put("WG", Double.POSITIVE_INFINITY);
    }

    public double getTerrainWeight(String terrain) {
        if (terrainWeights.containsKey(terrain)) {
            return terrainWeights.get(terrain);
        } else {
            return 1.0;
        }
    }

    public void setTerrainWeight(String terrain, double weight) {
        terrainWeights.put(terrain, weight);
    }

    private double getWeight(String[][] map, int x, int y) {
        String terrain = map[x][y];
        return getTerrainWeight(terrain);
    }

    /**
     * Calculates paths for all spawn points to the center point on the given map.
     *
     * @param map A 2D String array representing the map.
     * @return A list of PathResult objects containing the success status and path for each spawn point.
     */
    public List<PathResult> calculatePath(String[][] map) {
        List<int[]> spawnPoints = findSpawnPoints(map);
        int[] centerPoint = findCenterPoint(map);

        if (spawnPoints.isEmpty() || centerPoint == null) {
            System.out.println("Could not find both characters in the map.");
            return null;
        }

        List<PathResult> pathResults = new ArrayList<>();
        for (int[] spawnPoint : spawnPoints) {
            PathResult pathResult = findPath(map, spawnPoint, centerPoint);
            pathResults.add(pathResult);
        }
        return pathResults;
    }

    /**
     * Finds all spawn points on the given map.
     *
     * @param map A 2D String array representing the map.
     * @return A list of int arrays representing the coordinates of the spawn points.
     */
    private List<int[]> findSpawnPoints(String[][] map) {
        List<int[]> spawnPoints = new ArrayList<>();
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[0].length; column++) {
                if (map[row][column].equals("S")) {
                    spawnPoints.add(new int[]{row, column});
                }
            }
        }
        return spawnPoints;
    }

    /**
     * Finds the center point on the given map.
     *
     * @param map A 2D String array representing the map.
     * @return An int array representing the coordinates of the center point, or null if not found.
     */
    private int[] findCenterPoint(String[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[0].length; column++) {
                if (map[row][column].equals("C")) {
                    return new int[]{row, column};
                }
            }
        }
        return null;
    }

    /**
     * Finds the shortest path between startPoint and endPoint using the A* algorithm.
     *
     * @param map        A 2D String array representing the map.
     * @param startPoint An int array representing the starting point coordinates.
     * @param endPoint   An int array representing the end point coordinates.
     * @return A PathResult object containing the success status and the path, if found.
     */
    public PathResult findPath(String[][] map, int[] startPoint, int[] endPoint) {
        long startTime = System.currentTimeMillis();

        double[][] gScore = initialiseScores(map);
        double[][] fScore = initialiseScores(map);

        gScore[startPoint[0]][startPoint[1]] = 0;
        fScore[startPoint[0]][startPoint[1]] = heuristicCostEstimate(startPoint, endPoint);

        PriorityQueue<int[]> openSet = new PriorityQueue<>(new PathComparator(fScore));
        openSet.offer(startPoint);

        Map<String, int[]> cameFrom = new HashMap<>();

        while (!openSet.isEmpty()) {
            if (isTimeoutExceeded(startTime)) {
                return new PathResult(false, null, null);
            }

            int[] current = openSet.poll();
            if (isEndPointReached(current, endPoint)) {
                return new PathResult(true, reconstructPath(cameFrom, current), reconstructPathPositions(cameFrom, current));
            }

            updateNeighbourScores(map, current, endPoint, gScore, fScore, openSet, cameFrom);
        }

        return new PathResult(false, null, null); // No path found
    }

    /**
     * Reconstructs the path positions from the start to the end point using the path information in cameFrom.
     *
     * @param cameFrom A Map containing the path information.
     * @param current  An int array representing the current point coordinates.
     * @return A list of strings representing the path positions.
     */
    private List<String> reconstructPathPositions(Map<String, int[]> cameFrom, int[] current) {
        List<String> pathPositions = new ArrayList<>();
        int position = 0;
        while (cameFrom.containsKey(current[0] + "," + current[1])) {
            pathPositions.add(String.valueOf(position));
            position++;
            current = cameFrom.get(current[0] + "," + current[1]);
        }
        pathPositions.add(String.valueOf(position));
        Collections.reverse(pathPositions);
        return pathPositions;
    }

    /**
     * Initialises the score matrices with positive infinity values.
     *
     * @param map A 2D String array representing the map.
     * @return A 2D double array containing the initialised scores.
     */
    private double[][] initialiseScores(String[][] map) {
        double[][] scores = new double[map.length][map[0].length];
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[0].length; column++) {
                scores[row][column] = Double.POSITIVE_INFINITY;
            }
        }
        return scores;
    }

    /**
     * Checks if the time spent on pathfinding exceeds the maximum allowed time.
     *
     * @param startTime The starting time of the pathfinding process in milliseconds.
     * @return true if the time spent exceeds the maximum allowed time, false otherwise.
     */
    private boolean isTimeoutExceeded(long startTime) {
        return System.currentTimeMillis() - startTime > MAX_EXECUTION_TIME_MS;
    }

    /**
     * Checks if the current point is the end point.
     *
     * @param current  An int array representing the current point coordinates.
     * @param endPoint An int array representing the end point coordinates.
     * @return true if the current point is the end point, false otherwise.
     */
    private boolean isEndPointReached(int[] current, int[] endPoint) {
        return current[0] == endPoint[0] && current[1] == endPoint[1];
    }

    /**
     * Updates neighbour scores and their paths.
     *
     * @param map      A 2D String array representing the map.
     * @param current  An int array representing the current point coordinates.
     * @param endPoint An int array representing the end point coordinates.
     * @param gScore   A 2D double array containing the g scores.
     * @param fScore   A 2D double array containing the f scores.
     * @param openSet  A PriorityQueue containing nodes to be processed.
     * @param cameFrom A Map containing the path information.
     */
    private void updateNeighbourScores(String[][] map, int[] current, int[] endPoint, double[][] gScore, double[][] fScore,
                                       PriorityQueue<int[]> openSet, Map<String, int[]> cameFrom) {
        int[][] neighbours = getNeighbours(current);

        for (int[] neighbour : neighbours) {
            int neighborX = neighbour[0];
            int neighborY = neighbour[1];

            if (isValid(map, neighborX, neighborY)) {
                double tentativeGScore = gScore[current[0]][current[1]] + getWeight(map, neighborX, neighborY);

                if (tentativeGScore < gScore[neighborX][neighborY]) {
                    cameFrom.put(neighborX + "," + neighborY, current);
                    gScore[neighborX][neighborY] = tentativeGScore;
                    fScore[neighborX][neighborY] = gScore[neighborX][neighborY] + heuristicCostEstimate(neighbour, endPoint);

                    openSet.remove(neighbour); // Remove the neighbor from the open set
                    openSet.offer(neighbour); // Add it back with the updated scores
                }
            }
        }
    }

    /**
     * Gets the potential neighbors of the current point.
     *
     * @param current An int array representing the current point coordinates.
     * @return A 2D int array containing the coordinates of the potential neighbors.
     */
    private int[][] getNeighbours(int[] current) {
        int x = current[0];
        int y = current[1];
        return new int[][]{{x - 1, y}, {x + 1, y}, {x, y - 1}, {x, y + 1}};
    }

    /**
     * Reconstructs the path from the start to the end point using the path information in cameFrom.
     *
     * @param cameFrom A Map containing the path information.
     * @param current  An int array representing the current point coordinates.
     * @return A list of int arrays representing the path, with each int array containing the x and y coordinates of a point.
     */
    private List<int[]> reconstructPath(Map<String, int[]> cameFrom, int[] current) {
        List<int[]> path = new ArrayList<>();
        while (cameFrom.containsKey(current[0] + "," + current[1])) {
            path.add(current);
            current = cameFrom.get(current[0] + "," + current[1]);
        }
        path.add(current);
        Collections.reverse(path);
        return path;
    }

    /**
     * Validates if the given coordinates are within the map boundaries.
     *
     * @param map A 2D String array representing the map.
     * @param x   The x-coordinate to be checked.
     * @param y   The y-coordinate to be checked.
     * @return true if the coordinates are within the map boundaries, false otherwise.
     */
    private boolean isValid(String[][] map, int x, int y) {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
    }

    /**
     * Calculates the heuristic cost estimate between two points using the Manhattan distance.
     *
     * @param pointA An int array representing the first point's coordinates.
     * @param pointB An int array representing the second point's coordinates.
     * @return A double value representing the heuristic cost estimate between the two points.
     */
    private double heuristicCostEstimate(int[] pointA, int[] pointB) {
        return Math.sqrt(Math.pow(pointA[0] - pointB[0], 2) + Math.pow(pointA[1] - pointB[1], 2));
    }
}