package com.farmsteadfortress.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.farmsteadfortress.FarmsteadFortress;

public class MenuScreen implements Screen {
    FarmsteadFortress game;
    SpriteBatch menuBatch;

    Texture bgTexture;
    Image background;
    Skin skin;
    Stage stage;
    BitmapFont bmfont;

    public MenuScreen(FarmsteadFortress game) {
        this.game = game;
    }

    public void create() {
        menuBatch = new SpriteBatch();
        bgTexture = new Texture(Gdx.files.internal("gui/msg-background.png"));
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        stage = new Stage();
        background = new Image(bgTexture);
        //background.setScale(2f);
        background.setSize(background.getWidth() * 4, background.getHeight() * 4);
        //background.setOrigin(0, 0);
        background.setPosition(Gdx.graphics.getWidth() / 2 - background.getWidth() / 2, Gdx.graphics.getHeight() / 2 - background.getHeight() /2 );
        //background.setPosition(Gdx.graphics.getWidth() / 2 - bgTexture.getWidth() / 2, Gdx.graphics.getHeight() / 2 - bgTexture.getHeight() /2);

        final TextButton startButton = new TextButton("Start", skin, "default");
        startButton.setWidth(300f);
        startButton.setHeight(200f);
        final TextButton quitButton = new TextButton("Quit", skin, "default");
        quitButton.setWidth(300f);
        quitButton.setHeight(200f);

        // maybe realign buttons more toward the center; seem to be very far apart
        // need to increase the size of the text on the buttons

        // need to resize background
        // need to center background properly

        int half = Gdx.graphics.getWidth() / 2;
        int quarter = (half) / 2;
        startButton.setPosition(quarter / 2 + quarter - 150f, Gdx.graphics.getHeight() / 4);
        quitButton.setPosition( quarter / 2 + half - 150f, Gdx.graphics.getHeight() / 4);
        stage.addActor(background);
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
        bmfont.getData().setScale(3, 3);

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
        Gdx.gl.glClearColor(55/255f, 125/255f, 176/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        menuBatch.begin();
        stage.draw();
        bmfont.setColor(1, 1, 1, 1);
        bmfont.draw(menuBatch, "Farmstead Fortress", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 3 / 4, 0f, 1, false);
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
        Gdx.input.setInputProcessor(null);
        stage.clear();
    }

    /**
     *
     */
    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
        menuBatch.dispose();
    }
}