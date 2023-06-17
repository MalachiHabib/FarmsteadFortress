package com.farmsteadfortress.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.entities.plants.Plant;

import java.util.List;

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
        CLASSIC_GRASS,
        MUSHROOM,
        ROCK,
        ROCK_TWO,
        OBJECT_TILE,
        WATER,
        BRIDGE,
        PATH,
        CENTER,
        MEADOW_GRASS,
        EVERGREEN_GRASS,
        FLOWER, SPAWN_POINT
    }

    private static final TileType[] INTRACTABLE_TILES = {
            TileType.CROP_LAND,
            TileType.CLASSIC_GRASS,
            TileType.MUSHROOM,
            TileType.ROCK,
            TileType.FLOWER,
            TileType.ROCK_TWO,
            TileType.OBJECT_TILE,
            TileType.EVERGREEN_GRASS,
            TileType.MEADOW_GRASS
    };

    private Texture tileTexture;
    private Texture originalTile;
    public Vector2 tileMapPos;
    public Vector2 worldPos;
    private TileType tileType;
    private TileObject tileObject;
    private Plant plant;

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

    public void setHoverTexture(Texture newTexture) {
        this.tileTexture = newTexture;
    }

    public Texture getOriginalTileTexture() {
        return originalTile;
    }

    public TileType getTileType() {
        return tileType;
    }

    public Vector2 getPosition() {
        return worldPos;
    }
    /**
     * Renders the tile and plant using the provided SpriteBatch.
     *
     * @param batch The SpriteBatch used for rendering.
     */
    public void render(SpriteBatch batch) {
        batch.draw(tileTexture, worldPos.x, worldPos.y);
        if (plant != null) {
            plant.draw(batch);
        }
        if (tileObject != null) {
            tileObject.draw(batch);
        }
    }

    public void update(float delta, List<Enemy> enemies) {
        if (plant != null) {
            plant.update(delta, enemies);
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

        return relY >= halfHeight - 2f && relY < TILE_SIZE + 2f && relX >= halfWidth - xLimit - 5f && relX < halfWidth + xLimit + 5f;
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
}