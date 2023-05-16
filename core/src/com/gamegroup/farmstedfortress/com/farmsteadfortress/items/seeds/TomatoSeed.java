package com.farmsteadfortress.items.seeds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TomatoSeed extends Seed {
    public TomatoSeed() {
        super(ItemType.SEED, "Tomato Seed", 5, new Texture(Gdx.files.internal("gui/hotbar-tomato.png")), 10);
    }
}