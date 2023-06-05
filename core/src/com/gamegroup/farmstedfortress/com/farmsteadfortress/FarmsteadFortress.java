package com.farmsteadfortress;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.FxaaEffect;
import com.crashinvaders.vfx.effects.MotionBlurEffect;
import com.crashinvaders.vfx.effects.util.MixEffect;
import com.farmsteadfortress.screens.GameScreen;
import com.farmsteadfortress.screens.MenuScreen;

public class FarmsteadFortress extends Game {
    private SpriteBatch batch;
    private VfxManager vfxManager;
    private FxaaEffect fxaaEffect;
    private MotionBlurEffect motionBlurEffect;

    public static GameScreen screen;
    public static MenuScreen menuScreen;

    @Override
    public void create() {
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        motionBlurEffect = new MotionBlurEffect(Pixmap.Format.RGBA8888, MixEffect.Method.MAX, .5f);
        vfxManager.addEffect(motionBlurEffect);
        fxaaEffect = new FxaaEffect();
        vfxManager.addEffect(fxaaEffect);

        batch = new SpriteBatch();
        screen = new GameScreen(batch);
        menuScreen = new MenuScreen(this);
        //setScreen(new GameScreen(batch));

        setScreen(new MenuScreen(this));

    }

    @Override
    public void render() {
        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();
        super.render();
        vfxManager.endInputCapture();
        vfxManager.applyEffects();
        vfxManager.renderToScreen();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        vfxManager.dispose();
    }
}