package com.farmsteadfortress.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;

public class InputHandler extends InputAdapter {
    private TileMap tileMap;
    private OrthographicCamera camera;
    private Tile lastHoveredTile;
    private Texture hoverTexture;

    public InputHandler(TileMap tileMap, OrthographicCamera camera) {
        hoverTexture = new Texture("tiles/highlight.png");
        this.tileMap = tileMap;
        this.camera = camera;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        updateHoverEffect(screenX, screenY);
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        replaceTile(screenX, screenY, new Texture("tiles/middle.png"));
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        replaceTile(screenX, screenY, new Texture("tiles/middle.png"));
        updateHoverEffect(screenX, screenY);
        return super.touchDragged(screenX, screenY, pointer);
    }

    private void updateHoverEffect(int screenX, int screenY) {
        Tile currentTile = getTileAtPosition(screenX, screenY);
        if (currentTile != null) {
            if (currentTile.isInteractable()) {
                if (currentTile != lastHoveredTile) {
                    if (lastHoveredTile != null) {
                        lastHoveredTile.setHoverTexture(lastHoveredTile.getOriginalTileTexture());
                    }

                    if (currentTile != null) {
                        lastHoveredTile = currentTile;
                        currentTile.setHoverTexture(hoverTexture);
                    } else {
                        lastHoveredTile = null;
                    }
                }
            } else if (lastHoveredTile != null) {
                lastHoveredTile.setHoverTexture(lastHoveredTile.getOriginalTileTexture());
                lastHoveredTile = null;
            }
        }
    }
    public void replaceTile(int screenX, int screenY, Texture newTileTexture) {
        Tile clickedTile = getTileAtPosition(screenX, screenY);
        if (clickedTile != null && clickedTile.isInteractable()) {
            clickedTile.setTileTexture(newTileTexture);
        }
    }

    private Tile getTileAtPosition(int screenX, int screenY) {
        Vector3 unprojected = new Vector3(screenX, screenY, 0);
        camera.unproject(unprojected);
        Vector2 clickedWorldPosition = new Vector2(unprojected.x, unprojected.y);
        Tile clickedTile = tileMap.getTileAt(clickedWorldPosition);
        return clickedTile;
    }
}