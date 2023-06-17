package com.farmsteadfortress.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.farmsteadfortress.FarmsteadFortress;

public class CreditsScreen implements Screen {
    FarmsteadFortress game;
    Skin skin;
    Stage stage;
    SpriteBatch creditsBatch;
    Texture bgTexture;
    Image background;
    BitmapFont font;

    public CreditsScreen(FarmsteadFortress game){
        this.game = game;
    }

    public void create(){
        creditsBatch = new SpriteBatch();
        bgTexture = new Texture(Gdx.files.internal("gui/msg-background.png"));
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );
        font.getData().setScale(1.2f, 1.2f);

        TextButton.TextButtonStyle defaultStyle = skin.get(TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle newStyle = new TextButton.TextButtonStyle();
        newStyle.font = font;
        newStyle.up = defaultStyle.up;
        newStyle.down = defaultStyle.down;
        newStyle.checked = defaultStyle.checked;

        background = new Image(bgTexture);
        background.setSize(background.getWidth() * 4, background.getHeight() * 4);
        background.setPosition(Gdx.graphics.getWidth() / 2 - background.getWidth() / 2, Gdx.graphics.getHeight() / 2 - background.getHeight() / 2);

        TextButton backButton = new TextButton("Back", newStyle);
        backButton.setWidth(200f);
        backButton.setHeight(100f);
        backButton.setPosition(Gdx.graphics.getWidth() / 2 - backButton.getWidth() / 2, Gdx.graphics.getHeight() / 7);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(FarmsteadFortress.menuScreen);
            }
        });

        stage.addActor(background);
        stage.addActor(backButton);
    }

    @Override
    public void show() {
        create();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(55 / 255f, 125 / 255f, 176 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        creditsBatch.begin();
        font.getData().setScale(2, 2);
        font.draw(creditsBatch, "Credits", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 3f / 4f, 0f, 1, false);
        font.getData().setScale(1.2f, 1.2f);
        font.draw(creditsBatch, "Font sourced from \n https://www.dafont.com/lilian-2.font?back=bitmap", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 3 / 5, 0f, 1, false);
        font.getData().setScale(1f, 1f);
        font.draw(creditsBatch, "Graphics sourced from \n https://craftpix.net/ and \n https://itch.io/game-assets/tag-isometric/tag-pixel-art", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 2 / 5 + 50, 0f, 1, false);  // Position adjusted
        font.draw(creditsBatch, "Sound design sourced from \n https://www.zapsplat.com/", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 1 / 5 + 100, 0f, 1, false);  // Position adjusted

        stage.draw();
        creditsBatch.end();
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
        font.dispose();
        skin.dispose();
        stage.dispose();
    }
}
