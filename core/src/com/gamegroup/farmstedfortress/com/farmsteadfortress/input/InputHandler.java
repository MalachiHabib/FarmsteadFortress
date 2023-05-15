package com.farmsteadfortress.input;

import static com.farmsteadfortress.utils.Helpers.getTileAtPosition;
import static com.farmsteadfortress.utils.Helpers.isDesktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.farmsteadfortress.entities.Enemy;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.path.PathCalculator;
import com.farmsteadfortress.path.PathResult;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;


public class InputHandler extends InputAdapter implements GestureDetector.GestureListener, InputProcessor {
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

    private boolean isPanning;
    private boolean isZooming;

    private float panSpeed = 0.1f;
    private float zoomSpeed = 0.1f;

    public InputHandler(TileMap tileMap, OrthographicCamera camera, Player player, Enemy enemy) {
        hoverTexture = new Texture("tiles/highlight.png");
        this.tileMap = tileMap;
        this.camera = camera;
        this.player = player;
        this.enemy = enemy;
        this.pathCalculator = new PathCalculator();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(this));
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);

        if (isDesktop()) {
            panSpeed = 10f;
        }
    }

    public void handleCameraInput() {
        if (Gdx.input.isTouched(0) && Gdx.input.isTouched(1)) {
            float deltaX = (Gdx.input.getDeltaX(0) + Gdx.input.getDeltaX(1)) * panSpeed;
            float deltaY = (Gdx.input.getDeltaY(0) + Gdx.input.getDeltaY(1)) * panSpeed;

            camera.position.x -= deltaX;
            camera.position.y += deltaY;
        }

        if (Gdx.input.isTouched(0) && Gdx.input.isTouched(1)) {
            float initialDistance = Vector2.dst(
                    Gdx.input.getX(0), Gdx.input.getY(0),
                    Gdx.input.getX(1), Gdx.input.getY(1));

            float currentDistance = Vector2.dst(
                    Gdx.input.getX(0) + Gdx.input.getDeltaX(0), Gdx.input.getY(0) + Gdx.input.getDeltaY(0),
                    Gdx.input.getX(1) + Gdx.input.getDeltaX(1), Gdx.input.getY(1) + Gdx.input.getDeltaY(1));

            float pinchScale = currentDistance / initialDistance;

            float targetZoom = camera.zoom / pinchScale;
            camera.zoom = MathUtils.lerp(camera.zoom, targetZoom, zoomSpeed);
        }

        // Keyboard movement
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.position.y += panSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.position.y -= panSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.position.x -= panSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.position.x += panSpeed;
        }

        // Limit the camera zoom within a specific range
        camera.zoom = MathUtils.clamp(camera.zoom, 1, 3);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        float zoomSpeed = 0.1f;
        float newZoom = camera.zoom + amountY * zoomSpeed;
        newZoom = MathUtils.clamp(newZoom, 1, 4);
        camera.zoom = newZoom;
        return true;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        updateHoverEffect(screenX, screenY);
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Gdx.input.isTouched(1)) {
            // More than one finger is touching the screen, so this is a multi-touch gesture
            return false;
        }
        if (!isPanning && !isZooming) {
            Tile playerTile = tileMap.getTileAt(player.getPosition());
            if (playerTile != null) {
                Tile clickedTile = getTileAtPosition(tileMap, camera, screenX, screenY);
                if (clickedTile != null && clickedTile.isIntractable()) {
                    if (player.hasReachedTarget() || targetTile == null || !targetTile.equals(clickedTile)) {
                        targetTile = clickedTile;
                        Tile startTile = playerTile;
                        int[] startTilePos = new int[]{(int) startTile.tileMapPos.x + 2, (int) startTile.tileMapPos.y};
                        int[] endTilePos = new int[]{(int) targetTile.tileMapPos.x, (int) targetTile.tileMapPos.y};
                        pathCalculator.clearTerrainWeights();
                        pathCalculator.setTerrainWeight("W", Double.POSITIVE_INFINITY);
                        pathResult = pathCalculator.findPath(tileMap.getMap(), startTilePos, endTilePos);
                        updateHoverEffect(screenX, screenY);
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
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDesktop()) {
            updateHoverEffect(screenX, screenY);
        }
        return super.touchDragged(screenX, screenY, pointer);
    }

    public void update() {
        if (player.hasReachedTarget() && targetTile != null
                && pathResult.isSuccess()) {
            targetTile.setTileTexture(new Texture("tiles/crop_land.png"));
        }
    }

    private void updateHoverEffect(int screenX, int screenY) {
        Tile currentTile = getTileAtPosition(tileMap, camera, screenX, screenY);
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

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isZooming = false;
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        isPanning = true;
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        isPanning = false;
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        isZooming = true;
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        isZooming = true;
        return true;
    }

    @Override
    public void pinchStop() {
        isZooming = false;
    }
}
