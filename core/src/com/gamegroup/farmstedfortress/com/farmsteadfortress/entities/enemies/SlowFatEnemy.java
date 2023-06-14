package com.farmsteadfortress.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.farmsteadfortress.entities.Player;

import java.util.List;

/**
 * Represents a basic enemy entity in the game.
 */
public class SlowFatEnemy extends Enemy {
    /**
     * Constructs a slow & fat enemy entity.
     *
     * @param atlas          the texture atlas containing the walking animation frames
     * @param animationSpeed the speed of the walking animation
     * @param speed          the movement speed of the enemy
     * @param health         the enemy's health
     */
    public SlowFatEnemy(Player player, TextureAtlas atlas, float animationSpeed, float speed, int health, int attackDamage, float timeBetweenAttacks, List<Enemy> enemies) {
        super(player, atlas, animationSpeed, speed, health, 3, attackDamage,timeBetweenAttacks, enemies);

        TextureAtlas idleAtlasN = new TextureAtlas(Gdx.files.internal("entities/Warhog/Idle_N.atlas"));
        TextureAtlas idleAtlasE = new TextureAtlas(Gdx.files.internal("entities/Warhog/Idle_E.atlas"));
        TextureAtlas idleAtlasS = new TextureAtlas(Gdx.files.internal("entities/Warhog/Idle_S.atlas"));
        TextureAtlas idleAtlasW = new TextureAtlas(Gdx.files.internal("entities/Warhog/Idle_W.atlas"));

        TextureAtlas attackAtlasN = new TextureAtlas(Gdx.files.internal("entities/Warhog/Attack_N.atlas"));
        TextureAtlas attackAtlasS = new TextureAtlas(Gdx.files.internal("entities/Warhog/Attack_S.atlas"));
        TextureAtlas attackAtlasE = new TextureAtlas(Gdx.files.internal("entities/Warhog/Attack_E.atlas"));
        TextureAtlas attackAtlasW = new TextureAtlas(Gdx.files.internal("entities/Warhog/Attack_W.atlas"));

        TextureAtlas walkAtlasN = new TextureAtlas(Gdx.files.internal("entities/Warhog/HogNorth.atlas"));
        TextureAtlas walkAtlasS = new TextureAtlas(Gdx.files.internal("entities/Warhog/HogSouth.atlas"));
        TextureAtlas walkAtlasE = new TextureAtlas(Gdx.files.internal("entities/Warhog/HogEast.atlas"));
        TextureAtlas walkAtlasW = new TextureAtlas(Gdx.files.internal("entities/Warhog/HogWest.atlas"));

        setTextureAtlas(walkAtlasN, walkAtlasS, walkAtlasE, walkAtlasW, attackAtlasN, attackAtlasS, attackAtlasE, attackAtlasW, idleAtlasN, idleAtlasS, idleAtlasE, idleAtlasW);
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