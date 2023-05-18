package com.farmsteadfortress.entities;

import static com.farmsteadfortress.utils.Helpers.gridToWorldPosition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.plants.Plant;
import com.farmsteadfortress.inventory.Inventory;
import com.farmsteadfortress.path.PathCalculator;
import com.farmsteadfortress.path.PathResult;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;

import java.util.List;

/**
 * Represents a player entity in the game.
 */
public class Player {
    private enum Direction {
        NW, S, SE, SW, W
    }

    private Animation<TextureRegion> idleAnimationNW;
    private Animation<TextureRegion> idleAnimationS;
    private Animation<TextureRegion> idleAnimationSE;
    private Animation<TextureRegion> idleAnimationSW;
    private Animation<TextureRegion> idleAnimationW;

    private Animation<TextureRegion> walkAnimationNW;
    private Animation<TextureRegion> walkAnimationS;
    private Animation<TextureRegion> walkAnimationSE;
    private Animation<TextureRegion> walkAnimationSW;
    private Animation<TextureRegion> walkAnimationW;

    private Animation<TextureRegion> attackNW;
    private Animation<TextureRegion> attackS;
    private Animation<TextureRegion> attackSE;
    private Animation<TextureRegion> attackSW;
    private Animation<TextureRegion> attackW;

    private Animation<TextureRegion> dieAnimation;

    private boolean flipCurrentFrame;
    private boolean isWalking;
    private float stateTime;
    private Vector2 position;
    private List<int[]> currentPath;
    private int currentPathIndex;
    private float speed;
    private TileMap map;
    private Inventory inventory;
    private Plant.PlantType plantToBePlanted;
    private Enemy targetedEnemy = null;

    private int money = 0;
    private float attackRange = 500f;
    private int attackDamage = 5;
    private float timeSinceLastAttack = 0f;
    private float timeBetweenAttacks = 10f;
    private Direction currentDirection = Direction.S;

