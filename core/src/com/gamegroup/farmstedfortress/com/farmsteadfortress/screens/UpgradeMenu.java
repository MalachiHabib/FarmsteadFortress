package com.farmsteadfortress.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.farmsteadfortress.FarmsteadFortress;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.entities.plants.Plant;
import com.farmsteadfortress.utils.Helpers;

public class UpgradeMenu implements Screen {
    private TextButton backButton;
    private TextButton upgrade;
    private FarmsteadFortress game;
    private SpriteBatch upgradeBatch;
    private Skin skin;
    private Stage stage;
    private BitmapFont buttonFont;
    private BitmapFont headingFont;
    private Plant plant;
    private Label upgradeLabel;
    private Player player;


    public UpgradeMenu(FarmsteadFortress game, Plant plant, Player player) {
        this.game = game;
        this.plant = plant;
        this.player = player;
    }

    @Override
    public void show() {
        upgradeBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        stage = new Stage();
        headingFont = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );
        headingFont.getData().setScale(3, 3);

        createUpgradeButtons();
        createBackButton();
        createUpgradeLabels();
        addUpgradeActors();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(55 / 255f, 125 / 255f, 176 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        upgradeBatch.begin();
        stage.draw();
        headingFont.setColor(1, 1, 1, 1);
        headingFont.getData().setScale(3, 3);
        headingFont.draw(upgradeBatch, plant.getName(), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 3 / 4 + 100, 0f, 1, false);
        headingFont.draw(upgradeBatch, "Upgrade Menu", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 3 / 4 + 20, 0f, 1, false);
        upgradeBatch.end();
    }

    private void createUpgradeButtons() {
        buttonFont = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );
        buttonFont.getData().setScale(1.25f, 1.25f);

        TextButton.TextButtonStyle defaultStyle = skin.get(TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle newStyle = new TextButton.TextButtonStyle();
        newStyle.font = buttonFont;
        newStyle.up = defaultStyle.up;
        newStyle.down = defaultStyle.down;
        newStyle.checked = defaultStyle.checked;

        upgrade = new TextButton("Cost: 15", newStyle);

        upgrade.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player.getMoney() >= 15) {
                    plant.upgrade();
                } else {
                    String message = "You cannot afford this. You have " + player.getMoney() + " dollars.";
                    Helpers.showDialog(stage, "Insufficient Funds", message);
                }
            }
        });

        float scaleFactor = 1.5f;
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float padding = 20f;

        upgrade.setPosition(screenWidth / 2f - (upgrade.getWidth() * scaleFactor) / 2f, screenHeight / 4f + 3 * (upgrade.getHeight() * scaleFactor + padding));
        upgrade.setSize(upgrade.getWidth() * scaleFactor, upgrade.getHeight() * scaleFactor);
    }


    private void createBackButton() {
        TextButton.TextButtonStyle defaultStyle = skin.get(TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle newStyle = new TextButton.TextButtonStyle();
        newStyle.font = buttonFont;
        newStyle.up = defaultStyle.up;
        newStyle.down = defaultStyle.down;
        newStyle.checked = defaultStyle.checked;

        backButton = new TextButton("Back", newStyle);
        backButton.setPosition(Gdx.graphics.getWidth() - backButton.getWidth() - 20, Gdx.graphics.getHeight() - backButton.getHeight() - 20);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getGameScreen());
            }
        });
    }

    private void addUpgradeActors() {
        stage.addActor(backButton);
        stage.addActor(upgrade);
        stage.addActor(upgradeLabel);
    }

    private void createUpgradeLabels() {
        upgradeLabel = new Label("Upgrade", new Label.LabelStyle(buttonFont, Color.WHITE));
        upgradeLabel.setPosition(upgrade.getX() - upgradeLabel.getWidth() - 50f, upgrade.getY() + upgrade.getHeight() / 4);
        upgradeLabel.setBounds(upgradeLabel.getX(), upgradeLabel.getY(), upgradeLabel.getWidth(), upgradeLabel.getPrefHeight());
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
    }

    @Override
    public void dispose() {
    }
}
