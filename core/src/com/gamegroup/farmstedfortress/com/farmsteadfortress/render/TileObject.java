package com.farmsteadfortress.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TileObject {
    private Texture texture;
    private Vector2 position;

    public TileObject(Texture texture, Vector2 position) {
        this.texture = texture;
        this.position = position;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }
}
