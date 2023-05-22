package com.farmsteadfortress.screens;

import static com.farmsteadfortress.entities.enemies.EnemyFactory.EnemyType.BASIC_ENEMY;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.entities.PlayerFactory;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.input.InputHandler;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;
import com.farmsteadfortress.ui.Health;
import com.farmsteadfortress.ui.Hotbar;
import com.farmsteadfortress.ui.MoneyDisplay;
import com.farmsteadfortress.ui.Shop;
import com.farmsteadfortress.ui.SpawnWaveUI;
import com.farmsteadfortress.waves.WaveController;

import java.util.ArrayList;
import java.util.Iterator;
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
    private Shop shop;
    private ArrayList<Stage> uiStages;
    private MoneyDisplay moneyDisplay;
    private WaveController waveController;
    private SpawnWaveUI spawnWaveUI;
    private Health health;

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
        shop = new Shop(hotbar, player);
        hotbar = new Hotbar(player.getInventory(), shop);
        moneyDisplay = new MoneyDisplay();
        health = new Health();

        spawnWaveUI = new SpawnWaveUI(waveController);
        inputMultiplexer = new InputMultiplexer();
        shapeRenderer = new ShapeRenderer();
        inputHandler = new InputHandler(map, camera, player, enemies, inputMultiplexer, hotbar);

        uiStages.add(spawnWaveUI.getStage());
        uiStages.add(hotbar.getStage());
        uiStages.add(shop.getStage());
        inputHandler.setUiStages(uiStages);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(spawnWaveUI.getStage());
        inputMultiplexer.addProcessor(inputHandler);
        inputMultiplexer.addProcessor(hotbar.getStage());
        inputMultiplexer.addProcessor(shop.getStage());
        Gdx.input.setInputProcessor(inputMultiplexer);

        shapeRenderer.setAutoShapeType(true);
    }

    @Override
    public void render(float delta) {
        moneyDisplay.update(player);
        health.update(player);
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
        hotbar.render();
        shop.render();
        hotbar.render();
        moneyDisplay.render();
        health.render();
        spawnWaveUI.render();
        spawnWaveUI.updateWaveNumber(waveController.getCurrentWave());
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
        moneyDisplay.dispose();
        spawnWaveUI.getStage().dispose();
        health.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}