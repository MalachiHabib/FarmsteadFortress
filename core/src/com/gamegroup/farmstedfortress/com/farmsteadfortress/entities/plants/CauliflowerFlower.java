package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.entities.enemies.Enemy;
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
                             int payoutAmount, Tile tile, TextureAtlas atlas, Player player) {
        super(growTime, position, health, 0, 0, 0, tile);
        this.atlas = atlas;
        this.timeBetweenPayouts = timeBetweenPayouts;
        this.payoutAmount = payoutAmount;
        this.player = player;
        initialiseTextures();
    }

    @Override
    protected void initialiseTextures() {
        textures.put(GrowthStage.SEEDLING, atlas.findRegion("cauliflower_seedling"));
        textures.put(GrowthStage.SPROUT, atlas.findRegion("cauliflower_sprout"));
        textures.put(GrowthStage.SMALL_PLANT, atlas.findRegion("cauliflower_small_plant"));
        textures.put(GrowthStage.ADULT, atlas.findRegion("cauliflower_full_grown"));
    }

    @Override
    protected void attack(float delta, Enemy enemy) {
        // Does not attack
    }

    @Override
    public void update(float delta, List<Enemy> enemies) {
        super.update(delta, enemies);
        generateIncome(delta);
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