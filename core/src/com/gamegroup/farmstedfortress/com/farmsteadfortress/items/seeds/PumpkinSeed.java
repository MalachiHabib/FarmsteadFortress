package com.farmsteadfortress.items.seeds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.farmsteadfortress.entities.plants.Plant;

public class PumpkinSeed extends Seed {
    public PumpkinSeed() {
        super(ItemType.SEED, "Pumpkin Seed", 7, new Texture(Gdx.files.internal("gui/shop-pumpkin.png")), new Texture(Gdx.files.internal("gui/hotbar-pumpkin.png")), 15, Plant.PlantType.PUMPKIN);
    }
}
