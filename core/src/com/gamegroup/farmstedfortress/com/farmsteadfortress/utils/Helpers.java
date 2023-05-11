package com.farmsteadfortress.utils;

import com.badlogic.gdx.math.Vector2;

public class Helpers {

    /**
     * Converts grid coordinates to world position based on the tile dimensions.
     *
     * @param gridX      The x-coordinate in the grid.
     * @param gridY      The y-coordinate in the grid.
     * @param tileWidth  The width of a tile in world units.
     * @param tileHeight The height of a tile in world units.
     * @return The world position as a Vector2.
     */
    public static Vector2 gridToWorldPosition(int gridX, int gridY, float tileWidth, float tileHeight) {
        float worldX = (gridX - gridY) * tileWidth / 2;
        float worldY = (gridX + gridY) * tileHeight / 2;
        return new Vector2(worldX, worldY);
    }
}
