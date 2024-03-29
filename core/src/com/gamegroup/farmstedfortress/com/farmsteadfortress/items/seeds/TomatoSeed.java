package com.farmsteadfortress.items.seeds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.farmsteadfortress.entities.plants.Plant;

public class TomatoSeed extends Seed {
    public TomatoSeed() {
        super(ItemType.SEED, "Tomato Seed", 5, new Texture(Gdx.files.internal("gui/shop-tomato.png")),  new Texture(Gdx.files.internal("gui/hotbar-tomato.png")), 10, Plant.PlantType.TOMATO);
    }
}