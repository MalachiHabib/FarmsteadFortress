package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.render.Tile;

public class PlantFactory {
    public static TomatoPlant createTomatoPlant(Tile tile) {
        Texture texture = new Texture("objects/tomato_full_grown.png");
        float growTime = 5.0f;
        int health = 100;
        int damage = 10;
        float attackSpeed = 1.0f;
        float attackRange = 5.0f;
        int cost = 50;
        Vector2 position = new Vector2(tile.worldPos.x, tile.worldPos.y);
        return new TomatoPlant(texture, growTime, position, health, damage, attackSpeed, attackRange, cost, tile);
    }

    // In the future, you could add more methods here to create other types of plants.
}