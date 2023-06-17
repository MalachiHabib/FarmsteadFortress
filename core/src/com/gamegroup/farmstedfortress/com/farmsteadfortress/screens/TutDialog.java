package com.farmsteadfortress.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class TutDialog extends Dialog {

    public TutDialog(String title, Skin skin) {
        super(title, skin);
    }

    public TutDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public TutDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);
    }

    {
        text("Welcome to Farmstead Fortess. \n" +
                "In this game you must protect the core of your island (indicated by the grey tile) from enemies by planting crops\n" +
                "These crops can be bought within the shop in the bottom right hand corner.\n" +
                "Bought crops will appear in your hotbar and can be placed by selecting it from the hot bar and clicking where to place it on the field.\n" +
                " You will only have a little bit of time before the enemies arrive so get ready and good luck!");
        //button("Exit");
    }

    @Override
    protected void result(Object object){
        this.hide();
    }

}
