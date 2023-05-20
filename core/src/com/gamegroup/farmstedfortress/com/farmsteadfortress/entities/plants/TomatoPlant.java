package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.render.Tile;

public class TomatoPlant extends Plant {

    private TextureAtlas atlas;
    public TomatoPlant(float growTime, Vector2 position, int health, int damage, float attackSpeed, float attackRange, int cost, Tile tile, TextureAtlas atlas) {
        super(growTime, position, health, damage, attackSpeed, attackRange, cost, tile);
        this.atlas = atlas;
        initialiseTextures();
    }

    @Override
    protected void initialiseTextures() {
        textures.put(GrowthStage.SEEDLING, atlas.findRegion("tomato_seedling"));
        textures.put(GrowthStage.SPROUT, atlas.findRegion("tomato_sprout"));
        textures.put(GrowthStage.SMALL_PLANT, atlas.findRegion("tomato_small_plant"));
        textures.put(GrowthStage.ADULT, atlas.findRegion("tomato_full_grown"));
    }

    @Override
    protected void attack(float delta) {
        System.out.println("Shooting a tomato!");
    }
}