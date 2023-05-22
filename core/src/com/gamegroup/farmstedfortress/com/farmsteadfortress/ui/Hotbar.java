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

public class Hotbar {
    public ImageButton highlightedButton;
    public Item selectedSlotItem;
    public ImageButton highlightedCircleButton;
    private final Stage stage;
    private final Table table;
    private final Array<ImageButton> buttons;
    private TextureRegionDrawable highlightDrawable;
    private Drawable defaultDrawable;
    private final Inventory inventory;
    private Texture bannerTexture;
    private Drawable circleDefaultDrawable;
    private final Table circleTable;
    private final Array<ImageButton> circleButtons;
    private Drawable circleHighlightDrawable;
    private final Shop shop;
    private boolean isShopOpen = false;
    private int lastInventorySize = 0;

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
        bannerImage.setHeight(Gdx.graphics.getHeight() / 8.5f);
        stage.addActor(bannerImage);
        circleTable.setFillParent(true);
        stage.addActor(circleTable);
        table.setFillParent(true);
        stage.addActor(table);
    }

    public void updateHotbar() {
        int inventorySize = inventory.getItems().size;

        for (int i = lastInventorySize; i < inventorySize; i++) {
            ImageButton button = buttons.get(i);
            Item item = inventory.getItems().get(i);
            button.getStyle().imageUp = new TextureRegionDrawable(item.getTexture());
        }

        if (inventorySize < lastInventorySize) {
            for (int i = inventorySize; i < lastInventorySize; i++) {
                ImageButton button = buttons.get(i);
                button.getStyle().imageUp = defaultDrawable;
            }
        }

        lastInventorySize = inventorySize;
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
            table.add(stack).size(100, 65).pad(10);

            if (i < inventory.getItems().size) {
                Item item = inventory.getItems().get(i);
                button.getStyle().imageUp = new TextureRegionDrawable(item.getTexture());
            }

            final int slotIndex = i;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    handleButtonClick(button, stack, slotIndex, false);
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
            circleTable.add(stack).size(100, 65).pad(10);

            final int slotIndex = i;
            if (i == 2) {
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        toggleShop();
                    }
                });
            } else {
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        handleButtonClick(button, stack, slotIndex, true);
                    }
                });
            }
        }
    }


    private void toggleShop() {
        if (this.isShopOpen) {
            this.shop.close();
            this.isShopOpen = false;
        } else {
            this.shop.open();
            this.isShopOpen = true;
        }
    }

    public void handleButtonClick(final ImageButton button, final Stack stack, final int slotIndex, boolean isCircleButton) {
        ImageButton currentHighlightedButton = isCircleButton ? this.highlightedCircleButton : this.highlightedButton;
        if (currentHighlightedButton != null) {
            if (currentHighlightedButton == button) {
                currentHighlightedButton.getParent().getChildren().get(1).setVisible(false);
                if (isCircleButton) {
                    this.highlightedCircleButton = null;
                } else {
                    this.highlightedButton = null;
                }
                this.selectedSlotItem = null;
            } else {
                currentHighlightedButton.getParent().getChildren().get(1).setVisible(false);
                if (isCircleButton) {
                    this.highlightedCircleButton = button;
                } else {
                    this.highlightedButton = button;
                }
                if (slotIndex < this.inventory.getItems().size) {
                    this.selectedSlotItem = this.inventory.getItems().get(slotIndex);
                } else {
                    this.selectedSlotItem = null;
                }
                stack.getChildren().get(1).setVisible(true);
            }
        } else {
            if (isCircleButton) {
                this.highlightedCircleButton = button;
            } else {
                this.highlightedButton = button;
            }
            if (slotIndex < this.inventory.getItems().size) {
                this.selectedSlotItem = this.inventory.getItems().get(slotIndex);
            } else {
                this.selectedSlotItem = null;
            }
            stack.getChildren().get(1).setVisible(true);
        }
        if (slotIndex < this.inventory.getItems().size) {
            this.selectedSlotItem = this.inventory.getItems().get(slotIndex);
        }
        this.stage.draw();
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