package com.farmsteadfortress.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.farmsteadfortress.inventory.Inventory;
import com.farmsteadfortress.items.Item;
import com.farmsteadfortress.utils.Helpers;

public class Hotbar {
    public ImageButton highlightedButton;
    public Item selectedSlotItem;
    private Stage stage;
    private Table table;
    private Array<ImageButton> buttons;
    private TextureRegionDrawable highlightDrawable;
    private Drawable defaultDrawable;
    private Inventory inventory;
    private Texture bannerTexture;
    private Drawable circleDefaultDrawable;
    private Table circleTable;
    private Array<ImageButton> circleButtons;
    private Drawable circleHighlightDrawable;
    public ImageButton highlightedCircleButton;
    private Shop shop;

    public Hotbar(Inventory inventory, Shop shop) {
        this.stage = new Stage(new ScreenViewport());
        this.table = new Table();
        this.buttons = new Array<>();
        this.circleTable = new Table();
        this.shop = shop;
        this.inventory = inventory;
        circleButtons = new Array<>();
        initTextures();
        createHotbar();
    }

    private void initTextures() {
        bannerTexture = new Texture(Gdx.files.internal("gui/banner.png"));
        circleDefaultDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-slot-circle.png")));
        circleHighlightDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-slot-circle-highlighted.png")));
        defaultDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-slot.png")));
        highlightDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-slot-highlighted.png")));

        Image bannerImage = new Image(bannerTexture);
        bannerImage.setHeight(Gdx.graphics.getHeight() * 1.4f);
        stage.addActor(bannerImage);
        circleTable.setFillParent(true);
        stage.addActor(circleTable);
        table.setFillParent(true);
        stage.addActor(table);
    }

    public void updateHotbar() {
        for (int i = 0; i < buttons.size; i++) {
            ImageButton button = buttons.get(i);
            if (i < inventory.getItems().size) {
                Item item = inventory.getItems().get(i);
                Drawable itemDrawable = new TextureRegionDrawable(item.getTexture());
                button.getStyle().imageUp = itemDrawable;
            } else {
                button.getStyle().imageUp = defaultDrawable;
            }
        }
    }

    private void createHotbar() {
        table.left();
        for (int i = 0; i < 5; i++) {
            final ImageButton button = new ImageButton(defaultDrawable);
            final ImageButton highlightButton = new ImageButton(highlightDrawable);
            highlightButton.setVisible(false);
            final Stack stack = new Stack();
            stack.add(button);
            stack.add(highlightButton);
            buttons.add(button);
            table.add(stack).size(100, 75).pad(10);

            if (i < inventory.getItems().size) {
                Item item = inventory.getItems().get(i);
                Drawable itemDrawable = new TextureRegionDrawable(item.getTexture());
                button.getStyle().imageUp = itemDrawable;
            }

            final int slotIndex = i;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Helpers.handleButtonClick(button, stack, slotIndex, stage, inventory, Hotbar.this, false);
                }
            });
        }
        table.bottom();
        createCircleHotbar();
    }

    private void createCircleHotbar() {
        circleTable.right().bottom();
        for (int i = 0; i < 3; i++) {
            final ImageButton button = new ImageButton(circleDefaultDrawable);
            final ImageButton highlightButton = new ImageButton(circleHighlightDrawable);
            highlightButton.setVisible(false);
            final Stack stack = new Stack();
            stack.add(button);
            stack.add(highlightButton);
            circleButtons.add(button);
            circleTable.add(stack).size(100, 80).pad(10);

            final int slotIndex = i;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Helpers.handleButtonClick(button, stack, slotIndex, stage, inventory, Hotbar.this, true);
                    if (slotIndex == 2) {
                        System.out.println("open");
                        shop.open();
                    }
                }
            });
        }
    }

    public Item getSelectedSlotItem() {
        return selectedSlotItem;
    }

    public Stage getStage() {
        return stage;
    }

    public void render() {
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
        bannerTexture.dispose();
    }
}