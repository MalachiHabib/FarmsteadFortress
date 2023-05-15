package com.farmsteadfortress.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.farmsteadfortress.entities.Enemy;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.path.PathCalculator;
import com.farmsteadfortress.path.PathResult;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;
import com.farmsteadfortress.utils.Helpers;

public class InputHandler extends InputAdapter {
    private TileMap tileMap;
    private OrthographicCamera camera;
    private Texture hoverTexture;
    private Player player;
    private Enemy enemy;
    private PathCalculator pathCalculator;
    private PathResult pathResult;
    private Tile lastHoveredTile;
    private Tile targetTile;
    private int clickedScreenX;
    private int clickedScreenY;

    public InputHandler(TileMap tileMap, OrthographicCamera camera, Player player, Enemy enemy) {
        hoverTexture = new Texture("tiles/highlight.png");
        this.tileMap = tileMap;
        this.camera = camera;
        this.player = player;
        this.enemy = enemy;
        this.pathCalculator = new PathCalculator();
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        updateHoverEffect(screenX, screenY);
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Tile playerTile = tileMap.getTileAt(player.getPosition());
        if (playerTile != null) {
            Tile clickedTile = Helpers.getTileAtPosition(tileMap, camera, screenX, screenY);
            if (clickedTile != null && clickedTile.isIntractable()) {
                if (player.hasReachedTarget() || targetTile == null || !targetTile.equals(clickedTile)) {
                    targetTile = clickedTile;
                    Tile startTile = playerTile;
                    int[] startTilePos = new int[]{(int) startTile.tileMapPos.x + 2, (int) startTile.tileMapPos.y};
                    int[] endTilePos = new int[]{(int) targetTile.tileMapPos.x, (int) targetTile.tileMapPos.y};
                    pathCalculator.clearTerrainWeights();
                    pathCalculator.setTerrainWeight("W", Double.POSITIVE_INFINITY);
                    pathResult = pathCalculator.findPath(tileMap.getMap(), startTilePos, endTilePos);

                    if (pathResult.isSuccess()) {
                        player.setPath(pathResult.getPath());
                    }
                }
                clickedScreenX = screenX;
                clickedScreenY = screenY;
            }
        }

        Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));
        Vector2 touchPosition = new Vector2(worldCoordinates.x, worldCoordinates.y);

        if (enemy.containsPoint(touchPosition)) {
            enemy.onClick();
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        updateHoverEffect(screenX, screenY);
        return super.touchDragged(screenX, screenY, pointer);
    }

    public void update() {
        if (player.hasReachedTarget() && targetTile != null
                && pathResult.isSuccess()) {
            targetTile.setTileTexture(new Texture("tiles/crop_land.png"));
        }
    }

    private void updateHoverEffect(int screenX, int screenY) {
        Tile currentTile = Helpers.getTileAtPosition(tileMap, camera, screenX, screenY);
        if (currentTile != null) {
            if (currentTile.isIntractable()) {
                if (currentTile != lastHoveredTile) {
                    if (lastHoveredTile != null) {
                        lastHoveredTile.setHoverTexture(lastHoveredTile.getOriginalTileTexture());
                    }

                    lastHoveredTile = currentTile;
                    currentTile.setHoverTexture(hoverTexture);
                }
            } else {
                if (lastHoveredTile != null) {
                    lastHoveredTile.setHoverTexture(lastHoveredTile.getOriginalTileTexture());
                    lastHoveredTile = null;
                }
            }
        } else {
            if (lastHoveredTile != null) {
                lastHoveredTile.setHoverTexture(lastHoveredTile.getOriginalTileTexture());
                lastHoveredTile = null;
            }
        }
    }
}
