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

public class GameOverScreen implements Screen {
    SpriteBatch gameOverBatch;

    Texture bgTexture;
    Image background;
    Skin skin;
    Stage stage;
    BitmapFont bmfont;

    public GameOverScreen() {
        create();
    }

    public void create() {
        gameOverBatch = new SpriteBatch();
        bgTexture = new Texture(Gdx.files.internal("gui/msg-background.png"));
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        stage = new Stage();
        createTryAgainButton();
    }


    public void createTryAgainButton() {
        final TextButton tryAgainButton = new TextButton("Try Again", skin, "default");
        tryAgainButton.setWidth(300f);
        tryAgainButton.setHeight(200f);
        int half = Gdx.graphics.getWidth() / 2;
        int quarter = half / 2;
        tryAgainButton.setPosition(quarter / 2f + quarter - 150f, Gdx.graphics.getHeight() / 4f);

        // Set action on button click
        tryAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((FarmsteadFortress) Gdx.app.getApplicationListener()).restartGame();
            }
        });

        // Add the button to the stage
        stage.addActor(tryAgainButton);
    }


    @Override
    public void show() {
        create();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(55/255f, 125/255f, 176/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameOverBatch.begin();
        bmfont.setColor(1, 1, 1, 1);
        bmfont.draw(gameOverBatch, "Game Over", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 3f / 4f, 0f, 1, false);
        gameOverBatch.end();

        stage.draw(); // draw the stage which includes the "Try Again" button
    }


    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        stage.clear();
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
        gameOverBatch.dispose();
    }
}