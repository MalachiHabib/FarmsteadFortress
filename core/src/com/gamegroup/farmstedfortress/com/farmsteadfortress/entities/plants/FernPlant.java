package com.farmsteadfortress.entities.plants;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.render.Tile;

import java.util.List;

public class FernPlant extends Plant {
    private TextureAtlas atlas;
    private int reward;
    private TextureRegion highlightedTexture;

    public FernPlant(float growTime, Vector2 position, int health, int reward, Tile tile, TextureAtlas atlas) {
        super(growTime, position, health, 0, 0, 0, 0, tile);
        this.atlas = atlas;
        this.reward = reward;
        initialiseTextures();
        highlightedTexture = atlas.findRegion("grass_fern_highlight");
        currentStage = GrowthStage.ADULT;
    }

    @Override
    protected void initialiseTextures() {
        textures.put(GrowthStage.SEEDLING, atlas.findRegion("grass_fern_highlight"));
        textures.put(GrowthStage.SPROUT, atlas.findRegion("grass_fern"));
        textures.put(GrowthStage.SMALL_PLANT, atlas.findRegion("grass_fern"));
        textures.put(GrowthStage.ADULT, atlas.findRegion("grass_fern"));
    }

    @Override
    protected void attack(float delta) {
        // Fern does not attack, leave it empty
    }

    @Override
    public void update(float delta, List<Enemy> enemies) {
        growthTimer += delta;
        if (growthTimer > growTime) {
            currentStage = GrowthStage.ADULT;
            growthTimer = 0;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion currentTexture = isHighlighted ? highlightedTexture : textures.get(currentStage);
        batch.draw(currentTexture, this.position.x, this.position.y + 10f + Tile.TILE_SIZE / 2f);
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