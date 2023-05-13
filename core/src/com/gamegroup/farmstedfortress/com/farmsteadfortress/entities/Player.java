package com.farmsteadfortress.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.render.Tile;

public class Player {
    private Animation<TextureRegion> walkingAnimation;
    private float stateTime;
    private Vector2 direction;
    private Vector2 targetPosition;
    private Vector2 position;
    private float speed;

    public Player(TextureAtlas atlas, float animationSpeed, float speed, Vector2 spawnPosition) {
        this.walkingAnimation = new Animation<TextureRegion>(animationSpeed, atlas.getRegions());
        this.stateTime = 0f;
        this.position = spawnPosition;
        this.speed = speed;
        this.direction = new Vector2(1, 0); // Initial direction could be any unit vector
    }

    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition = targetPosition;
    }

    public void update(float delta) {
        if (targetPosition != null) {
            Vector2 direction = new Vector2(targetPosition.x - position.x, targetPosition.y - position.y).nor();
            position.x += direction.x * speed * delta;
            position.y += direction.y * speed * delta;

            // If the player is close enough to the target, stop moving and snap to the exact target position
            if (position.dst(targetPosition) < 5f) {
                position.set(targetPosition);
            }
        }

        stateTime += Gdx.graphics.getDeltaTime();
    }


    private float getRotationAngle() {
        float angle = direction.angleDeg();
        return angle;
    }

    public void render(SpriteBatch batch) {
        float yOffset = Tile.TILE_SIZE / 2f;
        float angle = getRotationAngle();
        float originX = walkingAnimation.getKeyFrame(stateTime).getRegionWidth() / 2f;
        float originY = walkingAnimation.getKeyFrame(stateTime).getRegionHeight() / 2f;

        TextureRegion currentFrame = walkingAnimation.getKeyFrame(stateTime, true);
        float posX = position.x - originX + (Tile.TILE_SIZE - currentFrame.getRegionWidth() );
        float posY = position.y - originY + yOffset + (Tile.TILE_SIZE - currentFrame.getRegionHeight());

        batch.draw(currentFrame,
                posX, posY,
                originX, originY,
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),
                1, 1,
                angle);
    }

    public boolean hasReachedTarget() {
        if (targetPosition != null) {
            // Check if the player's position is within some small threshold of the target position
            boolean hasReached = position.epsilonEquals(targetPosition, Tile.TILE_SIZE / 5f);
            if (hasReached) {
                System.out.println("Player has reached target");
            }
            return hasReached;
        }
        return false;
    }


    public boolean isMoving() {
        return targetPosition != null;
    }
}
