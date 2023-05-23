package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.render.Tile;

import java.util.List;

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
    protected ObjectMap<GrowthStage, TextureRegion> textures;
    protected GrowthStage currentStage;
    protected float growthTimer;
    protected boolean isHighlighted = false;

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
    }

    public void update(float delta, List<Enemy> enemies) {
        growthTimer += delta;
        if (growthTimer > growTime) {
            nextGrowthStage();
            growthTimer = 0;
        }

        if (currentStage == GrowthStage.ADULT) {
            for (Enemy enemy : enemies) {
                if (position.dst(enemy.getPosition()) <= attackRange) {
                    attack(delta, enemy);
                    break;
                }
            }
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

    public void setHighLight(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentTexture = textures.get(currentStage);
        if (isHighlighted) {
            batch.setColor(Color.BLUE);
        }
        batch.draw(currentTexture, this.position.x, this.position.y + 10f + Tile.TILE_SIZE / 2f);
        batch.setColor(Color.WHITE);
    }


    protected abstract void initialiseTextures();

    protected abstract void attack(float delta, Enemy enemy);

    public enum GrowthStage {
        SEEDLING,
        SPROUT,
        SMALL_PLANT,
        ADULT;
    }

    public enum PlantType {
        TOMATO,
        FERN
    }
}