    public Player(float animationSpeed, float speed, Vector2 spawnPosition, TileMap map) {
        TextureAtlas idleAtlasNW = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/idle/idle_nw.atlas"));
        TextureAtlas idleAtlasS = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/idle/idle_s.atlas"));

        idleAnimationNW = new Animation<TextureRegion>(animationSpeed, idleAtlasNW.getRegions(), Animation.PlayMode.LOOP);
        idleAnimationS = new Animation<TextureRegion>(animationSpeed, idleAtlasS.getRegions(), Animation.PlayMode.LOOP);

        TextureAtlas dieAtlas = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/die/die.atlas"));
        dieAnimation = new Animation<TextureRegion>(animationSpeed, dieAtlas.getRegions(), Animation.PlayMode.LOOP);

        TextureAtlas walkAtlasNW = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/movement/go_nw.atlas"));
        TextureAtlas walkAtlasS = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/movement/go_s.atlas"));

        walkAnimationNW = new Animation<TextureRegion>(animationSpeed, walkAtlasNW.getRegions(), Animation.PlayMode.LOOP);
        walkAnimationS = new Animation<TextureRegion>(animationSpeed, walkAtlasS.getRegions(), Animation.PlayMode.LOOP);

        TextureAtlas attackAtlasNW = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/attack/attack_nw.atlas"));
        TextureAtlas attackAtlasS = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/attack/attack_s.atlas"));
        TextureAtlas attackAtlasSE = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/attack/attack_se.atlas"));
        TextureAtlas attackAtlasSW = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/attack/attack_sw.atlas"));
        TextureAtlas attackAtlasW = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/attack/attack_w.atlas"));

        attackNW = new Animation<TextureRegion>(animationSpeed, attackAtlasNW.getRegions(), Animation.PlayMode.LOOP);
        attackS = new Animation<TextureRegion>(animationSpeed, attackAtlasS.getRegions(), Animation.PlayMode.LOOP);
        attackSE = new Animation<TextureRegion>(animationSpeed, attackAtlasSE.getRegions(), Animation.PlayMode.LOOP);
        attackSW = new Animation<TextureRegion>(animationSpeed, attackAtlasSW.getRegions(), Animation.PlayMode.LOOP);
        attackW = new Animation<TextureRegion>(animationSpeed, attackAtlasW.getRegions(), Animation.PlayMode.LOOP);

        this.stateTime = 0f;
        this.position = spawnPosition;
        this.speed = speed;
        this.currentPath = null;
        this.currentPathIndex = 0;
        this.map = map;
        this.money = 5;
        inventory = new Inventory();
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public Plant.PlantType getPlantToBePlanted() {
        return plantToBePlanted;
    }

    public void setPlantToBePlanted(Plant.PlantType plantToBePlanted) {
        this.plantToBePlanted = plantToBePlanted;
    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Sets the path for the player to follow.
     *
     * @param path The path represented as a list of grid points.
     */
    public void setPath(List<int[]> path) {
        currentPathIndex = 0;
        if (path != null && path.size() > 1) {
            if (path.size() > 3) {
                currentPath = path.subList(3, path.size());
            } else {
                currentPath = path.subList(1, path.size());
            }
        } else {
            currentPath = path;
        }
    }

    public void setDirection(Direction direction) {
        currentDirection = direction;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void updateDirection() {
        if (currentPath != null && currentPathIndex < currentPath.size()) {
            int[] currentTile = currentPath.get(currentPathIndex);
            int[] endTile = currentPath.get(currentPath.size() - 1);

            int dx = endTile[0] - currentTile[0];
            int dy = endTile[1] - currentTile[1];

            // Check if player is moving diagonally up and to the right (NE)
            if (dx > 0 && dy > 0) {
                setDirection(Direction.NW);
                flipCurrentFrame = true;
            }
            // Check if player is moving diagonally down and to the right (SE)
            else if (dx > 0 && dy < 0) {
                setDirection(Direction.S);
                flipCurrentFrame = true;
            }
            // Check if player is moving diagonally up and to the left (NW)
            else if (dx < 0 && dy > 0) {
                setDirection(Direction.NW);
                flipCurrentFrame = false;
            }
            // Check if player is moving diagonally down and to the left (SW)
            else if (dx < 0 && dy < 0) {
                setDirection(Direction.S);
                flipCurrentFrame = false;
            }
            // Check if player is moving straight down
            else if (dy < 0) {
                setDirection(Direction.S);
                flipCurrentFrame = true;
            }
            // Check if player is moving straight up
            else if (dy > 0) {
                setDirection(Direction.NW);
                flipCurrentFrame = false;
            }
            // Check if player is moving straight to the right
            else if (dx > 0) {
                setDirection(Direction.S);
                flipCurrentFrame = true;
            }
            // Check if player is moving straight to the left
            else if (dx < 0) {
                setDirection(Direction.S);
                flipCurrentFrame = false;
            }
        }
    }




    /**
     * Updates the player's position and state.
     *
     * @param delta The time elapsed since the last update.
     */
    public void update(float delta) {
        stateTime += delta;
        if (isWalking() && currentPath != null && !currentPath.isEmpty()) {
            int[] targetPoint = currentPath.get(currentPathIndex);
            Vector2 targetPosition = calculateTargetPosition(targetPoint);
            moveTowardsTarget(targetPosition, delta);
            checkTargetReached(targetPosition);
            updateDirection();
        }

        timeSinceLastAttack += delta;
        updateStateTime();

        if (targetedEnemy != null) {
            targetEnemy(targetedEnemy, map, new PathCalculator());
        }
    }

    /**
     * Renders the player on the screen.
     *
     * @param batch The SpriteBatch used for rendering.
     */
    public void render(SpriteBatch batch) {
        Animation<TextureRegion> currentAnimation = getCurrentAnimation();
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        float yOffset = Tile.TILE_SIZE / 2f;
        float posX = position.x;
        float posY = position.y + yOffset;
        batch.draw(currentFrame, posX, posY, currentFrame.getRegionWidth() / 2, currentFrame.getRegionHeight() / 2, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), flipCurrentFrame ? -1 : 1, 1, 0);
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
        Animation<TextureRegion> currentAnimation = getCurrentAnimation();
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        float yOffset = Tile.TILE_SIZE / 2f;  // Y Offset

        float originX = currentFrame.getRegionWidth() / 2f;
        float originY = currentFrame.getRegionHeight() / 2f;

        float posX = position.x - originX + (Tile.TILE_SIZE - currentFrame.getRegionWidth());
        float posY = position.y - originY + (Tile.TILE_SIZE - currentFrame.getRegionHeight()) + yOffset;  // Apply the Y offset here

        return new Vector2(posX, posY);
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
        float distance = position.dst(targetPosition);
        if (distance < 5f || (targetedEnemy != null && position.dst(targetedEnemy.getPosition()) < 5f)) {
            if (currentPathIndex < currentPath.size() - 1) {
                currentPathIndex++;
            } else {
                setWalking(false);
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

    public boolean canAttack() {
        return timeSinceLastAttack >= timeBetweenAttacks;
    }

    public void attack(Enemy enemy) {
        enemy.attacked(this);
        timeSinceLastAttack = 0f;
    }

    public boolean canAttackEnemy(Enemy enemy) {
        return this.getPosition().dst(enemy.getPosition()) <= attackRange && canAttack();
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void targetEnemy(Enemy enemy, TileMap tileMap, PathCalculator pathCalculator) {
        targetedEnemy = enemy;
        Tile playerTile = tileMap.getTileAt(getPosition());
        Tile enemyTile = tileMap.getTileAt(enemy.getPosition());
        if (playerTile != null && enemyTile != null) {
            int[] startTilePos = new int[]{(int) playerTile.tileMapPos.x + 2, (int) playerTile.tileMapPos.y};
            int[] endTilePos = new int[]{(int) enemyTile.tileMapPos.x, (int) enemyTile.tileMapPos.y};

            pathCalculator.clearTerrainWeights();
            pathCalculator.setTerrainWeight("W", Double.POSITIVE_INFINITY);

            float distance = getPosition().dst(enemy.getPosition());

            if (distance > 10f) {
                PathResult pathResult = pathCalculator.findPath(tileMap.getMap(), startTilePos, endTilePos);

                if (pathResult.isSuccess()) {
                    setPath(pathResult.getPath());
                }
            }

            if (canAttackEnemy(enemy)) {
                attack(enemy);
            }
        }
    }

    public void stopFollowing() {
        targetedEnemy = null;
    }

    private Animation<TextureRegion> getCurrentAnimation() {
        Animation<TextureRegion> currentAnimation;
        switch (currentDirection) {
            case NW:
                currentAnimation = isWalking() ? walkAnimationNW : idleAnimationNW;
                break;
            case S:
                currentAnimation = isWalking() ? walkAnimationS : idleAnimationS;
                break;
            default:
                throw new IllegalStateException("Invalid direction: " + currentDirection);
        }
        return currentAnimation;
    }
}