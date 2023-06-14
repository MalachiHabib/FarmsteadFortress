package com.farmsteadfortress.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.farmsteadfortress.FarmsteadFortress;

public class OptionsScreen implements Screen {
    FarmsteadFortress game;
    Skin skin;
    Stage stage;
    Slider volumeMusicSlider;
    Slider volumeSoundSlider;
    SpriteBatch optionsBatch;
    Texture bgTexture;
    Image background;
    BitmapFont font;

    public OptionsScreen(FarmsteadFortress game) {
        this.game = game;
    }

    public void create() {
        optionsBatch = new SpriteBatch();
        bgTexture = new Texture(Gdx.files.internal("gui/msg-background.png"));
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );
        font.getData().setScale(1.5f, 1.5f);

        TextButton.TextButtonStyle defaultStyle = skin.get(TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle newStyle = new TextButton.TextButtonStyle();
        newStyle.font = font;
        newStyle.up = defaultStyle.up;
        newStyle.down = defaultStyle.down;
        newStyle.checked = defaultStyle.checked;

        background = new Image(bgTexture);
        background.setSize(background.getWidth() * 4, background.getHeight() * 4);
        background.setPosition(Gdx.graphics.getWidth() / 2 - background.getWidth() / 2, Gdx.graphics.getHeight() / 2 - background.getHeight() / 2);

        volumeMusicSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin);
        volumeMusicSlider.setWidth(200f);
        volumeMusicSlider.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        volumeMusicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setMusicVolume(volumeMusicSlider.getValue());
            }
        });

        volumeSoundSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin);
        volumeSoundSlider.setWidth(200f);
        volumeSoundSlider.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - volumeSoundSlider.getHeight() * 2);
        volumeSoundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setSoundVolume(volumeSoundSlider.getValue());
            }
        });

        TextButton backButton = new TextButton("Back", newStyle);
        backButton.setWidth(200f);
        backButton.setHeight(100f);
        backButton.setPosition(Gdx.graphics.getWidth() / 2 - backButton.getWidth() / 2, Gdx.graphics.getHeight() / 4);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(FarmsteadFortress.menuScreen);
            }
        });

        stage.addActor(background);
        stage.addActor(volumeMusicSlider);
        stage.addActor(volumeSoundSlider);
        stage.addActor(backButton);
    }

    @Override
    public void show() {
        create();
        volumeMusicSlider.setValue(game.getMusicVolume());
        volumeSoundSlider.setValue(game.getSoundVolume());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(55 / 255f, 125 / 255f, 176 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        optionsBatch.begin();
        font.draw(optionsBatch, "Options", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 3 / 4, 0f, 1, false);
        font.draw(optionsBatch, "Music Volume", volumeMusicSlider.getX() - (1f * Gdx.graphics.getWidth() / 6.5f), volumeMusicSlider.getY() + 35f);
        font.draw(optionsBatch, "Sound Volume", volumeSoundSlider.getX() - (1f * Gdx.graphics.getWidth() / 6.5f), volumeSoundSlider.getY() + 35f);
        stage.draw();
        optionsBatch.end();
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
        font.dispose();
        skin.dispose();
        stage.dispose();
    }
}
