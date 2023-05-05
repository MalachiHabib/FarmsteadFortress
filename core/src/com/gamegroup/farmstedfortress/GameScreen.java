package com.gamegroup.farmstedfortress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamegroup.farmstedfortress.render.Tile;

public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Tile map;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
        camera = new OrthographicCamera(1280, 720);
        //map = new Tile();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        cameraInput();
        camera.update();

        batch.begin();

        map.render(batch);
        batch.end();
    }

    public void cameraInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.position.x -=1;
        }
    }
}
