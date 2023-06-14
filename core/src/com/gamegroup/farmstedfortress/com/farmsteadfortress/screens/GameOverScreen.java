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
        background = new Image(bgTexture);
        background.setSize(background.getWidth() * 4, background.getHeight() * 4);
        background.setPosition(Gdx.graphics.getWidth() / 2f - background.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - background.getHeight() /2 );

        final TextButton tryAgainButton = new TextButton("Try Again", skin, "default");
        tryAgainButton.setWidth(300f);
        tryAgainButton.setHeight(200f);
        final TextButton quitButton = new TextButton("Quit", skin, "default");
        quitButton.setWidth(300f);
        quitButton.setHeight(200f);

        int half = Gdx.graphics.getWidth() / 2;
        int quarter = (half) / 2;
        tryAgainButton.setPosition(quarter / 2f + quarter - 150f, Gdx.graphics.getHeight() / 4f);
        quitButton.setPosition( quarter / 2f + half - 150f, Gdx.graphics.getHeight() / 4f);
        stage.addActor(background);
        stage.addActor(tryAgainButton);
        stage.addActor(quitButton);
        Gdx.input.setInputProcessor(stage);

        bmfont = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );
        bmfont.getData().setScale(3, 3);

        tryAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((FarmsteadFortress) Gdx.app.getApplicationListener()).restartGame();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
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
        stage.draw();
        bmfont.setColor(1, 1, 1, 1);
        bmfont.draw(gameOverBatch, "Game Over", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 3f / 4f, 0f, 1, false);
        gameOverBatch.end();
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