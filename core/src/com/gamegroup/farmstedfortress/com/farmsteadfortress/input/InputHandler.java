package com.farmsteadfortress.input;

import static com.farmsteadfortress.utils.Helpers.getTileAtPosition;
import static com.farmsteadfortress.utils.Helpers.isDesktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.entities.plants.FernPlant;
import com.farmsteadfortress.entities.plants.Plant;
import com.farmsteadfortress.entities.plants.PlantFactory;
import com.farmsteadfortress.items.Item;
import com.farmsteadfortress.items.seeds.Seed;
import com.farmsteadfortress.path.PathCalculator;
import com.farmsteadfortress.path.PathResult;
import com.farmsteadfortress.projectiles.ProjectileManager;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;
import com.farmsteadfortress.ui.Hotbar;
import com.farmsteadfortress.ui.ShopUI;
import com.farmsteadfortress.utils.Helpers;

import java.util.ArrayList;
import java.util.List;


public class InputHandler extends InputAdapter implements GestureDetector.GestureListener, InputProcessor {
    private TileMap tileMap;
    private OrthographicCamera camera;
    private Texture hoverTexture;
    private Player player;

    private List<Enemy> enemies;
    private Enemy selectedEnemy;

    private PathCalculator pathCalculator;
    private PathResult pathResult;
    private Tile lastHoveredTile;
    private Tile targetTile;
    private Hotbar hotbar;
    private ArrayList<Stage> uiStages;
    private boolean isPanning;
    private boolean isZooming;
    private float panSpeed = 10f;
    private float cameraOffsetX = 0;
    private float cameraOffsetY = 0;
    private float cameraMoveSpeed = 7f;
    private float maxCameraOffset = 150f;
    private Tile playerTile;
    private ShopUI shop;
    private ProjectileManager projectileManager;

    public InputHandler(TileMap tileMap, OrthographicCamera camera, Player player, List<Enemy> enemies, InputMultiplexer inputMultiplexer, Hotbar hotbar, ShopUI shop) {
        hoverTexture = new Texture("tiles/highlight.png");
        this.tileMap = tileMap;
        this.camera = camera;
        this.player = player;
        this.enemies = enemies;
        this.selectedEnemy = null;
        this.pathCalculator = new PathCalculator();
        this.hotbar = hotbar;
        this.shop = shop;
        inputMultiplexer.addProcessor(this);

        if (isDesktop()) {
            panSpeed = 10f;
        }
    }

    public void handleCameraInput() {
        if (Gdx.input.isTouched(0) && Gdx.input.isTouched(1)) {
            float initialDistance = Vector2.dst(
                    Gdx.input.getX(0), Gdx.input.getY(0),
                    Gdx.input.getX(1), Gdx.input.getY(1));

            float currentDistance = Vector2.dst(
                    Gdx.input.getX(0) + Gdx.input.getDeltaX(0), Gdx.input.getY(0) + Gdx.input.getDeltaY(0),
                    Gdx.input.getX(1) + Gdx.input.getDeltaX(1), Gdx.input.getY(1) + Gdx.input.getDeltaY(1));

            float pinchScale = currentDistance / initialDistance;

            float targetZoom = camera.zoom / pinchScale;
            camera.zoom = MathUtils.lerp(camera.zoom, targetZoom, panSpeed);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cameraOffsetY = Math.min(cameraOffsetY + cameraMoveSpeed, maxCameraOffset);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cameraOffsetX = Math.max(cameraOffsetX - cameraMoveSpeed, -maxCameraOffset);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            cameraOffsetY = Math.max(cameraOffsetY - cameraMoveSpeed, -maxCameraOffset);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            cameraOffsetX = Math.min(cameraOffsetX + cameraMoveSpeed, maxCameraOffset);
        }
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
        if (isTouchWithinUI(screenX, screenY) || isSecondFingerTouched()) {
            return false;
        }

        if (!isPanning && !isZooming) {
            handlePlayerActions(screenX, screenY);
            handleEnemyActions(screenX, screenY);
        }

        return true;
    }

    private boolean isTouchWithinUI(int screenX, int screenY) {
        return Helpers.uiContains(uiStages, screenX, screenY, shop);
    }

    private boolean isSecondFingerTouched() {
        return Gdx.input.isTouched(1);
    }

