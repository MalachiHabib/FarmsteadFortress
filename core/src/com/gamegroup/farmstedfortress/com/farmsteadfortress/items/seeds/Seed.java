package com.farmsteadfortress.items.seeds;

import com.badlogic.gdx.graphics.Texture;
import com.farmsteadfortress.entities.plants.Plant;
import com.farmsteadfortress.items.Item;

public abstract class Seed extends Item {
    private int growthTime;
    private Plant.PlantType plantType;

    public Seed(ItemType itemType, String name, int price, Texture texture, int growthTime, Plant.PlantType plantType) {
        super(itemType, name, price, texture);
        this.growthTime = growthTime;
        this.plantType = plantType;
    }

    public Plant.PlantType getPlantType() {
        return plantType;
    }

    public int getGrowthTime() {
        return growthTime;
    }

    public void setGrowthTime(int growthTime) {
        this.growthTime = growthTime;
    }
}