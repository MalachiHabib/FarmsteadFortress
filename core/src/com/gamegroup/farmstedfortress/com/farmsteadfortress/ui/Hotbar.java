package com.farmsteadfortress.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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

        if (highlightedButton != null) {
            highlightedButton.getStyle().imageUp = highlightDrawable;
            highlightedButton = null;
        }
    }


    private void createHotbar() {
        defaultDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("gui/hotbar-slot.png")));

        for (int i = 0; i < 9; i++) {
            final ImageButton button = new ImageButton(defaultDrawable);
            buttons.add(button);
            table.add(button).size(100, 100).pad(10);

            // Populate the hotbar slot with inventory item, if available
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
                    highlightedButton = button;
                    render();
                }
            });
        }
        table.bottom();
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