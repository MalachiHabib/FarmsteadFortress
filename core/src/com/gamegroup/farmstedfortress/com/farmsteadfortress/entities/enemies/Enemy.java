package com.farmsteadfortress.entities.enemies;

import static com.farmsteadfortress.utils.Helpers.gridToWorldPosition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.render.Tile;
import com.farmsteadfortress.render.TileMap;

import java.util.List;
import java.util.Random;

/**
 * Represents a generic enemy entity in the game.
 */
public abstract class Enemy {
    private static final float EFFECT_DURATION = 1.5f;
    private static final Color EFFECT_COLOR = Color.WHITE;
    protected List<Enemy> enemies;
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
    private boolean isWalking;
    private boolean reachedMiddle;
    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> idleAnimation, walkAnimationN, walkAnimationS, walkAnimationE, walkAnimationW;
    private Animation<TextureRegion> dieAnimation;
    private boolean isAttacked;
    private ShaderProgram attackShader;
    private Player player;
    private float effectTimer;
    private boolean isUnderEffect;
    private int attackDamage;
    private float timeSinceLastAttack = 0;
    private float timeBetweenAttacks;


    /**
     * Constructs an enemy entity.
     *
     * @param atlas          the texture atlas containing the walking animation frames
     * @param animationSpeed the speed of the walking animation
     * @param speed          the movement speed of the enemy
     * @param health         the enemy's health
     */
    public Enemy(Player player, TextureAtlas atlas, float animationSpeed, float speed, int health, int attackDamage, float timeBetweenAttacks, List<Enemy> enemies) {

        TextureAtlas idleAtlas = new TextureAtlas(Gdx.files.internal("entities/player/playerAnimation/idle/PlayerIdle.atlas"));
        idleAnimation = new Animation<TextureRegion>(animationSpeed, idleAtlas.getRegions(), Animation.PlayMode.LOOP);

        TextureAtlas walkAtlasN = new TextureAtlas(Gdx.files.internal("entities/Warhog/HogNorth.atlas"));
        TextureAtlas walkAtlasS = new TextureAtlas(Gdx.files.internal("entities/Warhog/HogSouth.atlas"));
        TextureAtlas walkAtlasE = new TextureAtlas(Gdx.files.internal("entities/Warhog/HogEast.atlas"));
        TextureAtlas walkAtlasW = new TextureAtlas(Gdx.files.internal("entities/Warhog/HogWest.atlas"));

        walkAnimationN = new Animation<TextureRegion>(animationSpeed, walkAtlasN.getRegions(), Animation.PlayMode.LOOP);
        walkAnimationS = new Animation<TextureRegion>(animationSpeed, walkAtlasS.getRegions(), Animation.PlayMode.LOOP);
        walkAnimationE = new Animation<TextureRegion>(animationSpeed, walkAtlasE.getRegions(), Animation.PlayMode.LOOP);
        walkAnimationW = new Animation<TextureRegion>(animationSpeed, walkAtlasW.getRegions(), Animation.PlayMode.LOOP);
        this.attackDamage = attackDamage;
        this.timeBetweenAttacks = timeBetweenAttacks;
        this.currentAnimation = walkAnimationN;
        this.isWalking = false;
        this.player = player;
        System.out.println(player.getHealth());
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
        this.enemies = enemies;
        this.isAttacked = false;
        this.attackShader = null;
        this.effectTimer = 0f;
        this.isUnderEffect = false;
        this.reachedMiddle = false;

        ShaderProgram.pedantic = false;
        String vertexShader = Gdx.files.internal("shaders/attackShader.vert").readString();
        String fragmentShader = Gdx.files.internal("shaders/attackShader.frag").readString();
        attackShader = new ShaderProgram(vertexShader, fragmentShader);

        if (!attackShader.isCompiled()) {
            System.err.println("Error compiling attack shader: " + attackShader.getLog());
        }
    }

    /**
     * Abstract method for the enemy being attacked, to be implemented by subclasses.
     *
     * @param damage, the amount of damage the enemy is hit.
     */
    public void attacked(int damage) {
        health -= damage;
        if (isDead()) {
            die();
        } else {
            startEffect();
        }
    }

