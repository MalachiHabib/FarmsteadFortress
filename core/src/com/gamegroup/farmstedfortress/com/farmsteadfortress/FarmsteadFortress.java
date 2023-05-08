package com.farmsteadfortress;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.farmsteadfortress.screens.GameScreen;
import com.farmsteadfortress.screens.MenuScreen;

public class FarmsteadFortress extends Game {
    private SpriteBatch batch;
    public static GameScreen screen;
    public static MenuScreen menuScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        screen = new GameScreen(batch);
        menuScreen = new MenuScreen(this);
        setScreen(menuScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }
}
