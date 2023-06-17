package com.farmsteadfortress;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.BloomEffect;
import com.crashinvaders.vfx.effects.FxaaEffect;
import com.crashinvaders.vfx.effects.LensFlareEffect;
import com.crashinvaders.vfx.effects.MotionBlurEffect;
import com.crashinvaders.vfx.effects.VignettingEffect;
import com.crashinvaders.vfx.effects.util.MixEffect;
import com.farmsteadfortress.screens.CreditsScreen;
import com.farmsteadfortress.screens.GameScreen;
import com.farmsteadfortress.screens.MenuScreen;
import com.farmsteadfortress.screens.OptionsScreen;

public class FarmsteadFortress extends Game {
    private SpriteBatch batch;
    private VfxManager vfxManager;
    private FxaaEffect fxaaEffect;
    private MotionBlurEffect motionBlurEffect;
    private Preferences preferences;
    private VignettingEffect vignettingEffect;
    private BloomEffect bloomEffect;
    public static Music backgroundMusic;
    public static GameScreen gameScreen;
    public static MenuScreen menuScreen;
    public static OptionsScreen optionsScreen;
    public static CreditsScreen creditsScreen;


    @Override
    public void create() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/background_track.mp3"));
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        motionBlurEffect = new MotionBlurEffect(Pixmap.Format.RGBA8888, MixEffect.Method.MAX, .5f);
        vfxManager.addEffect(motionBlurEffect);
        fxaaEffect = new FxaaEffect();
        bloomEffect = new BloomEffect();
        bloomEffect.setBloomIntensity(.35f);
        vfxManager.addEffect(bloomEffect);
        vfxManager.addEffect(fxaaEffect);
        vignettingEffect = new VignettingEffect(false);
        vignettingEffect.setIntensity(.25f);
        vfxManager.addEffect(vignettingEffect);
        batch = new SpriteBatch();
        optionsScreen = new OptionsScreen(this);
        creditsScreen = new CreditsScreen(this);
        gameScreen = new GameScreen(batch, this);
        menuScreen = new MenuScreen(this);
        setScreen(menuScreen);
    }

    public void initialisePreferences() {
        preferences = Gdx.app.getPreferences("MyPreferences");
    }

    public float getMusicVolume() {
        return preferences.getFloat("MusicVolume", 1.0f);
    }

    public float getSoundVolume() {
        return preferences.getFloat("SoundVolume", 1.0f);
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setMusicVolume(float volume) {
        preferences.putFloat("MusicVolume", volume);
        preferences.flush();
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(volume);
        }
    }

    public void setSoundVolume(float volume) {
        preferences.putFloat("SoundVolume", volume);
        preferences.flush();
    }

    public void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void restartGame() {
        gameScreen = new GameScreen(batch, this);
        setScreen(gameScreen);
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