    private void startEffect() {
        effectTimer = EFFECT_DURATION;
        isUnderEffect = true;
    }

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

    private void updateDirection() {
        if (currentPath != null && !currentPath.isEmpty()) {
            int[] coordinate = currentPath.get(currentPathIndex);
            int row = coordinate[0];
            int col = coordinate[1];
            Vector2 targetPosition = gridToWorldPosition(row, col, Tile.TILE_SIZE, (float) Tile.TILE_SIZE / 2);
            direction.set(targetPosition.x - position.x, targetPosition.y - position.y).nor();

            if (Math.abs(direction.y) > Math.abs(direction.x)) {
                if (direction.y > 0) {
                    currentAnimation = walkAnimationN;
                } else {
                    currentAnimation = walkAnimationS;
                }
            } else {
                if (direction.x > 0) {
                    currentAnimation = walkAnimationE;
                } else {
                    currentAnimation = walkAnimationW;
                }
            }
        }
    }

    private Animation<TextureRegion> getCurrentAnimation() {
        if (direction.y > 0) {
            return walkAnimationN;
        } else if (direction.y < 0) {
            return walkAnimationS;
        } else if (direction.x > 0) {
            return walkAnimationE;
        } else {
            return walkAnimationW;
        }
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

        updateDirection();
        timeSinceLastAttack += deltaTime;
        stateTime += deltaTime;

        boundingBox.set(position.x, position.y, walkingAnimation.getKeyFrame(stateTime).getRegionWidth(), walkingAnimation.getKeyFrame(stateTime).getRegionHeight());
        if (currentPath != null && !currentPath.isEmpty()) {
            if (currentPathIndex < currentPath.size() - 1) {
                int[] coordinate = currentPath.get(currentPathIndex);
                int row = coordinate[0];
                int col = coordinate[1];
                Vector2 targetPosition = gridToWorldPosition(row, col, Tile.TILE_SIZE, (float) Tile.TILE_SIZE / 2);
                Vector2 direction = new Vector2(targetPosition.x - position.x, targetPosition.y - position.y).nor();
                position.x += direction.x * speed * deltaTime;
                position.y += direction.y * speed * deltaTime;

                float distanceToTarget = position.dst(targetPosition);
                if (distanceToTarget <= speed * deltaTime) {
                    position.set(targetPosition);
                    currentPathIndex++;
                }
                isWalking = true;
            } else {
                isWalking = false;
                reachedMiddle = true;
                attackCenter();
            }
        } else {
            isWalking = false;
        }

        if (isUnderEffect) {
            effectTimer -= deltaTime;
            if (effectTimer <= 0f) {
                isUnderEffect = false;
            }
        }
    }

    public void attackCenter() {
        if (reachedMiddle && canAttack()) {
            player.attacked(attackDamage);
            timeSinceLastAttack = 0f;
        }
    }

    public boolean canAttack() {
        return timeSinceLastAttack >= timeBetweenAttacks;
    }
    /**
     * Sets the path for the enemy to follow.
     *
     * @param map the tile map
     */
    public void setPath(TileMap map) {
        List<List<int[]>> paths = map.getEnemyPaths();
        if (!paths.isEmpty()) {
            Random rand = new Random();
            currentPath = paths.get(rand.nextInt(paths.size()));
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
     * Renders the enemy on the screen.
     *
     * @param batch the sprite batch used for rendering
     */
    public void render(SpriteBatch batch) {
        Animation<TextureRegion> currentAnimation = getCurrentAnimation();
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        float yOffset = Tile.TILE_SIZE / 2f + 15f;
        float posX = position.x;
        float posY = position.y + yOffset;

        if (isUnderEffect) {
            batch.setShader(attackShader);
            attackShader.setUniformf("u_color", EFFECT_COLOR.r, EFFECT_COLOR.g, EFFECT_COLOR.b, 1f);
        }

        batch.draw(currentFrame, posX, posY, currentFrame.getRegionWidth() / 2, currentFrame.getRegionHeight() / 2, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), 1, 1, 0);

        if (isUnderEffect) {
            batch.setShader(null);
        }
    }

    public boolean isDead() {
        return health <= 0;
    }
}