package com.farmsteadfortress.entities.enemies;

import static com.farmsteadfortress.utils.Helpers.gridToWorldPosition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;

import java.util.List;

/**
 * Represents a generic enemy entity in the game.
 */
public abstract class Enemy {
    protected Animation<TextureRegion> walkingAnimation;
    protected float stateTime;
    protected Vector2 direction;
    protected Vector2 position;
    protected List<int[]> currentPath;
    protected int currentPathIndex;
    protected float speed;
    protected boolean outline;
    protected Rectangle boundingBox;
    protected int health;
    protected int reward;

    /**
     * Constructs an enemy entity.
     *
     * @param atlas          the texture atlas containing the walking animation frames
     * @param animationSpeed the speed of the walking animation
     * @param speed          the movement speed of the enemy
     * @param health         the enemy's health
     */
    public Enemy(TextureAtlas atlas, float animationSpeed, float speed, int health) {
        this.walkingAnimation = new Animation<TextureRegion>(animationSpeed, atlas.getRegions());
        this.stateTime = 0f;
        this.position = new Vector2();
        this.currentPath = null;
        this.currentPathIndex = 0;
        this.speed = speed;
        this.direction = new Vector2();
        this.outline = false;
        this.boundingBox = new Rectangle();
        this.health = health;
    }

    /**
     * Abstract method for the enemy being attacked, to be implemented by subclasses.
     *
     * @param player the player attacking the enemy
     */
    public abstract void attacked(Player player);

    /**
     * Toggles the enemy's outline status when clicked.
     */
    public void onClick() {
        outline = !outline;
    }

    /**
     * Describes the enemy's death.
     */
    public void die() {
        System.out.println("died");
    }

    /**
     * Retrieves the enemy's position.
     *
     * @return the enemy's position as a Vector2
     */
    public Vector2 getPosition() {
        TextureRegion currentFrame = walkingAnimation.getKeyFrame(stateTime, true);
        float originX = currentFrame.getRegionWidth() / 2f;
        float originY = currentFrame.getRegionHeight() / 2f;
        float yOffset = Tile.TILE_SIZE / 2f;

        float posX = position.x - originX + (Tile.TILE_SIZE - currentFrame.getRegionWidth() / 2.25f);
        float posY = position.y - originY + yOffset + (Tile.TILE_SIZE - currentFrame.getRegionHeight() / 2.25f);

        return new Vector2(posX, posY);
    }


    /**
     * Checks if the enemy contains the specified point.
     *
     * @param point the point to check
     * @return true if the enemy contains the point, false otherwise
     */
    public boolean containsPoint(Vector2 point) {
        return boundingBox.contains(point);
    }

    /**
     * Updates the enemy's position and animation.
     *
     * @param deltaTime the time elapsed since the last update
     * @param map       the tile map
     */
    public void update(float deltaTime, TileMap map) {
        if (currentPath == null) {
            setPath(map);
        }

        boundingBox.set(position.x, position.y, walkingAnimation.getKeyFrame(stateTime).getRegionWidth(), walkingAnimation.getKeyFrame(stateTime).getRegionHeight());
        if (currentPath != null && !currentPath.isEmpty()) {
            if (currentPathIndex < currentPath.size()) {
                int[] coordinate = currentPath.get(currentPathIndex);
                int row = coordinate[0];
                int col = coordinate[1];
                Vector2 targetPosition = gridToWorldPosition(row, col, Tile.TILE_SIZE, (float) Tile.TILE_SIZE / 2);
                Vector2 direction = new Vector2(targetPosition.x - position.x, targetPosition.y - position.y).nor();
                position.x += direction.x * speed * deltaTime;
                position.y += direction.y * speed * deltaTime;

                if (position.dst(targetPosition) < 5f) {
                    currentPathIndex++;
                }
            }
        }
    }

    /**
     * Sets the path for the enemy to follow.
     *
     * @param map the tile map
     */
    public void setPath(TileMap map) {
        if (!map.getEnemyPaths().isEmpty()) {
            currentPath = map.getEnemyPaths().get(0);
            if (currentPath != null && !currentPath.isEmpty()) {
                int[] firstCoordinate = currentPath.get(0);
                int firstRow = firstCoordinate[0];
                int firstCol = firstCoordinate[1];
                Vector2 initialPosition = gridToWorldPosition(firstRow, firstCol, Tile.TILE_SIZE, (float) Tile.TILE_SIZE / 2);
                position.set(initialPosition.x, initialPosition.y);
            }
        }
    }

    /**
     * Retrieves the rotation angle for rendering the enemy.
     *
     * @return the rotation angle in degrees
     */
    private float getRotationAngle() {
        float angle = direction.angleDeg();
        return angle;
    }

    /**
     * Renders the enemy on the screen.
     *
     * @param batch the sprite batch used for rendering
     */
    public void render(SpriteBatch batch) {
        float yOffset = Tile.TILE_SIZE / 2f;
        float angle = getRotationAngle();
        float originX = walkingAnimation.getKeyFrame(stateTime).getRegionWidth() / 2f;
        float originY = walkingAnimation.getKeyFrame(stateTime).getRegionHeight() / 2f;

        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = walkingAnimation.getKeyFrame(stateTime, true);
        float posX = position.x - originX + (Tile.TILE_SIZE - currentFrame.getRegionWidth() / 2.25f);
        float posY = position.y - originY + yOffset + (Tile.TILE_SIZE - currentFrame.getRegionHeight() / 2.25f);

        if (outline) {
            batch.setColor(Color.RED);
        }
        batch.draw(currentFrame,
                posX, posY,
                originX, originY,
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),
                1, 1,
                angle);
        if (outline) {
            batch.setColor(Color.WHITE);
        }
    }
}