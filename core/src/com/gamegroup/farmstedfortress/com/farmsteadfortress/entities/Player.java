package com.farmsteadfortress.entities;

import static com.farmsteadfortress.utils.Helpers.gridToWorldPosition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.entities.plants.Plant;
import com.farmsteadfortress.inventory.Inventory;
import com.farmsteadfortress.path.PathCalculator;
import com.farmsteadfortress.path.PathResult;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a player entity in the game.
 */
public class Player {

    private enum Direction {
        N, S, E, W, NE, NW, SE, SW
    }

    Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> idleAnimation, walkAnimationN, walkAnimationS, walkAnimationE, walkAnimationW, walkAnimationNE, walkAnimationNW, walkAnimationSW, walkAnimationSE;
    private Animation<TextureRegion> dieAnimation;
    private boolean flipCurrentFrame;
    private boolean isAttacking = false;
    private boolean isWalking;
    private float stateTime;
    private Vector2 position;
    private List<int[]> currentPath;
    private int currentPathIndex;
    private Tile lastKnownEnemyTile = null;
    private float speed;
    private TileMap map;
    private Inventory inventory;
    private Plant.PlantType plantToBePlanted;
    private Enemy targetedEnemy = null;
    private int money = 0;
    private int health = 0;
    private float attackRange = 200f;
    private int attackDamage = 2;
    private float timeSinceLastAttack = 0f;
    private float timeBetweenAttacks = 1f;
    private Direction currentDirection = Direction.S;
    private List<int[]> originalPath;

    public Player(float animationSpeed, float speed, Vector2 spawnPosition, TileMap map) {

        TextureAtlas idleAtlas = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/idle/PlayerIdle.atlas"));
        idleAnimation = new Animation<TextureRegion>(animationSpeed, idleAtlas.getRegions(), Animation.PlayMode.LOOP);

        TextureAtlas walkAtlasN = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/movement/PlayerN.atlas"));
        TextureAtlas walkAtlasS = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/movement/PlayerS.atlas"));
        TextureAtlas walkAtlasE = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/movement/PlayerE.atlas"));
        TextureAtlas walkAtlasW = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/movement/PlayerW.atlas"));
        TextureAtlas walkAtlasNE = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/movement/PlayerNE.atlas"));
        TextureAtlas walkAtlasNW = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/movement/PlayerNW.atlas"));
        TextureAtlas walkAtlasSE = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/movement/PlayerSE.atlas"));
        TextureAtlas walkAtlasSW = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/movement/PlayerSW.atlas"));

        walkAnimationN = new Animation<TextureRegion>(animationSpeed, walkAtlasN.getRegions(), Animation.PlayMode.LOOP);
        walkAnimationS = new Animation<TextureRegion>(animationSpeed, walkAtlasS.getRegions(), Animation.PlayMode.LOOP);
        walkAnimationE = new Animation<TextureRegion>(animationSpeed, walkAtlasE.getRegions(), Animation.PlayMode.LOOP);
        walkAnimationW = new Animation<TextureRegion>(animationSpeed, walkAtlasW.getRegions(), Animation.PlayMode.LOOP);
        walkAnimationNE = new Animation<TextureRegion>(animationSpeed, walkAtlasNE.getRegions(), Animation.PlayMode.LOOP);
        walkAnimationNW = new Animation<TextureRegion>(animationSpeed, walkAtlasNW.getRegions(), Animation.PlayMode.LOOP);
        walkAnimationSE = new Animation<TextureRegion>(animationSpeed, walkAtlasSE.getRegions(), Animation.PlayMode.LOOP);
        walkAnimationSW = new Animation<TextureRegion>(animationSpeed, walkAtlasSW.getRegions(), Animation.PlayMode.LOOP);

        this.stateTime = 0f;
        this.position = spawnPosition;
        this.speed = speed;
        this.currentPath = null;
        this.currentPathIndex = 0;
        this.map = map;
        this.money = 500;
        this.health = 100;
        inventory = new Inventory();
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public int getHealth() {
        return health;
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

    public void setPath(List<int[]> path) {
        int waterTileCount = 0;
        for (int[] tile : path) {
            if (map.getMap()[tile[0]][tile[1]].equals("W")) {
                waterTileCount++;
            }
            if (waterTileCount >= 3) {
                isWalking = false;
                return;
            }
        }

        currentPathIndex = 0;
        originalPath = path;
        int startPathIndex;
        if (path.size() > 3) {
            startPathIndex = 3;
        } else if (path.size() > 2) {
            startPathIndex = 2;
        } else if (path.size() > 1) {
            startPathIndex = 1;
        } else {
            startPathIndex = 0;
        }

        currentPath = path.subList(startPathIndex, path.size());
    }


    public void setDirection(Direction direction) {
        currentDirection = direction;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public void updateDirection() {
        if (originalPath != null && currentPathIndex < originalPath.size()) {
            int[] currentTile = originalPath.get(currentPathIndex);
            int[] endTile = originalPath.get(originalPath.size() - 1);

            int dx = endTile[0] - currentTile[0];
            int dy = endTile[1] - currentTile[1];

            if (dx > 0) {
                if (dy > 0) {
                    setDirection(Direction.NE);
                } else if (dy < 0) {
                    setDirection(Direction.SE);
                } else {
                    setDirection(Direction.E);
                }
            } else if (dx < 0) {
                if (dy > 0) {
                    setDirection(Direction.NW);
                } else if (dy < 0) {
                    setDirection(Direction.SW);
                } else {
                    setDirection(Direction.W);
                }
            } else {
                if (dy > 0) {
                    setDirection(Direction.N);
                } else if (dy < 0) {
                    setDirection(Direction.S);
                }
            }
        }
    }

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
            Tile currentEnemyTile = map.getTileAt(targetedEnemy.getPosition());
            if (lastKnownEnemyTile == null || !lastKnownEnemyTile.equals(currentEnemyTile)) {
                lastKnownEnemyTile = currentEnemyTile;
                targetEnemy(targetedEnemy, map, new PathCalculator());
            }
        }
    }

    public void render(SpriteBatch batch) {
        Animation<TextureRegion> currentAnimation = getCurrentAnimation();
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        float yOffset = Tile.TILE_SIZE / 2f + 15f;
        float posX = position.x;
        float posY = position.y + yOffset;
        batch.draw(currentFrame, posX, posY, currentFrame.getRegionWidth() / 2, currentFrame.getRegionHeight() / 2, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), flipCurrentFrame ? -1 : 1, 1, 0);
    }

