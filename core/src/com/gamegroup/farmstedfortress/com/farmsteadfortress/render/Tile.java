package com.farmsteadfortress.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Tile {



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
        CENTER
    }

    private static final TileType[] INTERACTABLE_TILES = {
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

    public Tile(Texture tileTexture, Vector2 tileMapPos, Vector2 worldPos, TileType tileType) {
        this.tileTexture = tileTexture;
        this.originalTile = tileTexture;
        this.tileMapPos = tileMapPos;
        this.worldPos = worldPos;
        this.tileType = tileType;
    }

    public void render(SpriteBatch batch) {
        batch.draw(tileTexture, worldPos.x, worldPos.y);
    }

    public void setTileTexture(Texture newTileTexture) {
        this.originalTile = newTileTexture;
    }
    public void setHoverTexture(Texture newTexture) {
        this.tileTexture = newTexture;
    }

    public Texture getTileTexture() {
        return tileTexture;
    }

    public Texture getOriginalTileTexture() {
        return originalTile;
    }

    public TileType getTileType() {
        return tileType;
    }

    public boolean containsWorldPosition(Vector2 worldPosition, float tileWidth, float tileHeight) {
        float halfWidth = tileWidth / 2.0f;
        float halfHeight = tileHeight / 2.0f;

        float relX = worldPosition.x - worldPos.x;
        float relY = worldPosition.y - worldPos.y;

        float xLimit = Math.abs(relY - halfHeight) / halfHeight * halfWidth;

        return relY >= halfHeight && relY < tileHeight && relX >= halfWidth - xLimit && relX < halfWidth + xLimit;
    }

    public boolean isInteractable() {
        for (TileType type : INTERACTABLE_TILES) {
            if (tileType == type) {
                return true;
            }
        }
        return false;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public Vector2 getPosition() {
        return worldPos;
    }
}