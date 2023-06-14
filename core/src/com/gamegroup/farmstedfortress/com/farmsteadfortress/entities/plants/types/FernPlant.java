package com.farmsteadfortress.entities.plants.types;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.entities.plants.Plant;
import com.farmsteadfortress.render.Tile;

import java.util.List;

public class FernPlant extends Plant {
    private TextureAtlas atlas;
    private int reward;
    private TextureRegion highlightedTexture;
    private String fernType;

    public FernPlant(float growTime, Vector2 position, int health, int reward, Tile tile, TextureAtlas atlas) {
        super((float) (growTime + Math.random() * 5), position, health, 0, 0, 0, tile);
        this.atlas = atlas;
        this.reward = reward;

        switch(tile.getTileType()) {
            case GRASS:
                fernType = "fern_one";
                break;
            case GRASS_YELLOW:
                fernType = "fern_two";
                break;
            case GRASS_BLUE:
                fernType = "fern_three";
                break;
            default:
                break;
        }

        initialiseTextures();
        highlightedTexture = atlas.findRegion(fernType + "_highlight");

        currentStage = GrowthStage.SEEDLING;
    }

    @Override
    protected void initialiseTextures() {
        TextureRegion fernTexture = atlas.findRegion(fernType);
        textures.put(GrowthStage.SEEDLING, atlas.findRegion(fernType + "_small"));
        textures.put(GrowthStage.SPROUT, atlas.findRegion(fernType + "_small"));
        textures.put(GrowthStage.SMALL_PLANT, fernTexture);
        textures.put(GrowthStage.ADULT, fernTexture);
    }

    @Override
    protected void attack(float delta, Enemy enemy) {
        // Fern does not attack :)
    }

    public int harvest(Player player) {
        if (currentStage == GrowthStage.ADULT) {
            System.out.println(player.getMoney());
            player.addMoney(1);
            currentStage = GrowthStage.SEEDLING;
            growthTimer = 0;
            return reward;
        }
        return 0;
    }
}