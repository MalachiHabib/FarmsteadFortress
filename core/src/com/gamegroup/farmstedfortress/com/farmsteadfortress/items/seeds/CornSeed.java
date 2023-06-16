package com.farmsteadfortress.items.seeds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.farmsteadfortress.entities.plants.Plant;

public class CornSeed extends Seed {
    public CornSeed() {
        super(ItemType.SEED, "Corn Seed", 8, new Texture(Gdx.files.internal("gui/corn-shop.png")), new Texture(Gdx.files.internal("gui/hotbar-corn.png")), 10, Plant.PlantType.CORN);
    }
}
