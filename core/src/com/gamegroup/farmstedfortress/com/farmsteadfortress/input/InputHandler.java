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
        Tile currentTile = getTileAtPosition(screenX, screenY);
        if (currentTile != null) {
            if (currentTile.isInteractable()) {
                if (currentTile != lastHoveredTile) {
                    if (lastHoveredTile != null) {
                        lastHoveredTile.setTileTexture(lastHoveredTile.getOriginalTileTexture());
                    }

                    if (currentTile != null) {
                        lastHoveredTile = currentTile;
                        currentTile.setTileTexture(hoverTexture);
                    } else {
                        lastHoveredTile = null;
                    }
                }
            } else if (lastHoveredTile != null) {
                lastHoveredTile.setTileTexture(lastHoveredTile.getOriginalTileTexture());
                lastHoveredTile = null;
            }
        }
        return super.mouseMoved(screenX, screenY);
    }


    private Tile getTileAtPosition(int screenX, int screenY) {
        Vector3 unprojected = new Vector3(screenX, screenY, 0);
        camera.unproject(unprojected);
        Vector2 clickedWorldPosition = new Vector2(unprojected.x, unprojected.y);
        Tile clickedTile = tileMap.getTileAt(clickedWorldPosition);
        return clickedTile;
    }
}
