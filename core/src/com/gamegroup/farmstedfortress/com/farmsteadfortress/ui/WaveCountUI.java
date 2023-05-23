package com.farmsteadfortress.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.farmsteadfortress.waves.Wave;

public class WaveCountUI {
    private Stage stage;
    private Skin skin;
    private Table table;
    private Label waveNumberLabel;

    public WaveCountUI() {
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        this.table = new Table();
        table.setFillParent(true);
        table.top().left().padTop(50);

        BitmapFont font = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );

        waveNumberLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));

        table.add(waveNumberLabel).padLeft(5).padRight(15).padTop(60);
        stage.addActor(this.table);
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