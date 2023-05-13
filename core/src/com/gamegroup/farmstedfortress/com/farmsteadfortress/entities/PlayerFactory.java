package com.farmsteadfortress.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

/**
 * Factory class for creating Player instances.
 */
public class PlayerFactory {

    private static final float DEFAULT_ANIMATION_SPEED = 1 / 10f;
    private static final float DEFAULT_PLAYER_SPEED = 120f;
    private static final String DEFAULT_TEXTURE_ATLAS_PATH = "entities/player/playerAtlas.atlas";

    /**
     * Creates a new Player instance with default settings.
     *
     * @return The created Player instance.
     */
    public static Player createPlayer(Vector2 centerPosition) {
        TextureAtlas atlas = new TextureAtlas(DEFAULT_TEXTURE_ATLAS_PATH);
        return new Player(atlas, DEFAULT_ANIMATION_SPEED, DEFAULT_PLAYER_SPEED, centerPosition);
    }
}
