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
        camera.zoom = 1.5f;
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

        float offsetX = 50; // Adjust this value to change the horizontal shift
        float offsetY = 30; // Adjust this value to change the vertical shift
        camera.position.set(centerX + offsetX, centerY + offsetY, 0);
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
        System.out.println(camera.position.x);
        System.out.println(camera.position.y);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.position.x -= 3f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.position.x += 3f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.position.y += 3f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.position.y -= 3f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            if (camera.zoom > 0.55) camera.zoom -= .1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            if (camera.zoom < 1.25) camera.zoom += .1;
        }
    }


}
