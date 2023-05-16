package com.farmsteadfortress.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


/**
 * Factory class for creating Enemy instances.
 */
public class EnemyFactory {

    private static final float DEFAULT_ANIMATION_SPEED = 1 / 10f;
    private static final float DEFAULT_ENEMY_SPEED = 120f;
    private static final String DEFAULT_TEXTURE_ATLAS_PATH = "entities/player/playerAtlas.atlas";

    /**
     * Creates a new Enemy instance with default settings.
     *
     * @return The created Enemy instance.
     */
    public static Enemy createEnemy() {
        TextureAtlas atlas = new TextureAtlas(DEFAULT_TEXTURE_ATLAS_PATH);
        setTextureFilters(atlas);
        return new Enemy(atlas, DEFAULT_ANIMATION_SPEED, DEFAULT_ENEMY_SPEED);
    }

    /**
     * Set texture filters for all textures in the atlas.
     *
     * @param atlas The texture atlas.
     */
    private static void setTextureFilters(TextureAtlas atlas) {
        for (Texture texture : atlas.getTextures()) {
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }
}
