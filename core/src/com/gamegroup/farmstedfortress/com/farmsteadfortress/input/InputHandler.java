package com.farmsteadfortress.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.farmsteadfortress.entities.Enemy;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.path.PathCalculator;
import com.farmsteadfortress.path.PathResult;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;

import java.util.List;

public class InputHandler extends InputAdapter {
    private TileMap tileMap;
    private OrthographicCamera camera;
    private Tile lastHoveredTile;
    private Texture hoverTexture;
    private Tile targetTile;
    private Player player;
    private Enemy enemy;
    private int clickedScreenX;
    private int clickedScreenY;


    public InputHandler(TileMap tileMap, OrthographicCamera camera, Player player, Enemy enemy) {
        hoverTexture = new Texture("tiles/highlight.png");
        this.tileMap = tileMap;
        this.camera = camera;
        this.player = player;
        this.enemy = enemy;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        updateHoverEffect(screenX, screenY);
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Transform the screen coordinates to world coordinates
        Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));
        Vector2 clickPosition = new Vector2(worldCoordinates.x, worldCoordinates.y);

        // Calculate path from player's current position to the clicked position
        int playerTileRow = (int) (player.getPosition().y / Tile.TILE_SIZE);
        int playerTileCol = (int) (player.getPosition().x / Tile.TILE_SIZE);
        int targetTileRow = (int) (clickPosition.y / Tile.TILE_SIZE);
        int targetTileCol = (int) (clickPosition.x / Tile.TILE_SIZE);

        PathCalculator pathCalculator = new PathCalculator();
        PathResult pathResult = pathCalculator.findPath(tileMap.getMap(), new int[]{playerTileRow, playerTileCol}, new int[]{targetTileRow, targetTileCol});

        // Print the path
        if (pathResult.isSuccess()) {
            System.out.println("Path:");
            List<int[]> path = pathResult.getPath();
            for (int[] point : path) {
                System.out.println("[" + point[0] + ", " + point[1] + "]");
            }
        } else {
            System.out.println("No path found.");
        }

        // Update the player's target position and path
        if (pathResult.isSuccess()) {
            player.setTargetPosition(clickPosition);
        }

        return true;
    }

    public void update() {
        if (player.hasReachedTarget() && targetTile != null) {
            System.out.println("Player has reached target, replacing tile...");
            targetTile.setTileTexture(new Texture("tiles/crop_land.png"));
        }
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