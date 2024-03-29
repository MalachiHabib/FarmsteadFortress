package com.farmsteadfortress.entities.plants.types;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.entities.plants.Plant;
import com.farmsteadfortress.render.Tile;

public class FernPlant extends Plant {
    private TextureAtlas atlas;
    private int reward;
    private TextureRegion highlightedTexture;
    private String fernType;

    public FernPlant(float growTime, Vector2 position, int health, int reward, Tile tile, TextureAtlas atlas) {
        super((float) (growTime + Math.random() * 5), position, health, 0, 0, 0, tile, "Fern");
        this.atlas = atlas;
        this.reward = reward;

        switch(tile.getTileType()) {
            case CLASSIC_GRASS:
                fernType = "fern_one";
                break;
            case MEADOW_GRASS:
                fernType = "fern_two";
                break;
            case EVERGREEN_GRASS:
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
    public void upgrade() {

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

    public int harvest(Player player, Tile targetTile) {
        if (currentStage == GrowthStage.ADULT) {
            player.addMoney(1);
            growthTimer = 0;
            targetTile.setPlant(null);
            return reward;
        }
        return 0;
    }
}