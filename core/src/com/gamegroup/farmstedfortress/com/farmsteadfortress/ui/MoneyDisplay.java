package com.farmsteadfortress.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.farmsteadfortress.entities.Player;

public class MoneyDisplay {
    private Stage stage;
    private Label moneyLabel;
    private BitmapFont font;
    private int currentBalance;

    public MoneyDisplay() {
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
        moneyLabel = new Label(String.valueOf(currentBalance), labelStyle);
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

    public void update(Player player) {
        int newBalance = player.getMoney();
        if (newBalance != currentBalance) {
            currentBalance = newBalance;
            moneyLabel.setText(String.valueOf(currentBalance));
            animateBalanceChange();
        }
    }

    private void animateBalanceChange() {
        moneyLabel.clearActions();
        moneyLabel.setFontScale(1f);

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
}