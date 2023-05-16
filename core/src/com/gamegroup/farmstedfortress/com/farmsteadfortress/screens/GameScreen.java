package com.farmsteadfortress.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.farmsteadfortress.entities.Enemy;
import com.farmsteadfortress.entities.EnemyFactory;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.entities.PlayerFactory;
import com.farmsteadfortress.input.InputHandler;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;

public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private TileMap map;
    private Enemy enemy;
    private Player player;
    private InputHandler inputHandler;
    private ShapeRenderer shapeRenderer;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
        camera = new OrthographicCamera(1920, 1080);
        camera.zoom = 1.5f;
        map = new TileMap();
        calculateCameraPosition();

        enemy = EnemyFactory.createEnemy();
        enemy.setPath(map);

        player = PlayerFactory.createPlayer(map.getCenterTilePos(), map);
        inputHandler = new InputHandler(map, camera, player, enemy);
        Gdx.input.setInputProcessor(inputHandler);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        updateCamera();
        inputHandler.update();
        inputHandler.handleCameraInput();
        enemy.update(delta, map);
        player.update(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin();
        map.render(batch);
        enemy.render(batch);
        player.render(batch);
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // Draw shapes using shapeRenderer
        shapeRenderer.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f); // Set your desired background color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void updateCamera() {
        camera.update();
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

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }
}