package com.farmsteadfortress.entities.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.List;

/**
 * Represents a basic enemy entity in the game.
 */
public class BasicEnemy extends Enemy {
    /**
     * Constructs a basic enemy entity.
     *
     * @param atlas          the texture atlas containing the walking animation frames
     * @param animationSpeed the speed of the walking animation
     * @param speed          the movement speed of the enemy
     * @param health         the enemy's health
     */
    public BasicEnemy(TextureAtlas atlas, float animationSpeed, float speed, int health, List<Enemy> enemies) {
        super(atlas, animationSpeed, speed, health, enemies);
    }

    /**
     * Describes the basic enemy's death.
     * Overridden from the Enemy superclass.
     */
    @Override
    public void die() {
        enemies.remove(this);
    }

    /**
     * Renders the basic enemy on the screen.
     * Overridden from the Enemy superclass.
     *
     * @param batch the sprite batch used for rendering
     */
    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}