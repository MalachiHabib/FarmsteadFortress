package com.farmsteadfortress.items.seeds;

import com.badlogic.gdx.graphics.Texture;
import com.farmsteadfortress.entities.plants.Plant;
import com.farmsteadfortress.items.Item;

import java.util.Arrays;
import java.util.List;

public abstract class Seed extends Item {
    private int growthTime;
    private Plant.PlantType plantType;

    public Seed(ItemType itemType, String name, int price, Texture seedTexture, Texture hotbarTexture, int growthTime, Plant.PlantType plantType) {
        super(itemType, name, price, seedTexture, hotbarTexture);
        this.growthTime = growthTime;
        this.plantType = plantType;
    }

    public static List<Seed> getSeedTypes() {
        return Arrays.asList(new TomatoSeed(), new CauliflowerSeed(), new PumpkinSeed());
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