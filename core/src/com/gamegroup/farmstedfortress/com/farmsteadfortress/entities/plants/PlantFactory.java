package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.entities.plants.types.CauliflowerFlower;
import com.farmsteadfortress.entities.plants.types.FernPlant;
import com.farmsteadfortress.entities.plants.types.TomatoPlant;
import com.farmsteadfortress.projectiles.ProjectileManager;
import com.farmsteadfortress.render.Tile;

public class PlantFactory {
    private static TextureAtlas tomatoAtlas = new TextureAtlas(Gdx.files.internal("objects/tomato/TomatoAtlas.atlas"));
    private static TextureAtlas tomatoProjectileAtlas = new TextureAtlas(Gdx.files.internal("objects/tomato/Tomato_projectile.atlas"));
    private static TextureAtlas fernAtlas = new TextureAtlas(Gdx.files.internal("objects/fern/FernAtlas.atlas"));
    private static TextureAtlas cauliflowerAtlas = new TextureAtlas(Gdx.files.internal("objects/cauliflower/CauliflowerAtlas.atlas"));
    private static Player player;

    public static Plant createPlant(Plant.PlantType plantType, Tile tile, Player player, ProjectileManager projectileManager) {
        switch (plantType) {
            case TOMATO:
                return createTomatoPlant(tile, projectileManager);
            case FERN:
                return createFernPlant(tile);
            case CAULIFLOWER:
                return createCauliflowerPlant(tile, player);
            default:
                return null;
        }
    }

    private static TomatoPlant createTomatoPlant(Tile tile, ProjectileManager projectileManager) {
        float growTime = 2.5f;
        int health = 100;
        int damage = 3;
        float attackSpeed = 1.0f;
        float attackRange = 600f;
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

    private static CauliflowerFlower createCauliflowerPlant(Tile tile, Player player) {
        float growTime = 5.0f;
        int health = 50;
        float timeBetweenPayouts = 3f;
        int payoutAmount = 2;
        Vector2 position = new Vector2(tile.worldPos.x, tile.worldPos.y);
        return new CauliflowerFlower(growTime, position, health, timeBetweenPayouts, payoutAmount, tile, cauliflowerAtlas, player);
    }
}