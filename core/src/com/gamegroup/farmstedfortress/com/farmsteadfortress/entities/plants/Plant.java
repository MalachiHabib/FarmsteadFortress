package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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

    public Plant(Texture texture, float growTime, Vector2 position, int health, int damage, float attackSpeed, float attackRange, int cost, Tile tile) {
        this.texture = texture;
        this.growTime = growTime;
        this.position = position;
        this.health = health;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
        this.cost = cost;
        this.tile = tile;
    }

    public void update(float delta) {
        // Here you can implement the plant's logic
    }

    public void draw(SpriteBatch batch) {
        batch.draw(this.texture, this.position.x, this.position.y + Tile.TILE_SIZE / 2f);
    }

    protected abstract void attack(float delta);
}