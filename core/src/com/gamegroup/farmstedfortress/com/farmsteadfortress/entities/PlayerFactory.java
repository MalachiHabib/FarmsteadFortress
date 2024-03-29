package com.farmsteadfortress.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.render.TileMap;


/**
 * Factory class for creating Player instances.
 */
public class PlayerFactory {

    private static final float DEFAULT_ANIMATION_SPEED = 1 / 5f;
    private static final float DEFAULT_PLAYER_SPEED = 240f;

    /**
     * Creates a new Player instance with default settings.
     *
     * @return The created Player instance.
     */
    public static Player createPlayer(Vector2 centerPosition, TileMap map) {
        return new Player(DEFAULT_ANIMATION_SPEED, DEFAULT_PLAYER_SPEED, centerPosition, map);
    }
}