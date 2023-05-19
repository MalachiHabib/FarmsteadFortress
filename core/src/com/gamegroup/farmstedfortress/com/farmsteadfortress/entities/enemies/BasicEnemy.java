package com.farmsteadfortress.entities.enemies;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.farmsteadfortress.entities.Player;

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
    public BasicEnemy(TextureAtlas atlas, float animationSpeed, float speed, int health) {
        super(atlas, animationSpeed, speed, health);
    }

    /**
     * Handles the basic enemy being attacked.
     *
     * @param player the player attacking the enemy
     */
    @Override
    public void attacked(Player player) {
        health -= player.getAttackDamage();
        if(health <= 0) {
            die();
        }
    }

    /**
     * Describes the basic enemy's death.
     * Overridden from the Enemy superclass.
     */
    @Override
    public void die() {
        // implementation for what happens when the basic enemy dies...
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