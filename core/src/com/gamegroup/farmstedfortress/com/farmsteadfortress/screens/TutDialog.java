package com.farmsteadfortress.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class TutDialog extends Dialog {
    private final String[] lines;
    private int currentLine;

    public TutDialog(String title, Skin skin) {
        super(title, skin);

        lines = new String[]{
                "Welcome to Farmstead Fortess.",
                "In this game you must protect the core of your island (indicated by the grey tile) from enemies by planting crops.",
                "These crops can be bought within the shop in the bottom right hand corner.",
                "Bought crops will appear in your hotbar and can be placed by selecting it from the hot bar and clicking where to place it on the field.",
                "You will only have a little bit of time before the enemies arrive so get ready and good luck!"
        };
        currentLine = 0;
        TextButton.TextButtonStyle style = skin.get(TextButton.TextButtonStyle.class);
        TextButton nextButton = new TextButton("Next", style);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextLine();
            }
        });
        text(lines[currentLine]);
        button(nextButton);
    }

    private void nextLine() {
        if (currentLine < lines.length - 1) {
            currentLine++;
            getContentTable().clear();
            text(lines[currentLine]);
        } else {
            this.hide();
        }
    }

    @Override
    protected void result(Object object) {
        this.hide();
    }
}
