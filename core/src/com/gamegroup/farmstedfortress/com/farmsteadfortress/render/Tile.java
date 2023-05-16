package com.farmsteadfortress.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.plants.Plant;
import com.farmsteadfortress.entities.plants.TomatoPlant;

/**
 * Represents a tile in the game world.
 */
public class Tile {
    public static final int TILE_SIZE = 192;

    /**
     * Types of tiles in the game.
     */
    public enum TileType {
        CROP_LAND,
        GRASS,
        MUSHROOM,
        ROCK,
        ROCK_TWO,
        OBJECT_TILE,
        WATER,
        BRIDGE,
        PATH,
        CENTER,
        SPAWN_POINT
    }

    private static final TileType[] INTRACTABLE_TILES = {
            TileType.CROP_LAND,
            TileType.GRASS,
            TileType.MUSHROOM,
            TileType.ROCK,
            TileType.ROCK_TWO,
            TileType.OBJECT_TILE
    };

    private Texture tileTexture;
    private Texture originalTile;
    public Vector2 tileMapPos;
    public Vector2 worldPos;
    private TileType tileType;
    private Plant plant;


    public Vector2 getTileMapPos() {
        return tileMapPos;
    }
    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Plant getPlant() {
        return this.plant;
    }

    /**
     * Creates a Tile object.
     *
     * @param tileTexture The texture of the tile.
     * @param tileMapPos  The position of the tile in the tile map.
     * @param worldPos    The world position of the tile.
     * @param tileType    The type of the tile.
     */
    public Tile(Texture tileTexture, Vector2 tileMapPos, Vector2 worldPos, TileType tileType) {
        this.tileTexture = tileTexture;
        this.originalTile = tileTexture;
        this.tileMapPos = tileMapPos;
        this.worldPos = worldPos;
        this.tileType = tileType;
    }

    /**
     * Renders the tile and plant using the provided SpriteBatch.
     *
     * @param batch The SpriteBatch used for rendering.
     */
    public void render(SpriteBatch batch) {
        batch.draw(tileTexture, worldPos.x, worldPos.y);
        if (plant != null) {
            System.out.println("render plant");
            plant.draw(batch);
        }
    }

    /**
     * Sets the tile texture to a new texture.
     *
     * @param newTileTexture The new texture to set.
     */
    public void setTileTexture(Texture newTileTexture) {
        this.originalTile = newTileTexture;
        this.tileTexture = newTileTexture;
    }

    /**
     * Sets the hover texture of the tile.
     *
     * @param newTexture The new hover texture to set.
     */
    public void setHoverTexture(Texture newTexture) {
        this.tileTexture = newTexture;
    }

    /**
     * Retrieves the original tile texture.
     *
     * @return The original tile texture.
     */
    public Texture getOriginalTileTexture() {
        return originalTile;
    }

    /**
     * Retrieves the type of the tile.
     *
     * @return The type of the tile.
     */
    public TileType getTileType() {
        return tileType;
    }

    /**
     * Checks if the tile contains a given world position.
     *
     * @param worldPosition The world position to check.
     * @return True if the tile contains the position, false otherwise.
     */
    public boolean containsWorldPosition(Vector2 worldPosition) {
        float halfWidth = TILE_SIZE / 2.0f;
        float halfHeight = TILE_SIZE / 2.0f;

        float relX = worldPosition.x - worldPos.x;
        float relY = worldPosition.y - worldPos.y;

        float xLimit = Math.abs(relY - halfHeight) / halfHeight * halfWidth;

        //Consider the tile to contain the point if it's within some small threshold of the tile's center
        return relY >= halfHeight - 5f && relY < TILE_SIZE + 5f && relX >= halfWidth - xLimit - 5f && relX < halfWidth + xLimit + 5f;
    }

    /**
     * Checks if the tile is intractable.
     *
     * @return True if the tile is intractable, false otherwise.
     */
    public boolean isIntractable() {
        for (TileType type : INTRACTABLE_TILES) {
            if (tileType == type) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the type of the tile.
     *
     * @param tileType The new type of the tile.
     */
    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    /**
     * Retrieves the position of the tile in world coordinates.
     *
     * @return The position of the tile.
     */
    public Vector2 getPosition() {
        return worldPos;
    }
}