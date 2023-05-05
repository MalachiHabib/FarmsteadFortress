package com.gamegroup.farmstedfortress.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import java.io.IOException;
import java.util.LinkedList;

public class TileMap {

    public LinkedList<Tile> base;
    public LinkedList<Tile> objects;
    private Texture water;
    private Texture sand;

    public TileMap() {
        water = new Texture("water");
        sand = new Texture("sand");
        base = new LinkedList<>();
        objects = new LinkedList<>();

        try {
            fillMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(SpriteBatch batch) {
        for (Tile tile : base) {
            tile.render(batch);
        }
        for (Tile tile : objects) {
            tile.render(batch);
        }
    }

    public void fillMap() throws IOException {

    }
}
