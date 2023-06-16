package com.farmsteadfortress.projectiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProjectileManager {
    private List<Projectile> projectiles;
    private float timeSinceLastHit = 0;

    public ProjectileManager() {
        projectiles = new ArrayList<>();
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    public void update(float delta) {
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.update(delta);
            if (projectile.getTarget().isHitBy(projectile)) {
                projectile.onHit();
                timeSinceLastHit += delta;
            }

            if (projectile.shouldRemove()) {
                projectile.getTarget().resetHit();
                iterator.remove();
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Projectile projectile : projectiles) {
            projectile.render(batch);
        }
    }
}
