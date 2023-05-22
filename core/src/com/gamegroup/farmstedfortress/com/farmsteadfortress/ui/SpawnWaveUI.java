package com.farmsteadfortress.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.farmsteadfortress.waves.Wave;
import com.farmsteadfortress.waves.WaveController;

public class SpawnWaveUI {
    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton startWaveButton;
    private Label waveNumberLabel;

    public SpawnWaveUI(final WaveController waveController) {
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        this.table = new Table();
        this.table.setFillParent(true);

        BitmapFont font = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.RED;

        startWaveButton = new TextButton("Start Wave", textButtonStyle);

        startWaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                waveController.startWave();
            }
        });

        waveNumberLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));

        this.table.add(waveNumberLabel).padBottom(10).row();
        this.table.add(startWaveButton);

        this.stage.addActor(this.table);
    }

    public Stage getStage() {
        return this.stage;
    }

    public void updateWaveNumber(Wave wave) {
        waveNumberLabel.setText("Wave: " + wave.getWaveNumber());
    }

    public void render() {
        stage.act();
        stage.draw();
    }
}