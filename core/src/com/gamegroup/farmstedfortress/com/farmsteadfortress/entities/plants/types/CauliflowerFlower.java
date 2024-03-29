package com.farmsteadfortress.entities.plants.types;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.entities.plants.Plant;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.waves.WaveController;

import java.util.List;

public class CauliflowerFlower extends Plant {
    private TextureAtlas atlas;
    private float timeBetweenPayouts;
    private float timeSinceLastPayout = 0;
    private int payoutAmount;
    private Player player;

    public CauliflowerFlower(float growTime, Vector2 position, int health, float timeBetweenPayouts,
                             int payoutAmount, Tile tile, TextureAtlas atlas, Player player, Sound upgradeSound, Sound shootSound, Sound maxUpgrade) {
        super(growTime, position, health, 0, 0, 0, tile, "Cauliflower");
        this.atlas = atlas;
        this.timeBetweenPayouts = timeBetweenPayouts;
        this.payoutAmount = payoutAmount;
        this.player = player;
        this.upgradeSound = upgradeSound;
        this.maxUpgrade = maxUpgrade;
        this.shootSound = shootSound;
        initialiseTextures();
    }

    @Override
    protected void initialiseTextures() {
        textures.put(GrowthStage.SEEDLING, atlas.findRegion("cauliflower_seedling"));
        textures.put(GrowthStage.SPROUT, atlas.findRegion("cauliflower_sprout"));
        textures.put(GrowthStage.SMALL_PLANT, atlas.findRegion("cauliflower_small_plant"));
        textures.put(GrowthStage.ADULT, atlas.findRegion("cauliflower_large_plant"));
        textures.put(GrowthStage.UPGRADE, atlas.findRegion("cauliflower_upgrade_plant"));
    }

    @Override
    protected void attack(float delta, Enemy enemy) {
        // Does not attack
    }

    @Override
    public void update(float delta, List<Enemy> enemies) {
        System.out.println(currentStage);
        super.update(delta, enemies);
        generateIncome(delta);
    }

    @Override
    public void upgrade() {
        timeBetweenPayouts /= 2;
        payoutAmount += 5;
        currentStage = GrowthStage.UPGRADE;
    }

    private void generateIncome(float delta) {
        timeSinceLastPayout += delta;
        if (currentStage == GrowthStage.ADULT) {
            if (timeSinceLastPayout >= timeBetweenPayouts) {
                if (WaveController.waveStarted) {
                    player.addMoney(payoutAmount);
                    timeSinceLastPayout = 0;
                }
            }
        }
    }
}