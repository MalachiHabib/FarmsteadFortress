package com.farmsteadfortress.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
    private Stage stage;
    private Skin skin;
    private Table table;
    private Array<ImageButton> buttons;
    private ImageButton highlightedButton;
    private TextureRegionDrawable highlightDrawable;
    private Drawable defaultDrawable;
    private Inventory inventory;
    private Item selectedSlotItem;

    public Hotbar(Inventory inventory) {
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        this.table = new Table();
        this.buttons = new Array<>();
        defaultDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-slot.png")));
        highlightDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-slot-highlighted.png")));
        this.inventory = inventory;
        table.setFillParent(true);
        stage.addActor(table);
        createHotbar();
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
        for (int i = 0; i < 9; i++) {
            final ImageButton button = new ImageButton(defaultDrawable);
            final ImageButton highlightButton = new ImageButton(highlightDrawable);
            highlightButton.setVisible(false);
            final Stack stack = new Stack();
            stack.add(button);
            stack.add(highlightButton);
            buttons.add(button);
            table.add(stack).size(100, 100).pad(10);

            if (i < inventory.getItems().size) {
                Item item = inventory.getItems().get(i);
                Drawable itemDrawable = new TextureRegionDrawable(item.getTexture());
                button.getStyle().imageUp = itemDrawable;
            }

            final int slotIndex = i;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Clicked hotbar slot: " + slotIndex);
                    if (highlightedButton != null) {
                        // Set the second ImageButton in the Stack to invisible
                        ((Stack) highlightedButton.getParent()).getChildren().get(1).setVisible(false);
                    }
                    highlightedButton = button;
                    stack.getChildren().get(1).setVisible(true);
                    if (slotIndex < inventory.getItems().size) {
                        selectedSlotItem = inventory.getItems().get(slotIndex);
                    } else {
                        selectedSlotItem = null;
                    }
                    render();
                }
            });
        }
        table.bottom();
    }

    public Item getSelectedSlotItem() {
        return selectedSlotItem;
    }

    public void render() {
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