    private void handlePlayerActions(int screenX, int screenY) {
        playerTile = tileMap.getTileAt(player.getPosition());

        if (playerTile != null) {
            Tile clickedTile = getTileAtPosition(tileMap, camera, screenX, screenY);

            if (clickedTile != null && clickedTile.isIntractable()) {
                if (player.hasReachedTarget() || targetTile != null) {
                    targetTile = clickedTile;
                    int[] startTilePos = new int[]{(int) playerTile.tileMapPos.x, (int) playerTile.tileMapPos.y};
                    int[] endTilePos = new int[]{(int) targetTile.tileMapPos.x, (int) targetTile.tileMapPos.y};
                    pathCalculator.clearTerrainWeights();
                    pathCalculator.setTerrainWeight("W", 1000);
                    pathResult = pathCalculator.findPath(tileMap.getMap(), startTilePos, endTilePos);
                    updateHoverEffect(screenX, screenY);
                    if (pathResult.isSuccess()) {
                        player.setPath(pathResult.getPath());
                        player.setWalking(true);
                        Item selectedItem = hotbar.getSelectedSlotItem();

                        if (selectedItem instanceof Seed && player.getInventory().contains(selectedItem)) {
                            player.getInventory().removeItem(selectedItem);
                            hotbar.updateHotbar();
                            player.setPlantToBePlanted(((Seed) selectedItem).getPlantType());
                        }
                    }
                }
            }
        }
    }

    private void handleEnemyActions(int screenX, int screenY) {
        Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));
        Vector2 touchPosition = new Vector2(worldCoordinates.x, worldCoordinates.y);

        List<Enemy> clickedEnemies = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy.containsPoint(touchPosition)) {
                clickedEnemies.add(enemy);
            }
        }

        for (Enemy enemy : clickedEnemies) {
            enemy.onClick();
            player.targetEnemy(enemy, tileMap, pathCalculator);
        }

        if (clickedEnemies.isEmpty()) {
            player.stopFollowing();
        }
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDesktop()) {
            updateHoverEffect(screenX, screenY);
        }
        return super.touchDragged(screenX, screenY, pointer);
    }

    public void update() {
        Vector3 playerPosition = new Vector3(player.getPosition().x + 145f + cameraOffsetX, player.getPosition().y + 50f + cameraOffsetY, 0); // adding 50 units to x-coordinate
        camera.position.set(playerPosition);
        camera.update();

        boolean hasReachedTarget = targetTile != null && player.hasReachedTarget() && pathResult.isSuccess();
        boolean hasPlantToPlant = player.getPlantToBePlanted() != null;
        boolean hasFern = targetTile != null && targetTile.getPlant() instanceof FernPlant;

        if (hasReachedTarget) {
            if (hasFern) {
                ((FernPlant) targetTile.getPlant()).harvest(player);
            } else if (hasPlantToPlant) {
                targetTile.setTileTexture(new Texture("tiles/crop_land.png"));
                Plant plant = PlantFactory.createPlant(player.getPlantToBePlanted(), targetTile, player, projectileManager);
                targetTile.setPlant(plant);
                player.setPlantToBePlanted(null);
            }
        }
    }

    private void updateHoverEffect(int screenX, int screenY) {
        Tile currentTile = getTileAtPosition(tileMap, camera, screenX, screenY);

        if (currentTile != null) {
            if (currentTile.isIntractable()) {
                if (currentTile != lastHoveredTile) {
                    if (lastHoveredTile != null) {
                        if (lastHoveredTile.getPlant() != null) {
                            lastHoveredTile.getPlant().setHighLight(false);
                        } else {
                            lastHoveredTile.setHoverTexture(lastHoveredTile.getOriginalTileTexture());
                        }
                    }

                    if (currentTile.getPlant() != null) {
                        currentTile.getPlant().setHighLight(true);
                    } else {
                        currentTile.setHoverTexture(hoverTexture);
                    }

                    lastHoveredTile = currentTile;
                }
            } else {
                if (lastHoveredTile != null) {
                    if (lastHoveredTile.getPlant() != null) {
                        lastHoveredTile.getPlant().setHighLight(false);
                    } else {
                        lastHoveredTile.setHoverTexture(lastHoveredTile.getOriginalTileTexture());
                    }
                    lastHoveredTile = null;
                }
            }
        } else {
            if (lastHoveredTile != null) {
                if (lastHoveredTile.getPlant() != null) {
                    lastHoveredTile.getPlant().setHighLight(false);
                } else {
                    lastHoveredTile.setHoverTexture(lastHoveredTile.getOriginalTileTexture());
                }
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

    public void setUiStages(ArrayList<Stage> uiStages) {
        this.uiStages = uiStages;
    }

    public void setProjectileManager(ProjectileManager projectileManager) {
        this.projectileManager = projectileManager;
    }
}