package com.farmsteadfortress.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.farmsteadfortress.entities.Player;

public class HealthUI {
    public Stage stage;

    private Label healthLabel;

    public Skin skin;

    public BitmapFont font;

    public int playerHealth;
    // create a bar
    // reduce bar when damaged
    // maybe adapt health bar to enemies and plants
    // maybe display above hot bar

    public HealthUI(){
        // this draws only the player health so may need to separate this out into other functions
        // or could only draw one type of health bar and reuse it for the player and enemies
        stage = new Stage(new ScreenViewport());
        Table table = new Table();
        table.top().left().padTop(50);
        table.setFillParent(true);

        this.skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        font = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        healthLabel = new Label(String.valueOf(playerHealth), labelStyle);
        healthLabel.setColor(Color.GREEN);
        Texture heartTexture = new Texture(Gdx.files.internal("gui/heart.png"));
        Image heartImage = new Image(heartTexture);
        heartImage.setScale(1f);
        table.add(heartImage).padLeft(10);
        table.add(healthLabel);
        stage.addActor(table);
    }

    public void drawPlayerHealth(){
        // draw player health above hotbar
    }



    public void update(Player player){
        int newHealth = player.getPlayerHealth();
        if(newHealth != playerHealth){
            playerHealth = newHealth;
            healthLabel.setText(String.valueOf(playerHealth));
        }
        if(playerHealth < 50){
            healthLabel.setColor(Color.ORANGE);
        }
        if(playerHealth < 25){
            healthLabel.setColor(Color.RED);
        }

    }

    public void render(){
        stage.act();
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
    }
}