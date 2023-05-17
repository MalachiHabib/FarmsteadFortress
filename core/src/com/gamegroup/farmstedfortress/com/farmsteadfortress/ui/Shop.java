package com.farmsteadfortress.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.inventory.Inventory;
import com.farmsteadfortress.items.seeds.TomatoSeed;

public class Shop {
    private Stage stage;
    private Skin skin;
    private Table table;
    private ImageButton shopButton;
    private Drawable defaultDrawable;
    private Array<ImageButton> buttons;
    private Label shopLabel;
    private Image backgroundImage;
    private ScrollPane scrollPane;
    private Table buttonTable;
    private BitmapFont font;
    private boolean isShopOpen;
    Player player;
    private Inventory inventory;
    private Hotbar hotbar;

    public Shop(Hotbar hotbar, Player player) {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        table = new Table();
        buttons = new Array<>();
        font = new BitmapFont();
        this.inventory = player.getInventory();
        this.player = player;
        this.hotbar = hotbar;

        createBackground();
        createShopButton();
        createShopLabel();
        createShop();
        isShopOpen = false;
    }

    private void createBackground() {
        Texture backgroundTexture = new Texture(Gdx.files.internal("gui/background.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        backgroundImage.setVisible(false);
        stage.addActor(backgroundImage);
    }

    private void createShopLabel() {
        BitmapFont bmfont = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = bmfont;
        labelStyle.fontColor = Color.WHITE;
        shopLabel = new Label("Shop", labelStyle);
        shopLabel.setFontScale(3);
        shopLabel.pack();

        float labelX = (Gdx.graphics.getWidth() - shopLabel.getWidth()) / 2;
        float labelY = Gdx.graphics.getHeight() / 1.3f;

        shopLabel.setPosition(labelX, labelY);
        shopLabel.setVisible(false);
        stage.addActor(shopLabel);
    }

    private void createShopButton() {
        defaultDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-slot.png")));
        shopButton = new ImageButton(defaultDrawable);
        shopButton.setPosition(20, Gdx.graphics.getHeight() - 50 - shopButton.getHeight());
        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleShop();
            }
        });
        stage.addActor(shopButton);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE && isShopOpen) {
                    toggleShop();
                    return true;
                }
                return false;
            }
        });
    }

    private void createShop() {
        buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.setVisible(false);

        for (int i = 0; i < 9; i++) {
            final TomatoSeed seed = new TomatoSeed();
            ImageButton button = new ImageButton(new TextureRegionDrawable(seed.getTexture()));
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                        player.setMoney(-5);
                        inventory.addItem(seed);
                        inventory.printInventory();
                        hotbar.updateHotbar();
                }
            });
            buttons.add(button);
            buttonTable.add(button).size(100, 100).pad(10);
            if ((i + 1) % 3 == 0) buttonTable.row();
        }

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPane = new ScrollPane(buttonTable, scrollPaneStyle);
        scrollPane.setFillParent(true);
        scrollPane.setVisible(false);
        stage.addActor(scrollPane);
    }

    private void toggleShop() {
        isShopOpen = !isShopOpen;
        shopLabel.setVisible(isShopOpen);
        backgroundImage.setVisible(isShopOpen);
        scrollPane.setVisible(isShopOpen);
        buttonTable.setVisible(isShopOpen);

        shopButton.remove();
        stage.addActor(shopButton);
    }

    public void render() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}