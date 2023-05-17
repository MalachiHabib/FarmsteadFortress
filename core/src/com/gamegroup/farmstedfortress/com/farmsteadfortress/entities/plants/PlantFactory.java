package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.render.Tile;

public class PlantFactory {
    public static Plant createPlant(Plant.PlantType plantType, Tile tile) {
        switch (plantType) {
            case TOMATO:
                return createTomatoPlant(tile);
            default:
                return null;
        }
    }
    public static TomatoPlant createTomatoPlant(Tile tile) {
        float growTime = 5.0f;
        int health = 100;
        int damage = 10;
        float attackSpeed = 1.0f;
        float attackRange = 5.0f;
        int cost = 50;
        Vector2 position = new Vector2(tile.worldPos.x, tile.worldPos.y);
        return new TomatoPlant(growTime, position, health, damage, attackSpeed, attackRange, cost, tile);
    }
}