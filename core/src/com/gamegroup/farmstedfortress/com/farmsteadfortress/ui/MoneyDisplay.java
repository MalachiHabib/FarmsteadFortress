package com.farmsteadfortress.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.farmsteadfortress.entities.Player;

public class MoneyDisplay {
    private Stage stage;
    private Label moneyLabel;
    private BitmapFont font;

    public MoneyDisplay(Player player) {
        stage = new Stage(new ScreenViewport());
        Table table = new Table();
        table.top().right();
        table.setFillParent(true);

        font = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        moneyLabel = new Label(String.valueOf(player.getMoney()), labelStyle);
        moneyLabel.setAlignment(Align.right);
        moneyLabel.setColor(Color.GOLD);
        table.add(new Label("Money: ", labelStyle));
        table.add(moneyLabel).padLeft(5).padRight(15);
        stage.addActor(table);
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
