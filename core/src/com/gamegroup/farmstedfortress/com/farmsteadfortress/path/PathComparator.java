package com.farmsteadfortress.path;

import java.util.Comparator;

/**
 * Comparator for sorting nodes based on their distance values in a 2D array.
 */
public class PathComparator implements Comparator<int[]> {
    private double[][] nodeDistances;

    /**
     * Constructor for PathComparator.
     *
     * @param nodeDistances 2D array containing the distance values for each node.
     */
    public PathComparator(double[][] nodeDistances) {
        this.nodeDistances = nodeDistances;
    }

    /**
     * Compares two nodes based on their distance values.
     *
     * @param a The first node, represented by an int array of size 2 (x, y).
     * @param b The second node, represented by an int array of size 2 (x, y).
     * @return A negative, zero, or positive integer if the distance of the first node is less than, equal to, or greater than the distance of the second node.
     */
    @Override
    public int compare(int[] a, int[] b) {
        return Double.compare(nodeDistances[a[0]][a[1]], nodeDistances[b[0]][b[1]]);
    }
}
