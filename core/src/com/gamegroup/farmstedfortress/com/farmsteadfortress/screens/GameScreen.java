package com.farmsteadfortress.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.crashinvaders.vfx.effects.VignettingEffect;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.entities.PlayerFactory;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.input.InputHandler;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;
import com.farmsteadfortress.ui.HUD;
import com.farmsteadfortress.ui.Hotbar;
import com.farmsteadfortress.ui.ShopUI;
import com.farmsteadfortress.waves.WaveController;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
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
    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
        camera = new OrthographicCamera(1920, 1080);
        camera.zoom = 1.5f;
        map = new TileMap();
        calculateCameraPosition();

        this.enemies = new ArrayList<>();
        waveController = new WaveController(enemies);
        player = PlayerFactory.createPlayer(map.getCenterTilePos(), map);

        uiStages = new ArrayList<>();
        hotbar = new Hotbar(player.getInventory(), shop);
        shop = new ShopUI(hotbar, player);
        hud = new HUD();
        hotbar = new Hotbar(player.getInventory(), shop);
        hotbar.setWaveController(waveController);
        inputMultiplexer = new InputMultiplexer();
        shapeRenderer = new ShapeRenderer();
        inputHandler = new InputHandler(map, camera, player, enemies, inputMultiplexer, hotbar, shop);

        uiStages.add(hotbar.getStage());
        uiStages.add(shop.getStage());
        uiStages.add(hud.getStage());
        inputHandler.setUiStages(uiStages);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputHandler);
        inputMultiplexer.addProcessor(hotbar.getStage());
        Gdx.input.setInputProcessor(inputMultiplexer);

        shapeRenderer.setAutoShapeType(true);

        // Initialize the vignetting effect
        vignettingEffect = new VignettingEffect(false);
        vignettingEffect.setIntensity(0.5f);
    }

    @Override
    public void render(float delta) {
        waveController.update(delta);
        if (waveController.isWaveOver()) {
            waveController.stopWave();
        }
        if (shop.isOpen()) {
            inputMultiplexer.addProcessor(shop.getStage());
        }
        hotbar.updateHotbar();
        clearScreen();
        updateCamera();
        inputHandler.update();
        inputHandler.handleCameraInput();

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

    private void updateCamera() {
        camera.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        hotbar.dispose();
        shop.dispose();
        hud.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}