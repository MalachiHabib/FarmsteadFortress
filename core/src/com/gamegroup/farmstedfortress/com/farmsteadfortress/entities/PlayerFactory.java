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

    private static final float DEFAULT_ANIMATION_SPEED = 1 / 10f;
    private static final float DEFAULT_PLAYER_SPEED = 240f;
    private static final String DEFAULT_TEXTURE_ATLAS_PATH = "entities/player/playerAtlas.atlas";

    /**
     * Creates a new Player instance with default settings.
     *
     * @return The created Player instance.
     */
    public static Player createPlayer(Vector2 centerPosition, TileMap map) {
        TextureAtlas atlas = new TextureAtlas(DEFAULT_TEXTURE_ATLAS_PATH);
        setTextureFilters(atlas);
        return new Player(atlas, DEFAULT_ANIMATION_SPEED, DEFAULT_PLAYER_SPEED, centerPosition, map);
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
