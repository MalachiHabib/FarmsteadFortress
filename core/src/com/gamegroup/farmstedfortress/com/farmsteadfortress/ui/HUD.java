package com.farmsteadfortress.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.waves.Wave;

public class HUD {
    private Stage stage;
    private Texture hudTexture;
    private BitmapFont font;
    private Label livesLabel, moneyLabel, waveLabel;
    private int currentBalance, playerLives, currentWave = 0;
    private Table livesTable, moneyTable, waveTable;

    public HUD() {
        stage = new Stage(new ScreenViewport());
        hudTexture = new Texture(Gdx.files.internal("gui/HUD.png"));
        Image hudImage = new Image(hudTexture);

        font = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );

        Label.LabelStyle livesLabelStyle = new Label.LabelStyle(font, Color.WHITE);
        livesLabel = new Label("", livesLabelStyle);
        Table hudTable = new Table();
        hudTable.top().left();
        hudTable.setFillParent(true);
        hudTable.add(hudImage).padLeft(10).padTop(10);

        livesTable = new Table();
        livesTable.top().left();
        livesTable.setFillParent(true);
        livesTable.add(livesLabel).padLeft(41).padTop(45);

        BitmapFont smallerFont = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );
        smallerFont.getData().setScale(.8f, .8f);
        Label.LabelStyle moneyLabelStyle = new Label.LabelStyle(smallerFont, Color.WHITE);
        moneyLabel = new Label(String.valueOf(currentBalance), moneyLabelStyle);
        moneyLabel.setColor(Color.GOLD);
        moneyTable = new Table();

        moneyTable.top().left();
        moneyTable.setFillParent(true);
        moneyTable.add(moneyLabel).padLeft(142).padTop(55.2f);

        Label.LabelStyle waveLabelStyle = new Label.LabelStyle(smallerFont, Color.WHITE);
        waveLabel = new Label(String.valueOf(currentWave), waveLabelStyle);
        waveTable = new Table();

        waveTable.top().left();
        waveTable.setFillParent(true);
        waveTable.add(waveLabel).padLeft(120).padTop(20);

        stage.addActor(hudTable);
        stage.addActor(livesTable);
        stage.addActor(moneyTable);
        stage.addActor(waveTable);
    }

    public Stage getStage() {
        return stage;
    }

    public void updateWaveCount(Wave wave) {
        waveLabel.setText("Wave: " + wave.getWaveNumber());
    }

    public void updateLivesCount(Player player) {
        int newLives = player.getHealth();
        if (newLives != playerLives) {
            playerLives = newLives;
            livesLabel.setText(String.valueOf(playerLives));
            if (playerLives < 100 && playerLives >= 9) {
                livesTable.padLeft(8);
            } else if (playerLives < 10) {
                livesTable.padLeft(16);
            }
        }
    }

    public void updatePlayerBalance(Player player) {
        int newBalance = player.getMoney();
        if (newBalance != currentBalance) {
            currentBalance = newBalance;
            moneyLabel.setText(String.valueOf(currentBalance));
            animateBalanceChange();
        }
    }

    private void animateBalanceChange() {
        moneyLabel.clearActions();
        moneyLabel.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.scaleTo(1.5f, 1.5f, 0.1f),
                                Actions.fadeIn(0.1f)
                        ),
                        Actions.parallel(
                                Actions.scaleTo(1f, 1f, 0.3f),
                                Actions.fadeOut(0.3f)
                        ),
                        Actions.fadeIn(0.1f)
                )
        );
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void dispose() {
        hudTexture.dispose();
        font.dispose();
        stage.dispose();
    }
}