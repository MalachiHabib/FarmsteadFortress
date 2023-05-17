package com.farmsteadfortress.inventory;

import com.badlogic.gdx.utils.Array;
import com.farmsteadfortress.items.Item;

public class Inventory {
    private Array<Item> items;

    public Inventory() {
        items = new Array<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.removeValue(item, true);
    }

    public Array<Item> getItems() {
        return items;
    }

    public void printInventory() {
        System.out.println("Inventory contents:");
        for (Item item : items) {
            System.out.println(item.getName());
        }
    }

    public boolean contains(Item selectedItem) {
        return items.contains(selectedItem, true);
    }
}