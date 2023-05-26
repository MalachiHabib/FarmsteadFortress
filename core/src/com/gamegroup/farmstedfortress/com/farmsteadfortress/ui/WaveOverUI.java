package com.farmsteadfortress.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class WaveOverUI {
    private static final float DISPLAY_TIME = 3.0f;
    private static final String FONT_PATH = "gui/Lilian.fnt";
    private static final String FONT_IMAGE_PATH = "gui/Lilian.png";
    private static final String TEXT_MESSAGE = "Wave Over";

    private Stage stage;
    private Label label;
    private float timer;

    public WaveOverUI() {
        stage = createStage();
        label = createLabel();
        stage.addActor(label);
        timer = DISPLAY_TIME;
    }

    private Stage createStage() {
        return new Stage(new ScreenViewport());
    }

    private Label createLabel() {
        Label.LabelStyle labelStyle = createLabelStyle();

        Label newLabel = new Label(TEXT_MESSAGE, labelStyle);
        newLabel.setFontScale(2f);
        newLabel.pack();
        newLabel.setPosition(
                Gdx.graphics.getWidth() / 2f - newLabel.getWidth() / 2f,
                Gdx.graphics.getHeight() - newLabel.getHeight() - 10f
        );
        newLabel.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));

        return newLabel;
    }

    private Label.LabelStyle createLabelStyle() {
        BitmapFont bmfont = new BitmapFont(
                Gdx.files.internal(FONT_PATH),
                Gdx.files.internal(FONT_IMAGE_PATH),
                false
        );

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = bmfont;
        labelStyle.fontColor = Color.WHITE;

        return labelStyle;
    }

    public void render(float delta) {
        if (timer > 0) {
            timer -= delta;
            stage.act(delta);
            stage.draw();

            if (timer <= 0) {
                fadeOutAndReset();
            }
        }
    }

    private void fadeOutAndReset() {
        label.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.alpha(0)));
        timer = DISPLAY_TIME;
    }
}