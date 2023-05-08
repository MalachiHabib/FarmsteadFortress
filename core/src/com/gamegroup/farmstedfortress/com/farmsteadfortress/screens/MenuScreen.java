package com.farmsteadfortress.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.farmsteadfortress.FarmsteadFortress;

public class MenuScreen implements Screen {
    FarmsteadFortress game;
    SpriteBatch menuBatch;
    Skin skin;
    Stage stage;
    BitmapFont bmfont;

    public MenuScreen(FarmsteadFortress game) {
        this.game = game;
    }

    public void create() {
        menuBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        stage = new Stage();

        final TextButton startButton = new TextButton("Start", skin, "default");
        startButton.setWidth(300f);
        startButton.setHeight(200f);
        final TextButton quitButton = new TextButton("Quit", skin, "default");
        quitButton.setWidth(300f);
        quitButton.setHeight(200f);

        // maybe realign buttons more toward the center; seem to be very far apart
        // need to increase the size of the text on the buttons

        int half = Gdx.graphics.getWidth() / 2;
        int quarter = (half) / 2;
        startButton.setPosition(quarter - 150f, Gdx.graphics.getHeight() / 2 - 100f);
        quitButton.setPosition(half + quarter - 150f, Gdx.graphics.getHeight() / 2 - 100f);
        stage.addActor(startButton);
        stage.addActor(quitButton);
        Gdx.input.setInputProcessor(stage);

        /*
        BitmapFont sourced from
        https://www.dafont.com/lilian-2.font?back=bitmap
        follow link for more details
         */
        bmfont = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );
        bmfont.getData().setScale(4, 4);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(FarmsteadFortress.screen);
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    /**
     *
     */
    @Override
    public void show() {
        create();
    }

    /**
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        menuBatch.begin();
        stage.draw();
        bmfont.setColor(0, 0, 0, 1);
        // could either stick with black or change at a later date
        bmfont.draw(menuBatch, "Farmstead Fortress", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 5 / 6, 0f, 1, false);
        menuBatch.end();
    }

    /**
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     *
     */
    @Override
    public void pause() {

    }

    /**
     *
     */
    @Override
    public void resume() {

    }

    /**
     *
     */
    @Override
    public void hide() {

    }

    /**
     *
     */
    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
        menuBatch.end();
    }
}