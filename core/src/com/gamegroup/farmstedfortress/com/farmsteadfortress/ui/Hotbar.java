package com.farmsteadfortress.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
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
import com.farmsteadfortress.waves.WaveController;

public class Hotbar {
    private final Stage stage;
    private final Table table;
    private final Array<ImageButton> buttons;
    private final Inventory inventory;
    private final Table circleTable;
    private final Array<ImageButton> circleButtons;
    private final ShopUI shop;
    public ImageButton highlightedButton;
    public Item selectedSlotItem;
    public ImageButton highlightedCircleButton;
    private TextureRegionDrawable highlightDrawable;
    private Drawable defaultDrawable;
    private Texture bannerTexture;
    private Drawable circleDefaultDrawable;
    private Drawable circleHighlightDrawable;
    private boolean isShopOpen = false;
    private int lastInventorySize = 0;
    private WaveController waveController;


    public Hotbar(Inventory inventory, ShopUI shop) {
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

    public void setWaveController(WaveController waveController) {
        this.waveController = waveController;
    }

    private void initTextures() {
        // Load textures
        bannerTexture = new Texture(Gdx.files.internal("gui/hotbar-bg.png"));
        circleDefaultDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-slot-circle.png")));
        circleHighlightDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-slot-circle-highlighted.png")));
        defaultDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-slot.png")));
        highlightDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-slot-highlighted.png")));

        // Create bannerImage and get its width
        Image bannerImage = new Image(bannerTexture);
        float bannerWidth = bannerImage.getWidth();
        float bannerHeight = Gdx.graphics.getHeight() / 8.5f;
        bannerImage.setPosition(Gdx.graphics.getWidth() - bannerWidth, 0); // Position in bottom-right corner

        // Create a Container for the buttons and add bannerImage to it as background
        Container<Table> container = new Container<>();
        container.setBackground(bannerImage.getDrawable());
        container.setSize(bannerWidth, bannerHeight);
        container.setPosition(Gdx.graphics.getWidth() - bannerWidth, 0);

        // Create parent table that will contain both original and circle tables
        Table parentTable = new Table();
        parentTable.setFillParent(true);

        // Add tables to the parent table
        table.setFillParent(false);
        circleTable.setFillParent(false);
        parentTable.add(table).expandX().left(); // original table on the left
        parentTable.add(circleTable).expandX().right(); // circle table on the right

        // Set parent table as the actor of the container
        container.setActor(parentTable);

        // Add container to the stage
        stage.addActor(container);
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
        table.right().bottom();
        for (int i = 0; i < 5; i++) {
            final ImageButton button = new ImageButton(defaultDrawable);
            final ImageButton highlightButton = new ImageButton(highlightDrawable);
            highlightButton.setVisible(false);
            final Stack stack = new Stack();
            stack.add(button);
            stack.add(highlightButton);
            buttons.add(button);
            table.add(stack).size(75, 75).padRight(5).padTop(7.5f);

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
        createCircleHotbar();
    }

    private void createCircleHotbar() {
        circleTable.right().bottom().padTop(8);
        for (int i = 0; i < 2; i++) {
            final ImageButton button;
            if (i == 0) {
                Drawable playDefaultDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-play.png")));
                Drawable playPressedDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-play-pressed.png")));
                button = new ImageButton(playDefaultDrawable, playPressedDrawable);
            } else {
                Drawable shopDefaultDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-shop.png")));
                Drawable shopPressedDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-shop-pressed.png")));
                button = new ImageButton(shopDefaultDrawable, shopPressedDrawable);
            }

            final ImageButton highlightButton = new ImageButton(circleHighlightDrawable);
            highlightButton.setVisible(false);
            final Stack stack = new Stack();
            stack.add(button);
            stack.add(highlightButton);
            circleButtons.add(button);

            if (i == 0) {
                circleTable.add(stack).size(90, 90).padRight(10).padTop(1.5f);
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (waveController != null) {
                            waveController.startWave();
                        }
                    }
                });
            } else {
                circleTable.add(stack).size(90, 90).padRight(50);
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        toggleShop();
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