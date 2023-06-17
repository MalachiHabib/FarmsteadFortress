package com.farmsteadfortress.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.render.Tile;

import java.util.List;

/**
 * Represents a basic enemy entity in the game.
 */


public class BossEnemy extends Enemy {
    private float maxHealth;
    /**
     * Constructs a basic enemy entity.
     *
     * @param atlas          the texture atlas containing the walking animation frames
     * @param animationSpeed the speed of the walking animation
     * @param speed          the movement speed of the enemy
     * @param health         the enemy's health
     */
    public BossEnemy(Player player, TextureAtlas atlas, float animationSpeed, float speed, int health, int attackDamage, float timeBetweenAttacks, List<Enemy> enemies) {
        super(player, atlas, animationSpeed, speed, health, 50, attackDamage, timeBetweenAttacks, enemies);
        this.scale = 1.5f;
        this.maxHealth = health;
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
        if (enemies.contains(this)) {
            enemies.remove(this);
            player.addMoney(reward);
        }
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    /**
     * Renders the boss enemy on the screen.
     * Overridden from the Enemy superclass.
     *
     * @param batch the sprite batch used for rendering
     */
    @Override
    public void render(SpriteBatch batch) {
        Animation<TextureRegion> currentAnimation = getCurrentAnimation();
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        float yOffset = Tile.TILE_SIZE / 2f + 15f;
        float posX = position.x;
        float posY = position.y + yOffset;

        batch.setShader(attackShader);
        attackShader.setUniformf("u_color", EFFECT_COLOR.r, EFFECT_COLOR.g, EFFECT_COLOR.b, 1f);

        batch.begin();
        batch.draw(currentFrame, posX, posY, currentFrame.getRegionWidth() / 2, currentFrame.getRegionHeight() / 2, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), scale, scale, 0);
        batch.end();
        batch.setShader(null);
    }
}