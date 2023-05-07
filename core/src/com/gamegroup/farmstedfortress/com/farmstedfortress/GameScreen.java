package com.farmstedfortress;

import static com.badlogic.gdx.Gdx.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.farmstedfortress.render.Tile;
import com.farmstedfortress.render.TileMap;

public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private TileMap map;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
        camera = new OrthographicCamera(1280, 720);
        map = new TileMap();

        // Set camera position to center of island
        float centerX = 0;
        float centerY = 0;
        for (Tile tile : map.getBaseTiles()) {
            centerX += tile.getPosition().x;
            centerY += tile.getPosition().y;
        }
        centerX /= map.getBaseTiles().size();
        centerY /= map.getBaseTiles().size();
        camera.position.set(centerX, centerY, 0);
    }

    @Override
    public void render(float delta) {
        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        cameraInput();
        camera.update();

        batch.begin();

        map.render(batch);
        batch.end();
    }

    public void cameraInput() {
        float zoom = camera.zoom;
        float maxMoveDistance = 150f * zoom;
        float minCameraX = -105f;
        float maxCameraX = 135f;
        float minCameraY = 950f;
        float maxCameraY = 980f;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (camera.position.x > minCameraX) camera.position.x -= Math.min(maxMoveDistance, 3f * zoom);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (camera.position.x < maxCameraX) camera.position.x += Math.min(maxMoveDistance, 3f * zoom);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (camera.position.y < maxCameraY) camera.position.y += Math.min(maxMoveDistance, 3f * zoom);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (camera.position.y > minCameraY) camera.position.y -= Math.min(maxMoveDistance, 3f * zoom);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            if (camera.zoom > 0.55) camera.zoom -= .1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            if (camera.zoom < 1.25) camera.zoom += .1;
        }
    }


}
