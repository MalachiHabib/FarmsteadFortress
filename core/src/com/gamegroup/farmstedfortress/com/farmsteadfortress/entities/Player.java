package com.farmsteadfortress.entities;

import static com.farmsteadfortress.utils.Helpers.gridToWorldPosition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;

import java.util.List;

/**
 * Represents a player entity in the game.
 */
public class Player {
    private Animation<TextureRegion> walkingAnimation;
    private float stateTime;
    private Vector2 position;
    private List<int[]> currentPath;
    private int currentPathIndex;
    private float speed;
    private TileMap map;

    public Player(TextureAtlas atlas, float animationSpeed, float speed, Vector2 spawnPosition, TileMap map) {
        this.walkingAnimation = new Animation<TextureRegion>(animationSpeed, atlas.getRegions());
        this.stateTime = 0f;
        this.position = spawnPosition;
        this.speed = speed;
        this.currentPath = null;
        this.currentPathIndex = 0;
        this.map = map;
    }

    /**
     * Sets the path for the player to follow.
     *
     * @param path The path represented as a list of grid points.
     */
    public void setPath(List<int[]> path) {
        currentPathIndex = 0;
        currentPath = (path != null && path.size() > 2) ? path.subList(2, path.size()) : path;
    }

    /**
     * Updates the player's position and state.
     *
     * @param delta The time elapsed since the last update.
     */
    public void update(float delta) {
        if (currentPath != null && !currentPath.isEmpty()) {
            int[] targetPoint = currentPath.get(currentPathIndex);
            Vector2 targetPosition = calculateTargetPosition(targetPoint);
            moveTowardsTarget(targetPosition, delta);
            checkTargetReached(targetPosition);
        }
        updateStateTime();
    }

    /**
     * Renders the player on the screen.
     *
     * @param batch The SpriteBatch used for rendering.
     */
    public void render(SpriteBatch batch) {
        float yOffset = Tile.TILE_SIZE / 2f;
        TextureRegion currentFrame = walkingAnimation.getKeyFrame(stateTime, true);
        float posX = position.x + (Tile.TILE_SIZE - currentFrame.getRegionWidth());
        float posY = position.y + yOffset + (Tile.TILE_SIZE - currentFrame.getRegionHeight());
        batch.draw(currentFrame, posX, posY);
    }

    /**
     * Checks if the player has reached the final target point.
     *
     * @return true if the player has reached the final target point, false otherwise.
     */
    public boolean hasReachedTarget() {
        if (currentPath != null && currentPathIndex < currentPath.size()) {
            int[] lastPoint = currentPath.get(currentPath.size() - 1);
            Vector2 targetPosition = gridToWorldPosition(lastPoint[0], lastPoint[1], Tile.TILE_SIZE, (float) Tile.TILE_SIZE / 2);
            return position.dst(targetPosition) < 5f;
        }
        return true;
    }

    /**
     * Returns the current position of thwe player.
     *
     * @return The position of the player.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Calculates the target position for the player to move towards.
     *
     * @param targetPoint The grid point representing the target position.
     * @return The world position corresponding to the target point.
     */
    private Vector2 calculateTargetPosition(int[] targetPoint) {
        int tileSize = Tile.TILE_SIZE;
        float halfTileSize = (float) tileSize / 2;
        return gridToWorldPosition(targetPoint[0], targetPoint[1], tileSize, halfTileSize);
    }

    /**
     * Moves the player towards the target position.
     *
     * @param targetPosition The target position to move towards.
     * @param delta          The time elapsed since the last update.
     */
    private void moveTowardsTarget(Vector2 targetPosition, float delta) {
        Vector2 direction = new Vector2(targetPosition.x - position.x, targetPosition.y - position.y).nor();
        position.x += direction.x * speed * delta;
        position.y += direction.y * speed * delta;
    }


    /**
     * Checks if the player has reached the target position and updates the path accordingly.
     *
     * @param targetPosition The target position to check against.
     */
    private void checkTargetReached(Vector2 targetPosition) {
        if (position.dst(targetPosition) < 5f) {
            if (currentPathIndex < currentPath.size() - 1) {
                currentPathIndex++;
            } else {
                clearPath();
            }
        }
    }

    /**
     * Clears the current path of the player.
     */
    private void clearPath() {
        currentPath = null;
        currentPathIndex = 0;
    }

    /**
     * Updates the state time of the player for animation purposes.
     */
    private void updateStateTime() {
        stateTime += Gdx.graphics.getDeltaTime();
    }
}