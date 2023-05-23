package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.render.Tile;

public class PlantFactory {
    private static TextureAtlas tomatoAtlas = new TextureAtlas(Gdx.files.internal("objects/tomato/TomatoAtlas.atlas"));
    private static TextureAtlas fernAtlas = new TextureAtlas(Gdx.files.internal("objects/fern/FernAtlas.atlas"));

    public static Plant createPlant(Plant.PlantType plantType, Tile tile) {
        switch (plantType) {
            case TOMATO:
                return createTomatoPlant(tile);
            case FERN:
                return createFernPlant(tile);
            default:
                return null;
        }
    }

    private static TomatoPlant createTomatoPlant(Tile tile) {
        float growTime = 2.5f;
        int health = 100;
        int damage = 10;
        float attackSpeed = 1.0f;
        float attackRange = 250f;
        float timeBetweenAttacks = 3f;
        int cost = 50;
        Vector2 position = new Vector2(tile.worldPos.x, tile.worldPos.y);
        return new TomatoPlant(growTime, position, health, damage, attackSpeed, attackRange, timeBetweenAttacks, cost, tile, tomatoAtlas);
    }

    private static FernPlant createFernPlant(Tile tile) {
        float growTime = 5.0f;
        int health = 50;
        int reward = 10;
        Vector2 position = new Vector2(tile.worldPos.x, tile.worldPos.y);
        return new FernPlant(growTime, position, health, reward, tile, fernAtlas);
    }

}