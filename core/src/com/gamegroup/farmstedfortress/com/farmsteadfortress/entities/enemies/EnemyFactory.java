package com.farmsteadfortress.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.List;

/**
 * Factory class for creating Enemy instances.
 */
public class EnemyFactory {

    private static final float DEFAULT_ANIMATION_SPEED = 1 / 10f;
    private static final float DEFAULT_ENEMY_SPEED = 120f;
    private static final String BASIC_ENEMY_TEXTURE_ATLAS_PATH = "entities/player/playerAtlas.atlas";
    private static List<Enemy> enemies;

    public enum EnemyType {
        BASIC_ENEMY, BOSS_ENEMY,
    }

    /**
     * Creates a new Enemy instance with default settings based on the type.
     *
     * @param type The type of enemy to create.
     * @return The created Enemy instance.
     */
    public static Enemy createEnemy(EnemyType type) {
        switch (type) {
            case BASIC_ENEMY:
                return createBasicEnemy();
            default:
                return null;
        }
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }
    /**
     * Creates a BasicEnemy instance.
     *a
     * @return The created BasicEnemy instance.
     */
    private static BasicEnemy createBasicEnemy() {
        TextureAtlas atlas = new TextureAtlas(BASIC_ENEMY_TEXTURE_ATLAS_PATH);
        setTextureFilters(atlas);
        return new BasicEnemy(atlas, DEFAULT_ANIMATION_SPEED, DEFAULT_ENEMY_SPEED, 20, enemies);
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