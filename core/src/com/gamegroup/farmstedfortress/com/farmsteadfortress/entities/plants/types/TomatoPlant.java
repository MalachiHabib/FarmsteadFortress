package com.farmsteadfortress.entities.plants.types;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.entities.plants.Plant;
import com.farmsteadfortress.projectiles.Projectile;
import com.farmsteadfortress.projectiles.ProjectileManager;
import com.farmsteadfortress.render.Tile;

public class TomatoPlant extends Plant {
    private TextureAtlas atlas;
    private float timeBetweenAttacks;
    private float timeSinceLastAttack = 0;
    private int attackDamage;
    private ProjectileManager projectileManager;
    private TextureRegion projectileTexture, projectileHitTexture;

    public TomatoPlant(float growTime, Vector2 position, int health, int attackDamage, float attackSpeed,
                       float attackRange, float timeBetweenAttacks, Tile tile, TextureAtlas atlas, TextureAtlas projectileAtlas, ProjectileManager projectileManager) {
        super(growTime, position, health, attackDamage, attackSpeed, attackRange, tile, "Tomato");
        this.atlas = atlas;
        this.timeBetweenAttacks = timeBetweenAttacks;
        this.attackDamage = attackDamage;
        this.projectileManager = projectileManager;
        this.projectileTexture = projectileAtlas.findRegion("projectile");
        this.projectileHitTexture = projectileAtlas.findRegion("splatter");
        initialiseTextures();
    }

    @Override
    public void upgrade() {
        attackDamage += 5;
        timeBetweenAttacks -= 3;
        attackRange *= 2;
        currentStage = GrowthStage.UPGRADE;
    }

    @Override
    protected void initialiseTextures() {
        textures.put(GrowthStage.SEEDLING, atlas.findRegion("sprout"));
        textures.put(GrowthStage.SPROUT, atlas.findRegion("tomato_small"));
        textures.put(GrowthStage.SMALL_PLANT, atlas.findRegion("tomato_mid"));
        textures.put(GrowthStage.ADULT, atlas.findRegion("tomato_full"));
        textures.put(GrowthStage.UPGRADE, atlas.findRegion("tomato_upgraded"));
    }

    @Override
    protected void attack(float delta, Enemy enemy) {
        timeSinceLastAttack += delta;
        if (timeSinceLastAttack >= timeBetweenAttacks && !enemy.isDead()) {
            Vector2 direction = new Vector2(enemy.getPosition().x - position.x, enemy.getPosition().y - position.y);
            Vector2 projectilePosition = new Vector2(this.position.x + Tile.TILE_SIZE / 2f, this.position.y + 10f + Tile.TILE_SIZE);
            projectileManager.addProjectile(new Projectile(projectilePosition, direction, projectileTexture, projectileHitTexture, attackDamage, 200f, enemy));
            timeSinceLastAttack = 0;
        }
    }
}