package com.farmsteadfortress.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Tile {
    private Texture tile;
    public Vector2 tileMapPos;
    public Vector2 worldPos;

    public Tile(Texture tile, Vector2 tileMapPos, Vector2 worldPos) {
        this.tile = tile;
        this.tileMapPos = tileMapPos;
        this.worldPos = worldPos;
    }

    public void render(SpriteBatch batch) {
        batch.draw(tile, worldPos.x, worldPos.y);
    }

    public Vector2 getPosition() {
        return worldPos;
    }
}
