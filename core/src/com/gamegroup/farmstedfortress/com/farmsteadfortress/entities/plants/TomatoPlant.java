package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.render.Tile;

public class TomatoPlant extends Plant {
    public TomatoPlant(Texture texture, float growTime, Vector2 position, int health, int damage, float attackSpeed, float attackRange, int cost, Tile tile) {
        super(texture, growTime, position, health, damage, attackSpeed, attackRange, cost, tile);
    }

    @Override
    protected void attack(float delta) {
        System.out.println("Shooting a tomato!");
    }
}