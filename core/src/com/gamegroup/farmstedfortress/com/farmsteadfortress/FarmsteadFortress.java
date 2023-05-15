package com.farmsteadfortress;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.BloomEffect;
import com.crashinvaders.vfx.effects.FxaaEffect;
import com.crashinvaders.vfx.effects.MotionBlurEffect;
import com.crashinvaders.vfx.effects.VignettingEffect;
import com.crashinvaders.vfx.effects.util.MixEffect;
import com.farmsteadfortress.screens.GameScreen;
import com.farmsteadfortress.screens.MenuScreen;

public class FarmsteadFortress extends Game {
    private SpriteBatch batch;
    private VfxManager vfxManager;
    private VignettingEffect vignettingEffect;
    private FxaaEffect fxaaEffect;
    private MotionBlurEffect motionBlurEffect;

    public static GameScreen screen;
    public static MenuScreen menuScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        screen = new GameScreen(batch);
        menuScreen = new MenuScreen(this);
        setScreen(screen);

        batch = new SpriteBatch();
        setScreen(new GameScreen(batch));

        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);

        vignettingEffect = new VignettingEffect(false);
        vignettingEffect.setIntensity(0.5f);
        vfxManager.addEffect(vignettingEffect);

        motionBlurEffect = new MotionBlurEffect(Pixmap.Format.RGBA8888, MixEffect.Method.MAX, .5f);
        vfxManager.addEffect(motionBlurEffect);

        fxaaEffect = new FxaaEffect();
        vfxManager.addEffect(fxaaEffect);
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
