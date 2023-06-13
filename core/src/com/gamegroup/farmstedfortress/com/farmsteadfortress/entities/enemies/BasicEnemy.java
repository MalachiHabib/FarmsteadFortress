package com.farmsteadfortress.entities.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.farmsteadfortress.entities.Player;

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
    public BasicEnemy(Player player, TextureAtlas atlas, float animationSpeed, float speed, int health, int attackDamage, float timeBetweenAttacks, List<Enemy> enemies) {
        super(player, atlas, animationSpeed, speed, health, 3, attackDamage,timeBetweenAttacks, enemies);
    }

    /**
     * Describes the basic enemy's death.
     * Overridden from the Enemy superclass.
     */
    @Override
    public void die() {
        enemies.remove(this);
        player.addMoney(reward);
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