package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.projectiles.ProjectileManager;
import com.farmsteadfortress.render.Tile;

public class PlantFactory {
    private static TextureAtlas tomatoAtlas = new TextureAtlas(Gdx.files.internal("objects/tomato/TomatoAtlas.atlas"));
    private static TextureAtlas tomatoProjectileAtlas = new TextureAtlas(Gdx.files.internal("objects/tomato/Tomato_projectile.atlas"));

    private static TextureAtlas fernAtlas = new TextureAtlas(Gdx.files.internal("objects/fern/FernAtlas.atlas"));
    private static TextureAtlas sunflowerAtlas = new TextureAtlas(Gdx.files.internal("objects/cauliflower/CauliflowerAtlas.atlas"));
    private static Player player;
    public static Plant createPlant(Plant.PlantType plantType, Tile tile, Player player, ProjectileManager projectileManager) {
        switch (plantType) {
            case TOMATO:
                return createTomatoPlant(tile, projectileManager);
            case FERN:
                return createFernPlant(tile);
            case SUNFLOWER:
                if (player == null) {
                    throw new IllegalArgumentException("Player instance is required to create a SunflowerPlant");
                }
                return createSunflowerPlant(tile, player);
            default:
                return null;
        }
    }

    private static TomatoPlant createTomatoPlant(Tile tile, ProjectileManager projectileManager) {
        float growTime = 2.5f;
        int health = 100;
        int damage = 10;
        float attackSpeed = 1.0f;
        float attackRange = 20000f;
        float timeBetweenAttacks = 3f;
        Vector2 position = new Vector2(tile.worldPos.x, tile.worldPos.y);
        return new TomatoPlant(growTime, position, health, damage, attackSpeed, attackRange, timeBetweenAttacks, tile, tomatoAtlas, tomatoProjectileAtlas, projectileManager);
    }

    private static FernPlant createFernPlant(Tile tile) {
        float growTime = 5.0f;
        int health = 50;
        int reward = 10;
        Vector2 position = new Vector2(tile.worldPos.x, tile.worldPos.y);
        return new FernPlant(growTime, position, health, reward, tile, fernAtlas);
    }

    private static CauliflowerFlower createSunflowerPlant(Tile tile, Player player) {
        float growTime = 5.0f;
        int health = 50;
        float timeBetweenPayouts = 3f;
        int payoutAmount = 10;
        Vector2 position = new Vector2(tile.worldPos.x, tile.worldPos.y);
        return new CauliflowerFlower(growTime, position, health, timeBetweenPayouts, payoutAmount, tile, sunflowerAtlas, player);
    }
}