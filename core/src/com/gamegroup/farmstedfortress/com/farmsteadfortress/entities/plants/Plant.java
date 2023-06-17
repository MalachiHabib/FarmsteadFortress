package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
    public enum GrowthStage {
        SEEDLING,
        SPROUT,
        SMALL_PLANT,
        UPGRADE,
        ADULT
    }

    public enum PlantType {
        TOMATO,
        CAULIFLOWER,
        PUMPKIN,
        CORN,
        FERN
    }

    protected Texture texture;
    protected float growTime;
    protected Vector2 position;
    protected int health;
    protected int damage;
    protected float attackSpeed;
    protected float attackRange;
    protected Tile tile;
    protected ObjectMap<GrowthStage, TextureRegion> textures;
    protected GrowthStage currentStage;
    protected float growthTimer;
    protected boolean isHighlighted = false;
    protected String name;
    protected Sound upgradeSound;
    protected Sound maxUpgrade;
    protected Sound shootSound;
    private boolean maxUpgradeSoundPlayed = false;

    public Plant(float growTime, Vector2 position, int health, int damage, float attackSpeed, float attackRange, Tile tile, String plantName) {
        this.growTime = growTime;
        this.position = position;
        this.health = health;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
        this.tile = tile;
        this.textures = new ObjectMap<>();
        this.currentStage = GrowthStage.SEEDLING;
        this.growthTimer = 0;
        this.name = plantName;
    }

    public void update(float delta, List<Enemy> enemies) {
        growthTimer += delta;
        if (growthTimer > growTime) {
            nextGrowthStage();
            growthTimer = 0;
        }

        if (currentStage == GrowthStage.ADULT || currentStage == GrowthStage.UPGRADE) {
            for (Enemy enemy : enemies) {
                if (position.dst(enemy.getPosition()) <= attackRange) {
                    attack(delta, enemy);
                    break;
                }
            }
        }
    }

    private void nextGrowthStage() {
        if (upgradeSound != null) {
            switch (currentStage) {
                case SEEDLING:
                    currentStage = GrowthStage.SPROUT;
                    upgradeSound.play(0.2f);
                    break;
                case SPROUT:
                    currentStage = GrowthStage.SMALL_PLANT;
                    upgradeSound.play(0.2f);
                    break;
                case SMALL_PLANT:
                    currentStage = GrowthStage.ADULT;
                    upgradeSound.play(0.2f);
                    break;
                case ADULT:
                case UPGRADE:
                    if (!maxUpgradeSoundPlayed) {
                        maxUpgrade.play(0.2f);
                        maxUpgradeSoundPlayed = true;
                    }
                    break;
            }
        }
    }

    public void setHighLight(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public void draw(SpriteBatch batch) {
        if (currentStage != null) {
            TextureRegion currentTexture = textures.get(currentStage);
            if (currentTexture != null) {
                if (isHighlighted) {
                    batch.setColor(Color.GRAY);
                }
                batch.draw(currentTexture, this.position.x, this.position.y + Tile.TILE_SIZE / 2f);
                batch.setColor(Color.WHITE);
            }
        }
    }

    public String getName() {
        return name;
    }

    public abstract void upgrade();

    protected abstract void initialiseTextures();

    protected abstract void attack(float delta, Enemy enemy);

    public void dispose() {
        upgradeSound.dispose();
    }
}