package com.farmsteadfortress.path;

import java.util.List;

/**
 * Represents the result of a pathfinding operation.
 */
public class PathResult {
    private boolean success;
    private List<int[]> path;
    private List<String> pathPositions;

    /**
     * Constructor for PathResult.
     *
     * @param success        Indicates whether the pathfinding operation was successful.
     * @param path           A list of int arrays representing the path, with each int array containing the x and y coordinates of a point.
     * @param pathPositions  A list of strings representing the positions of the path.
     */
    public PathResult(boolean success, List<int[]> path, List<String> pathPositions) {
        this.success = success;
        this.path = path;
        this.pathPositions = pathPositions;
    }

    /**
     * Returns whether the pathfinding operation was successful.
     *
     * @return true if the operation was successful, false otherwise.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the path as a list of int arrays, where each int array contains the x and y coordinates of a point.
     *
     * @return The path as a list of int arrays.
     */
    public List<int[]> getPath() {
        return path;
    }
}