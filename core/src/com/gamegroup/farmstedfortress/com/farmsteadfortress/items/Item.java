package com.farmsteadfortress.items;

import com.badlogic.gdx.graphics.Texture;

public abstract class Item {
    public enum ItemType {
        SEED
    }

    private ItemType itemType;
    private String name;
    private int price;
    private Texture texture;

    public Item(ItemType itemType, String name, int price, Texture texture) {
        this.itemType = itemType;
        this.name = name;
        this.price = price;
        this.texture = texture;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}