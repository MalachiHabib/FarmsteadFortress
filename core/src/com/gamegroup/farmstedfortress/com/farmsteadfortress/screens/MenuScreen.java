package com.farmsteadfortress.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
    private final int screenWidth = Gdx.graphics.getWidth();
    private Music backgroundMusic;
    private TextButton quitButton;
    private TextButton startButton;
    private FarmsteadFortress game;
    private SpriteBatch menuBatch;
    private Texture bgTexture;
    private Image background;
    private Skin skin;
    private Stage stage;
    private BitmapFont headingFont;
    private BitmapFont buttonFont;
    private TextButton optionsButton;

    public MenuScreen(FarmsteadFortress game) {
        this.game = game;
        game.initialisePreferences();
    }

    public void create() {
        if (!FarmsteadFortress.backgroundMusic.isPlaying()) {
            FarmsteadFortress.backgroundMusic.play();
        }
        FarmsteadFortress.backgroundMusic.setVolume(game.getMusicVolume());
        initialise();
        createButtons();
        addActors();
        Gdx.input.setInputProcessor(stage);
    }

    private void addActors() {
        stage.addActor(background);
        stage.addActor(startButton);
        stage.addActor(optionsButton);
        stage.addActor(quitButton);
    }

    private void createButtons() {
        BitmapFont buttonFont = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );
        buttonFont.getData().setScale(1.5f, 1.5f);

        TextButton.TextButtonStyle defaultStyle = skin.get(TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle newStyle = new TextButton.TextButtonStyle();
        newStyle.font = buttonFont;
        newStyle.up = defaultStyle.up;
        newStyle.down = defaultStyle.down;
        newStyle.checked = defaultStyle.checked;

        startButton = new TextButton("Start", newStyle);
        startButton.setWidth(300f);
        startButton.setHeight(200f);

        quitButton = new TextButton("Quit", newStyle);
        quitButton.setWidth(300f);
        quitButton.setHeight(200f);

        optionsButton = new TextButton("Options", newStyle);
        optionsButton.setWidth(300f);
        optionsButton.setHeight(200f);

        optionsButton.setPosition(screenWidth / 2f - optionsButton.getWidth() / 2f, Gdx.graphics.getHeight() / 4f);
        startButton.setPosition(screenWidth / 3f - startButton.getWidth() / 2, Gdx.graphics.getHeight() / 4f);
        quitButton.setPosition(2f * screenWidth / 3f - quitButton.getWidth() / 2, Gdx.graphics.getHeight() / 4f);

        headingFont = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );

        headingFont.getData().setScale(3, 3);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.stopMusic();
                game.setScreen(FarmsteadFortress.screen);
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(FarmsteadFortress.optionsScreen);
            }
        });
    }

    private void initialise() {
        menuBatch = new SpriteBatch();
        bgTexture = new Texture(Gdx.files.internal("gui/msg-background.png"));
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        optionsButton = new TextButton("Options", skin, "default");
        stage = new Stage();
        background = new Image(bgTexture);

        background.setSize(background.getWidth() * 4, background.getHeight() * 4);
        background.setPosition(Gdx.graphics.getWidth() / 2 - background.getWidth() / 2, Gdx.graphics.getHeight() / 2 - background.getHeight() / 2);
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
        Gdx.gl.glClearColor(55 / 255f, 125 / 255f, 176 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        menuBatch.begin();
        stage.draw();
        headingFont.setColor(1, 1, 1, 1);
        headingFont.draw(menuBatch, "Farmstead Fortress", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 3 / 4, 0f, 1, false);
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
        backgroundMusic.pause();
    }

    /**
     *
     */
    @Override
    public void resume() {
        backgroundMusic.play();
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
        backgroundMusic.dispose();
        skin.dispose();
        stage.dispose();
        menuBatch.dispose();
    }
}