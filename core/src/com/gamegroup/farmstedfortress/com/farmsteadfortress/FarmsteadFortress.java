package com.farmsteadfortress;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.FxaaEffect;
import com.crashinvaders.vfx.effects.MotionBlurEffect;
import com.crashinvaders.vfx.effects.util.MixEffect;
import com.farmsteadfortress.screens.GameScreen;
import com.farmsteadfortress.screens.MenuScreen;
import com.farmsteadfortress.screens.OptionsScreen;

public class FarmsteadFortress extends Game {
    private SpriteBatch batch;
    private VfxManager vfxManager;
    private FxaaEffect fxaaEffect;
    private MotionBlurEffect motionBlurEffect;
    private Preferences preferences;
    public static Music backgroundMusic;
    public static GameScreen gameScreen;
    public static MenuScreen menuScreen;
    public static OptionsScreen optionsScreen;


    @Override
    public void create() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/background_track.mp3"));
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        motionBlurEffect = new MotionBlurEffect(Pixmap.Format.RGBA8888, MixEffect.Method.MAX, .5f);
        vfxManager.addEffect(motionBlurEffect);
        fxaaEffect = new FxaaEffect();
        vfxManager.addEffect(fxaaEffect);

        batch = new SpriteBatch();
        optionsScreen = new OptionsScreen(this);
        gameScreen = new GameScreen(batch, this);
        menuScreen = new MenuScreen(this);
        setScreen(new MenuScreen(this));
    }

    public void initialisePreferences() {
        preferences = Gdx.app.getPreferences("MyPreferences");
    }

    public float getMusicVolume() {
        return preferences.getFloat("MusicVolume", 1.0f);
    }

    public void setMusicVolume(float volume) {
        preferences.putFloat("MusicVolume", volume);
        preferences.flush();
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(volume);
        }
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public float getSoundVolume() {
        return preferences.getFloat("SoundVolume", 1.0f);
    }

    public void setSoundVolume(float volume) {
        preferences.putFloat("SoundVolume", volume);
        preferences.flush();
    }

    public void playMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.play();
        }
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
