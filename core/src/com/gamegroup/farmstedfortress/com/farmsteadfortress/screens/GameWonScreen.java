package com.farmsteadfortress.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.farmsteadfortress.FarmsteadFortress;
import com.farmsteadfortress.utils.Helpers;

public class GameWonScreen implements Screen {
    private FarmsteadFortress game;
    private Stage stage;
    private Skin skin;
    private Image background;
    private BitmapFont winFont;
    private TextButton playAgainButton;
    private TextButton quitButton;
    private SpriteBatch gameWonBatch;

    public GameWonScreen(FarmsteadFortress game) {
        this.game = game;
    }

    @Override
    public void show() {
        if (!FarmsteadFortress.backgroundMusic.isPlaying()) {
            FarmsteadFortress.backgroundMusic.play();
        }
        FarmsteadFortress.backgroundMusic.setVolume(game.getMusicVolume());
        initialise();
        createButtons();
        addActors();
        Gdx.input.setInputProcessor(stage);
    }

    private void initialise() {
        gameWonBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        stage = new Stage();
        background = new Image(new Texture(Gdx.files.internal("gui/msg-background.png")));
        winFont = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );
        winFont.getData().setScale(2, 2);

        background.setSize(background.getWidth() * 4, background.getHeight() * 4);
        background.setPosition(Gdx.graphics.getWidth() / 2 - background.getWidth() / 2, Gdx.graphics.getHeight() / 2 - background.getHeight() / 2);
    }

    private void createButtons() {
        TextButton.TextButtonStyle style = skin.get(TextButton.TextButtonStyle.class);
        float scaleFactor = Helpers.getScaleFactor();
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float buttonWidth = 200f;
        float buttonHeight = 150f;

        playAgainButton = new TextButton("Play Again", style);
        playAgainButton.setWidth(buttonWidth * scaleFactor);
        playAgainButton.setHeight(buttonHeight * scaleFactor);
        playAgainButton.setPosition((screenWidth / 3f) - (playAgainButton.getWidth() / 2f), screenHeight / 4f);

        quitButton = new TextButton("Quit", style);
        quitButton.setWidth(buttonWidth * scaleFactor);
        quitButton.setHeight(buttonHeight * scaleFactor);
        quitButton.setPosition((2f * screenWidth / 3f) - (quitButton.getWidth() / 2f), screenHeight / 4f);

        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.restartGame();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }


    private void addActors() {
        stage.addActor(background);
        stage.addActor(playAgainButton);
        stage.addActor(quitButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(55 / 255f, 125 / 255f, 176 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        gameWonBatch.begin();
        renderFont();
        gameWonBatch.end();
    }

    public void renderFont() {
        winFont.getData().setScale(3, 3);
        winFont.draw(gameWonBatch, "Congratulations", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 3 / 4, 0f, 1, false);
        winFont.getData().setScale(1.5f, 1.5f);
        winFont.draw(gameWonBatch, "You have beat FarmsteadFortress!", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 3 / 4 - 100, 0f, 1, false);
        winFont.getData().setScale(1, 1);
        winFont.draw(gameWonBatch, "Thank you for playing!", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 3 / 4 - 150, 0f, 1, false);
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
    }
}