    public boolean hasReachedTarget() {
        if (currentPath != null && currentPathIndex < currentPath.size()) {
            int[] lastPoint = currentPath.get(currentPath.size() - 1);
            Vector2 targetPosition = gridToWorldPosition(lastPoint[0], lastPoint[1], Tile.TILE_SIZE, (float) Tile.TILE_SIZE / 2);
            return position.dst(targetPosition) < 5f;
        }
        return true;
    }

    public Vector2 getPosition() {
        Animation<TextureRegion> currentAnimation = getCurrentAnimation();
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        float yOffset = Tile.TILE_SIZE / 2f + 15f;

        float originX = currentFrame.getRegionWidth() / 2f;
        float originY = currentFrame.getRegionHeight() / 2f;

        float posX = position.x - originX + (Tile.TILE_SIZE - currentFrame.getRegionWidth());
        float posY = position.y - originY + (Tile.TILE_SIZE - currentFrame.getRegionHeight()) + yOffset;

        return new Vector2(posX, posY);
    }

    private Vector2 calculateTargetPosition(int[] targetPoint) {
        int tileSize = Tile.TILE_SIZE;
        float halfTileSize = (float) tileSize / 2;
        return gridToWorldPosition(targetPoint[0], targetPoint[1], tileSize, halfTileSize);
    }

    private void moveTowardsTarget(Vector2 targetPosition, float delta) {
        Vector2 direction = new Vector2(targetPosition.x - position.x, targetPosition.y - position.y).nor();
        position.x += direction.x * speed * delta;
        position.y += direction.y * speed * delta;
    }

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

    private void clearPath() {
        currentPath = null;
        currentPathIndex = 0;
    }

    private void updateStateTime() {
        stateTime += Gdx.graphics.getDeltaTime();
    }

    public boolean canAttack() {
        return timeSinceLastAttack >= timeBetweenAttacks;
    }

    public void attack(Enemy enemy) {
        System.out.println("attacked");
        enemy.attacked(attackDamage);
        timeSinceLastAttack = 0f;
    }

    public boolean canAttackEnemy(Enemy enemy) {
        return this.getPosition().dst(enemy.getPosition()) <= attackRange * 1.5f && canAttack();
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void targetEnemy(Enemy enemy, TileMap tileMap, PathCalculator pathCalculator) {
        targetedEnemy = enemy;
        Tile playerTile = tileMap.getTileAt(getPosition());
        Tile enemyTile = lastKnownEnemyTile;
        if (playerTile != null && enemyTile != null) {
            int[] startTilePos = new int[]{(int) playerTile.tileMapPos.x + 2, (int) playerTile.tileMapPos.y};
            int[] endTilePos = new int[]{(int) enemyTile.tileMapPos.x, (int) enemyTile.tileMapPos.y};

            pathCalculator.clearTerrainWeights();
            pathCalculator.setTerrainWeight("W", Double.POSITIVE_INFINITY);
            PathResult pathResult = pathCalculator.findPath(tileMap.getMap(), startTilePos, endTilePos);

            if (canAttackEnemy(enemy)) {
                attack(enemy);
            }

            if (pathResult.isSuccess()) {
                setPath(pathResult.getPath());
                setWalking(true);
            }
        }
    }

    public void stopFollowing() {
        targetedEnemy = null;
    }

    private Animation<TextureRegion> getCurrentAnimation() {
        switch (currentDirection) {
            case N:
                currentAnimation = isWalking() ? walkAnimationN : idleAnimation;
                break;
            case S:
                currentAnimation = isWalking() ? walkAnimationS : idleAnimation;
                break;
            case E:
                currentAnimation = isWalking() ? walkAnimationE : idleAnimation;
                break;
            case W:
                currentAnimation = isWalking() ? walkAnimationW : idleAnimation;
                break;
            case NE:
                currentAnimation = isWalking() ? walkAnimationNE : idleAnimation;
                break;
            case NW:
                currentAnimation = isWalking() ? walkAnimationNW : idleAnimation;
                break;
            case SE:
                currentAnimation = isWalking() ? walkAnimationSE : idleAnimation;
                break;
            case SW:
                currentAnimation = isWalking() ? walkAnimationSW : idleAnimation;
                break;
            default:
                throw new IllegalStateException("Invalid direction: " + currentDirection);
        }
        return currentAnimation;
    }

    public void attacked(int damage) {
        health -= damage;
        if (isDead()) {
            die();
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    private void die() {
        System.out.println("died");
    }
}