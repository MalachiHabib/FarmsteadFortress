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
import com.farmsteadfortress.items.seeds.Seed;

import java.util.List;

public class ShopUI {
    private final Stage stage;
    private final Skin skin;
    private final Array<ImageButton> buttons;
    private final Inventory inventory;
    private final Hotbar hotbar;
    Player player;
    private Image backgroundImage;
    private boolean isOpen;

    public ShopUI(Hotbar hotbar, Player player) {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        Table table = new Table();
        buttons = new Array<>();
        BitmapFont font = new BitmapFont();
        this.inventory = player.getInventory();
        this.player = player;
        this.hotbar = hotbar;
        this.isOpen = false;
        createShop();
        createShopLabel();
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
        Label shopLabel = new Label("SHOP", labelStyle);
        shopLabel.setFontScale(2f);
        shopLabel.pack();
        shopLabel.setPosition(Gdx.graphics.getWidth() * 0.86f, Gdx.graphics.getHeight() * 0.84f);
        stage.addActor(shopLabel);
    }

    private void createShop() {
        Table buttonTable = new Table();
        buttonTable.right().bottom();
        buttonTable.padLeft(35);
        List<Seed> seedTypes = Seed.getSeedTypes();
        for (int i = 0; i < seedTypes.size(); i++) {
            final Seed seed = seedTypes.get(i);
            ImageButton button = new ImageButton(new TextureRegionDrawable(seed.getTexture()));
            button.setName("shopActor");
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (isOpen && player.getMoney() >= seed.getPrice()) {
                        player.addMoney(-5);
                        inventory.addItem(seed);
                        inventory.printInventory();
                    }
                }
            });
            buttons.add(button);
            buttonTable.add(button).size(100, 100).pad(20);
            if ((i + 1) % 2 == 0) buttonTable.row();
        }

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        ScrollPane scrollPane = new ScrollPane(buttonTable, scrollPaneStyle);
        scrollPane.setName("shopActor");
        scrollPane.setSize(Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight());
        scrollPane.setPosition(Gdx.graphics.getWidth() - scrollPane.getWidth() + 108, 2.5f * Gdx.graphics.getHeight() / 4 - scrollPane.getHeight() / 2 - 20);
        Image shopBackground = new Image(new Texture(Gdx.files.internal("gui/shop-right-background.png")));
        shopBackground.setName("shopActor");
        shopBackground.setSize(scrollPane.getWidth() / 1.8f, Gdx.graphics.getHeight());
        shopBackground.setPosition(Gdx.graphics.getWidth() - shopBackground.getWidth(), 0);

        // Center the button table within the shopBackground
        buttonTable.center();
        buttonTable.setFillParent(true);

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