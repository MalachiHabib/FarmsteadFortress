package com.farmsteadfortress.projectiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.render.Tile;

public class Projectile {
    private Vector2 position;
    private Vector2 direction;
    private TextureRegion texture;
    private boolean hitTarget = false;
    private float rotation = 0;
    private TextureRegion splatterTexture;
    private int damage;
    private float speed;
    private Enemy target;
    private float timeSinceHit = 0;
    private static final float TIME_TO_LIVE_AFTER_HIT = 1f;

    public Projectile(Vector2 position, Vector2 direction, TextureRegion texture, TextureRegion splatterTexture, int damage, float speed, Enemy target) {
        this.position = position;
        this.direction = direction.nor();
        this.texture = texture;
        this.damage = damage;
        this.speed = speed;
        this.target = target;
        this.splatterTexture = splatterTexture;
    }

    public void render(SpriteBatch batch) {
        TextureRegion textureToUse = hitTarget ? splatterTexture : texture;

        float textureWidth = textureToUse.getRegionWidth();
        float textureHeight = textureToUse.getRegionHeight();
        float originX = textureWidth / 2f;
        float originY = textureHeight / 2f;

        batch.draw(
                textureToUse,
                position.x - originX,
                position.y - originY + 15f,
                originX,
                originY,
                textureWidth,
                textureHeight,
                1,
                1,
                rotation
        );
    }

    public void update(float delta) {
        if (!hitTarget) {
            direction.set(target.getPosition().x - position.x, target.getPosition().y - position.y).nor();
            position.x += direction.x * speed * delta;
            position.y += direction.y * speed * delta;
            rotation += 360 * delta;
        } else {
            timeSinceHit += delta;
        }
    }

    public boolean shouldRemove() {
        return hitTarget && timeSinceHit >= TIME_TO_LIVE_AFTER_HIT;
    }

    public void onHit() {
        System.out.println(!target.isHit());
        if (!target.isHit()) {
            target.setHitBy(this);
            target.attacked(damage);
            hitTarget = true;
        }
    }

    public Enemy getTarget() {
        return target;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public float getRadius() {
        return this.texture.getRegionWidth() / 2f;
    }
}
