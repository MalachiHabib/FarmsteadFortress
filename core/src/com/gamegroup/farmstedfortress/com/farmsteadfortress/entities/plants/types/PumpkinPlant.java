package com.farmsteadfortress.entities.plants.types;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.entities.plants.Plant;
import com.farmsteadfortress.projectiles.Projectile;
import com.farmsteadfortress.projectiles.ProjectileManager;
import com.farmsteadfortress.render.Tile;

public class PumpkinPlant extends Plant {
    private TextureAtlas atlas;
    private float timeBetweenAttacks;
    private float timeSinceLastAttack = 0;
    private int attackDamage;
    private ProjectileManager projectileManager;
    private TextureRegion projectileTexture, projectileHitTexture;

    public PumpkinPlant(float growTime, Vector2 position, int health, int attackDamage, float attackSpeed,
                        float attackRange, float timeBetweenAttacks, Tile tile, TextureAtlas atlas, TextureAtlas projectileAtlas, ProjectileManager projectileManager, Sound upgradeSound, Sound shootSound, Sound maxUpgrade) {
        super(growTime, position, health, attackDamage, attackSpeed, attackRange, tile, "Pumpkin");
        this.atlas = atlas;
        this.timeBetweenAttacks = timeBetweenAttacks;
        this.attackDamage = attackDamage;
        this.projectileManager = projectileManager;
        this.projectileTexture = projectileAtlas.findRegion("projectile");
        this.projectileHitTexture = projectileAtlas.findRegion("splatter");
        this.upgradeSound = upgradeSound;
        this.maxUpgrade = maxUpgrade;
        this.shootSound = shootSound;
        initialiseTextures();
    }

    @Override
    public void upgrade() {
        attackDamage += 10;
        timeBetweenAttacks -= 2;
        attackRange *= 1.5f;
        currentStage = GrowthStage.UPGRADE;
    }

    @Override
    protected void initialiseTextures() {
        textures.put(GrowthStage.SEEDLING, atlas.findRegion("sprout"));
        textures.put(GrowthStage.SPROUT, atlas.findRegion("small_pumpkin"));
        textures.put(GrowthStage.SMALL_PLANT, atlas.findRegion("medium_pumpkin"));
        textures.put(GrowthStage.ADULT, atlas.findRegion("large_pumpkin"));
        textures.put(GrowthStage.UPGRADE, atlas.findRegion("upgrade_pumpkin"));
    }

    @Override
    protected void attack(float delta, Enemy enemy) {
        timeSinceLastAttack += delta;
        if (timeSinceLastAttack >= timeBetweenAttacks && !enemy.isDead()) {
            Vector2 direction = new Vector2(enemy.getPosition().x - position.x, enemy.getPosition().y - position.y);
            Vector2[] projectilePositions = {
                    new Vector2(this.position.x + Tile.TILE_SIZE / 2f, this.position.y +  2 * Tile.TILE_SIZE / 3),
                    new Vector2(this.position.x + Tile.TILE_SIZE / 2f + 5, this.position.y + 2 * Tile.TILE_SIZE / 3 + 5),
                    new Vector2(this.position.x + Tile.TILE_SIZE / 2f + 10, this.position.y + 2 * Tile.TILE_SIZE / 3 + 10)
            };
            for (Vector2 projectilePosition : projectilePositions) {
                shootSound.play();
                projectileManager.addProjectile(new Projectile(projectilePosition, direction, projectileTexture, projectileHitTexture, attackDamage, 200f, enemy));
                timeSinceLastAttack = 0;
            }
        }
    }
}