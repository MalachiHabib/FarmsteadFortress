package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.render.Tile;

public class TomatoPlant extends Plant {
    public TomatoPlant(float growTime, Vector2 position, int health, int damage, float attackSpeed, float attackRange, int cost, Tile tile) {
        super(growTime, position, health, damage, attackSpeed, attackRange, cost, tile);
    }

    @Override
    protected void initializeTextures() {
        textures.put(GrowthStage.SEEDLING, new Texture("objects/tomato/tomato_seedling.png"));
        textures.put(GrowthStage.SPROUT, new Texture("objects/tomato/tomato_sprout.png"));
        textures.put(GrowthStage.SMALL_PLANT, new Texture("objects/tomato/tomato_small_plant.png"));
        textures.put(GrowthStage.ADULT, new Texture("objects/tomato/tomato_full_grown.png"));
    }

    @Override
    protected void attack(float delta) {
        System.out.println("Shooting a tomato!");
    }
}