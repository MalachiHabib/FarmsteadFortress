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

import java.util.List;

/**
 * Represents a player entity in the game.
 */
public class Player {
    Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> idleAnimationN;
    private Animation<TextureRegion> idleAnimationS;

    private Animation<TextureRegion> walkAnimationN;
    private Animation<TextureRegion> walkAnimationS;

    private Animation<TextureRegion> attackAnimationN;
    private Animation<TextureRegion> attackAnimationS;

    private Animation<TextureRegion> dieAnimation;
    private boolean flipCurrentFrame;
    private boolean isAttacking = false;
    private boolean isWalking;
    private float stateTime;
    private Vector2 position;
    private List<int[]> currentPath;
    private int currentPathIndex;
    private boolean isNewPathSet = false;
    private Tile lastKnownEnemyTile = null;
    private float speed;
    private TileMap map;
    private Inventory inventory;
    private Plant.PlantType plantToBePlanted;
    private Enemy targetedEnemy = null;
    private int money = 0;
    private int health = 0;
    private float attackRange = 500f;
    private int attackDamage = 5;
    private float timeSinceLastAttack = 0f;
    private float timeBetweenAttacks = 1f;
    private Direction currentDirection = Direction.S;
    public Player(float animationSpeed, float speed, Vector2 spawnPosition, TileMap map) {
        TextureAtlas idleAtlasNW = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/idle/idle_nw.atlas"));
        TextureAtlas idleAtlasS = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/idle/idle_s.atlas"));

        idleAnimationN = new Animation<TextureRegion>(animationSpeed, idleAtlasNW.getRegions(), Animation.PlayMode.LOOP);
        idleAnimationS = new Animation<TextureRegion>(animationSpeed, idleAtlasS.getRegions(), Animation.PlayMode.LOOP);

        TextureAtlas dieAtlas = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/die/die.atlas"));
        dieAnimation = new Animation<TextureRegion>(animationSpeed, dieAtlas.getRegions(), Animation.PlayMode.LOOP);

        TextureAtlas walkAtlasNW = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/movement/go_nw.atlas"));
        TextureAtlas walkAtlasS = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/movement/go_s.atlas"));

        walkAnimationN = new Animation<TextureRegion>(animationSpeed, walkAtlasNW.getRegions(), Animation.PlayMode.LOOP);
        walkAnimationS = new Animation<TextureRegion>(animationSpeed, walkAtlasS.getRegions(), Animation.PlayMode.LOOP);

        TextureAtlas attackAtlasNW = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/attack/attack_nw.atlas"));
        TextureAtlas attackAtlasS = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/attack/attack_s.atlas"));

        attackAnimationN = new Animation<TextureRegion>(animationSpeed, attackAtlasNW.getRegions(), Animation.PlayMode.LOOP);
        attackAnimationS = new Animation<TextureRegion>(animationSpeed, attackAtlasS.getRegions(), Animation.PlayMode.LOOP);

        this.stateTime = 0f;
        this.position = spawnPosition;
        this.speed = speed;
        this.currentPath = null;
        this.currentPathIndex = 0;
        this.map = map;
        this.money = 5;
        this.health = 100;
        inventory = new Inventory();
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public int getPlayerHealth(){
        return health;
    }

    public void reducePlayerHealth(int damage){
        health -= damage;
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
        isNewPathSet = true;
        currentPathIndex = 0;
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
        if (currentPath != null && currentPathIndex < currentPath.size()) {
            int[] currentTile = currentPath.get(currentPathIndex);
            int[] endTile = currentPath.get(currentPath.size() - 1);

            int dx = endTile[0] - currentTile[0];
            int dy = endTile[1] - currentTile[1];

            if (dx > 0) {
                setDirection(dy > 0 ? Direction.N : Direction.S);
                flipCurrentFrame = true;
            } else if (dx < 0) {
                setDirection(dy > 0 ? Direction.N : Direction.S);
                flipCurrentFrame = false;
            } else if (dy < 0) {
                setDirection(Direction.S);
                flipCurrentFrame = true;
            } else if (dy > 0) {
                setDirection(Direction.N);
                flipCurrentFrame = false;
            } else {
                adjustDirectionBasedOnPosition(currentTile);
            }
        }
    }

    private void adjustDirectionBasedOnPosition(int[] currentTile) {
        Vector2 position = getPosition();
        if (position.x > currentTile[0]) {
            setDirection(Direction.S);
            flipCurrentFrame = true;
        } else if (position.x < currentTile[0]) {
            setDirection(Direction.S);
            flipCurrentFrame = false;
        } else if (position.y > currentTile[1]) {
            setDirection(Direction.N);
            flipCurrentFrame = false;
        } else if (position.y < currentTile[1]) {
            setDirection(Direction.S);
            flipCurrentFrame = true;
        }
    }

    public void update(float delta) {
        stateTime += delta;
        if (isAttacking) {
            if (attackAnimationN.isAnimationFinished(stateTime) || attackAnimationS.isAnimationFinished(stateTime)) {
                setAttacking(false);
                timeSinceLastAttack = 0f;
            }
        } else if (isWalking() && currentPath != null && !currentPath.isEmpty()) {
            int[] targetPoint = currentPath.get(currentPathIndex);
            Vector2 targetPosition = calculateTargetPosition(targetPoint);
            moveTowardsTarget(targetPosition, delta);
            checkTargetReached(targetPosition);

            if (isNewPathSet) {
                updateDirection();
                isNewPathSet = false;
            }
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
        float yOffset = Tile.TILE_SIZE / 2f;
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
        float yOffset = Tile.TILE_SIZE / 2f;

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
        enemy.attacked(this);
        timeSinceLastAttack = 0f;
        setAttacking(true);
    }

    private void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
        if (isAttacking) {
            stateTime = 0;
        }
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

    private Animation<TextureRegion> getAttackAnimation() {
        switch (currentDirection) {
            case N:
                return attackAnimationN;
            case S:
                return attackAnimationS;
            default:
                throw new IllegalStateException("Invalid direction: " + currentDirection);
        }
    }

    private Animation<TextureRegion> getCurrentAnimation() {

        if (isAttacking) {
            currentAnimation = getAttackAnimation();
        } else {
            switch (currentDirection) {
                case N:
                    currentAnimation = isWalking() ? walkAnimationN : idleAnimationN;
                    break;
                case S:
                    currentAnimation = isWalking() ? walkAnimationS : idleAnimationS;
                    break;
                default:
                    throw new IllegalStateException("Invalid direction: " + currentDirection);
            }
        }
        return currentAnimation;
    }

    private enum Direction {
        N, S
    }
}