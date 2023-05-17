package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.farmsteadfortress.render.Tile;

public abstract class Plant {
    protected Texture texture;
    protected float growTime;
    protected Vector2 position;
    protected int health;
    protected int damage;
    protected float attackSpeed;
    protected float attackRange;
    protected int cost;
    protected Tile tile;
    protected ObjectMap<GrowthStage, Texture> textures;
    protected GrowthStage currentStage;
    protected float growthTimer;
    public Plant(float growTime, Vector2 position, int health, int damage, float attackSpeed, float attackRange, int cost, Tile tile) {
        this.growTime = growTime;
        this.position = position;
        this.health = health;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
        this.cost = cost;
        this.tile = tile;
        this.textures = new ObjectMap<>();
        this.currentStage = GrowthStage.SEEDLING;
        this.growthTimer = 0;
        initializeTextures();
    }

    public void update(float delta) {
        growthTimer += delta;
        if (growthTimer > growTime) {
            nextGrowthStage();
            growthTimer = 0;
        }
    }

    private void nextGrowthStage() {
        switch (currentStage) {
            case SEEDLING:
                currentStage = GrowthStage.SPROUT;
                break;
            case SPROUT:
                currentStage = GrowthStage.SMALL_PLANT;
                break;
            case SMALL_PLANT:
                currentStage = GrowthStage.ADULT;
                break;
        }
    }

    public void draw(SpriteBatch batch) {
        Texture currentTexture = textures.get(currentStage);
        batch.draw(currentTexture, this.position.x, this.position.y + Tile.TILE_SIZE / 2f);
    }

    protected abstract void initializeTextures();

    protected abstract void attack(float delta);

    public enum GrowthStage {
        SEEDLING,
        SPROUT,
        SMALL_PLANT,
        ADULT;
    }

    public enum PlantType {
        TOMATO
    }
}