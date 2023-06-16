package com.farmsteadfortress.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;
import com.farmsteadfortress.ui.ShopUI;

import java.util.List;

public class Helpers {

    /**
     * Converts grid coordinates to world position based on the tile dimensions.
     *
     * @param gridX      The x-coordinate in the grid.
     * @param gridY      The y-coordinate in the grid.
     * @param tileWidth  The width of a tile in world units.
     * @param tileHeight The height of a tile in world units.
     * @return The world position as a Vector2.
     */
    public static Vector2 gridToWorldPosition(int gridX, int gridY, float tileWidth, float tileHeight) {
        float worldX = (gridX - gridY) * tileWidth / 2;
        float worldY = (gridX + gridY) * tileHeight / 2;
        return new Vector2(worldX, worldY);
    }

    /**
     * Converts world position to grid coordinates based on the tile dimensions.
     *
     * @param worldX The x-coordinate in world units.
     * @param worldY The y-coordinate in world units.
     * @return An int array containing the grid coordinates [gridX, gridY].
     */
    public static int[] worldToGridPosition(float worldX, float worldY) {
        int gridX = Math.round((worldX / Tile.TILE_SIZE - worldY / Tile.TILE_SIZE) / 2);
        int gridY = Math.round((worldX / Tile.TILE_SIZE + worldY / Tile.TILE_SIZE) / 2);
        return new int[]{gridX, gridY};
    }

    /**
     * Retrieves the tile at the given screen coordinates from the provided TileMap.
     *
     * @param tileMap The TileMap object to retrieve the tile from.
     * @param camera  The OrthographicCamera used for the screen-to-world coordinate conversion.
     * @param screenX The X-coordinate on the screen.
     * @param screenY The Y-coordinate on the screen.
     * @return The Tile object at the specified coordinates, or null if no tile is found.
     */
    public static Tile getTileAtPosition(TileMap tileMap, OrthographicCamera camera, int screenX, int screenY) {
        Vector3 unprojected = new Vector3(screenX, screenY, 0);
        camera.unproject(unprojected);
        Vector2 clickedWorldPosition = new Vector2(unprojected.x, unprojected.y);
        Tile clickedTile = tileMap.getTileAt(clickedWorldPosition);
        return clickedTile;
    }

    public static boolean isDesktop() {
        return Gdx.app.getType() == Application.ApplicationType.Desktop;
    }

    public static boolean isMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android ||
                Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    public static boolean uiContains(List<Stage> uiStages, int screenX, int screenY, ShopUI shop) {
        for (Stage stage : uiStages) {
            Vector3 stageCoordinates = new Vector3(screenX, screenY, 0);
            stage.getCamera().unproject(stageCoordinates);
            Actor hitActor = stage.hit(stageCoordinates.x, stageCoordinates.y, true);
            if (hitActor != null) {
                if (hitActor.getName() != null && hitActor.getName().equals("shopActor") && !shop.isOpen()) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public static float getScaleFactor() {
        float screenWidth = Gdx.graphics.getWidth();
        if (screenWidth < 801) {
            return 0.5f;
        } else if (screenWidth < 1601) {
            return 0.75f;
        } else {
            return 1f;
        }
    }

    public static void showDialog(Stage stage, String title, String message) {
        Skin skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        final Dialog dialog = new Dialog(title, skin);
        dialog.text(message);
        dialog.button("OK").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.remove();
            }
        });
        dialog.show(stage);
    }
}