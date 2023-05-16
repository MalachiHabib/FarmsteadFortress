package com.farmsteadfortress.items.seeds;

import com.badlogic.gdx.graphics.Texture;
import com.farmsteadfortress.items.Item;

public abstract class Seed extends Item {
    private int growthTime;

    public Seed(ItemType itemType, String name, int price, Texture texture, int growthTime) {
        super(itemType, name, price, texture);
        this.growthTime = growthTime;
    }

    public int getGrowthTime() {
        return growthTime;
    }

    public void setGrowthTime(int growthTime) {
        this.growthTime = growthTime;
    }
}