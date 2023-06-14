package com.farmsteadfortress.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.crashinvaders.vfx.effects.VignettingEffect;
import com.farmsteadfortress.FarmsteadFortress;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.entities.PlayerFactory;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.input.InputHandler;
import com.farmsteadfortress.projectiles.ProjectileManager;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;
import com.farmsteadfortress.ui.HUD;
import com.farmsteadfortress.ui.Hotbar;
import com.farmsteadfortress.ui.ShopUI;
import com.farmsteadfortress.ui.WaveOverUI;
import com.farmsteadfortress.waves.WaveController;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends ScreenAdapter {
    private Music backgroundMusic;
    private SpriteBatch batch;
    private SpriteBatch tutBatch;
    private OrthographicCamera camera;
    private TileMap map;
    private List<Enemy> enemies;
    private Player player;
    private InputHandler inputHandler;
    private ShapeRenderer shapeRenderer;
    private InputMultiplexer inputMultiplexer;
    private Hotbar hotbar;
    private ShopUI shop;
    private ArrayList<Stage> uiStages;
    private WaveController waveController;
    private VignettingEffect vignettingEffect;
    private HUD hud;
    private WaveOverUI waveOverUI;
    private BitmapFont bmfont;
    private ProjectileManager projectileManager;
    private GameOverScreen gameOverScreen;
    private FarmsteadFortress game;

    public GameScreen(SpriteBatch batch, FarmsteadFortress game) {
        this.gameOverScreen = new GameOverScreen();
        this.batch = batch;
        this.game = game;
        tutBatch = new SpriteBatch();
        camera = new OrthographicCamera(1920, 1080);
        camera.zoom = 1.5f;
        map = new TileMap();
        this.projectileManager = new ProjectileManager();
        calculateCameraPosition();

        player = PlayerFactory.createPlayer(map.getCenterTilePos(), map);
        this.enemies = new ArrayList<>();
        waveController = new WaveController(enemies, player);

        uiStages = new ArrayList<>();
        hotbar = new Hotbar(player.getInventory(), shop);
        shop = new ShopUI(hotbar, player);
        hud = new HUD();
        hotbar = new Hotbar(player.getInventory(), shop);
        waveOverUI = new WaveOverUI();
        hotbar.setWaveController(waveController);
        inputMultiplexer = new InputMultiplexer();
        shapeRenderer = new ShapeRenderer();
        inputHandler = new InputHandler(map, camera, player, enemies, inputMultiplexer, hotbar, shop);

        uiStages.add(hotbar.getStage());
        uiStages.add(shop.getStage());
        uiStages.add(hud.getStage());
        inputHandler.setUiStages(uiStages);
        inputHandler.setProjectileManager(projectileManager);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputHandler);
        inputMultiplexer.addProcessor(hotbar.getStage());


        shapeRenderer.setAutoShapeType(true);

        // Initialise the vignetting effect
        vignettingEffect = new VignettingEffect(false);
        vignettingEffect.setIntensity(0.5f);

        /*
        BitmapFont sourced from
        https://www.dafont.com/lilian-2.font?back=bitmap
        follow link for more details
         */
        bmfont = new BitmapFont(
                Gdx.files.internal("gui/Lilian.fnt"),
                Gdx.files.internal("gui/Lilian.png"),
                false
        );
    }

    @Override
    public void render(float delta) {
        waveController.update(delta);

        if (shop.isOpen()) {
            inputMultiplexer.addProcessor(shop.getStage());
        }
        hotbar.updateHotbar();
        clearScreen();
        updateCamera();
        inputHandler.update();
        inputHandler.handleCameraInput();

        projectileManager.update(delta);
        for (Enemy enemy : enemies) {
            enemy.update(delta, map);
        }

        player.update(delta);
        updatePlants(delta, enemies);
        batch.setProjectionMatrix(camera.combined);
        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin();
        map.render(batch);
        projectileManager.render(batch);
        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }

        player.render(batch);
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.end();

        // Render the UI elements
        hotbar.render();
        shop.render();
        hotbar.render();
        hud.render();
        hud.updateLivesCount(player);
        hud.updatePlayerBalance(player);
        hud.updateWaveCount(waveController.getCurrentWave());

        if (waveController.isWaveOver() && waveController.getCurrentWave().getWaveNumber() != 0) {
            waveOverUI.render(delta);
            waveController.stopWave();
        }

        if (waveController.gameOver() || player.isDead()) {
            Gdx.input.setInputProcessor(null); // remove the focus from the current screen
            ((Game)Gdx.app.getApplicationListener()).setScreen(gameOverScreen); // switch to the game over screen
        }

        // Tutorial
//        tutBatch.begin();
//        bmfont.setColor(1, 1, 1, 1);
//        String tutorialText = "Welcome to Farmstead Fortess. \n" +
//                "In this game you must protect the core of your island (indicated by the grey tile) from enemies by planting crops\n" +
//                "These crops can be bought within the shop in the bottom right hand corner.\n" +
//                "Bought crops will appear in your hotbar and can be placed by selecting it from the hot bar and clicking where to place it on the field.\n" +
//                " You will only have a little bit of time before the enemies arrive so get ready and good luck!";
//        bmfont.draw(tutBatch, tutorialText, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0f, 1, false );
//        tutBatch.end();
    }


    private void updatePlants(float delta, List<Enemy> enemies) {
        for (Tile tile : map.getBaseTiles()) {
            tile.update(delta, enemies);
        }
    }

    private void calculateCameraPosition() {
        float centerX = 0;
        float centerY = 0;
        for (Tile tile : map.getBaseTiles()) {
            centerX += tile.getPosition().x;
            centerY += tile.getPosition().y;
        }
        centerX /= map.getBaseTiles().size();
        centerY /= map.getBaseTiles().size();
        float offsetX = 50;
        float offsetY = 30;
        camera.position.set(centerX + offsetX, centerY + offsetY, 0);
    }

    private void playMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/game_track.mp3"));
        backgroundMusic.play();
        backgroundMusic.setVolume(game.getMusicVolume());
    }

    private void updateCamera() {
        camera.update();
    }

    @Override
    public void dispose() {
        backgroundMusic.dispose();
        super.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        hotbar.dispose();
        shop.dispose();
        hud.dispose();
        bmfont.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void show() {
        playMusic();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void hide() {
        backgroundMusic.pause();
        Gdx.input.setInputProcessor(null);
    }
}