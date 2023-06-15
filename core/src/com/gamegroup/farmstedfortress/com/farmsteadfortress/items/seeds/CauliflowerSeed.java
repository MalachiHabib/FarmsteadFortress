package com.farmsteadfortress.items.seeds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.farmsteadfortress.entities.plants.Plant;

public class CauliflowerSeed extends Seed {
    public CauliflowerSeed() {
        super(ItemType.SEED, "Cauliflower Seed", 5, new Texture(Gdx.files.internal("gui/cauliflower-shop.png")), new Texture(Gdx.files.internal("gui/hotbar-cauliflower.png")), 10, Plant.PlantType.CAULIFLOWER);
    }
}