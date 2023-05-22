package com.farmsteadfortress.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.inventory.Inventory;
import com.farmsteadfortress.items.seeds.TomatoSeed;

public class Shop {
    Player player;
    private Stage stage;
    private Skin skin;
    private Table table;
    private Array<ImageButton> buttons;
    private Label shopLabel;
    private Image backgroundImage;
    private ScrollPane scrollPane;
    private Table buttonTable;
    private BitmapFont font;
    private Inventory inventory;
    private Hotbar hotbar;
    private boolean isOpen;


    public Shop(Hotbar hotbar, Player player) {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        table = new Table();
        buttons = new Array<>();
        font = new BitmapFont();
        this.inventory = player.getInventory();
        this.player = player;
        this.hotbar = hotbar;
        this.isOpen = false;
        createShopLabel();
        createShop();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        this.isOpen = true;
    }

    public void close() {
        this.isOpen = false;
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
        shopLabel = new Label("SHOP", labelStyle);
        shopLabel.setFontScale(3);
        shopLabel.pack();

        shopLabel.setPosition(Gdx.graphics.getWidth() / 2 - shopLabel.getWidth() / 2, 3 * Gdx.graphics.getHeight() / 4);
        stage.addActor(shopLabel);
    }

    private void createShop() {
        buttonTable = new Table();
        buttonTable.right().bottom();
        for (int i = 0; i < 8; i++) {
            final TomatoSeed seed = new TomatoSeed();
            ImageButton button = new ImageButton(new TextureRegionDrawable(seed.getTexture()));
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (player.getMoney() >= seed.getPrice()) {
                        player.addMoney(-5);
                        inventory.addItem(seed);
                        inventory.printInventory();
                        hotbar.updateHotbar();
                    }
                }
            });
            buttons.add(button);
            buttonTable.add(button).size(100, 80).pad(10);
            if ((i + 1) % 2 == 0) buttonTable.row();
        }

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPane = new ScrollPane(buttonTable, scrollPaneStyle);
        scrollPane.setSize(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight());

        scrollPane.setPosition(Gdx.graphics.getWidth() - scrollPane.getWidth(), 3 * Gdx.graphics.getHeight() / 4 - scrollPane.getHeight() / 2);

        Image shopBackground = new Image(new Texture(Gdx.files.internal("gui/shop-right-background.png")));
        shopBackground.setSize(scrollPane.getWidth() / 1.8f , Gdx.graphics.getHeight());
        shopBackground.setPosition(Gdx.graphics.getWidth() - shopBackground.getWidth(), 0);
        shopBackground.setColor(1f,1f,1f,0.5f);

        stage.addActor(shopBackground);
        stage.addActor(scrollPane);
    }


    public void render() {
        if (isOpen) {
            stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            stage.draw();
        }
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}