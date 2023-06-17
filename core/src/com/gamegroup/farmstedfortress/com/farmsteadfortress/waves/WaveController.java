package com.farmsteadfortress.waves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.farmsteadfortress.entities.Player;
import com.farmsteadfortress.entities.enemies.BossEnemy;
import com.farmsteadfortress.entities.enemies.Enemy;
import com.farmsteadfortress.entities.enemies.EnemyFactory;

import java.util.List;
import java.util.Random;

public class WaveController {

    private int currentWaveNumber;
    private List<Enemy> enemies;
    private float spawnInterval;
    private float timeSinceLastSpawn;
    private Wave currentWave;
    private ShapeRenderer shapeRenderer;
    private int cumulativeEnemyCount = 0;

    private boolean bossSpawned = false;
    public static boolean waveStarted;

    public WaveController(List<Enemy> enemies, Player player) {
        this.enemies = enemies;
        this.currentWaveNumber = 0;
        this.timeSinceLastSpawn = 0f;
        this.spawnInterval = 0.4f + new Random().nextFloat() * (0.6f - 0.4f);
        this.currentWave = generateNextWave();
        this.shapeRenderer = new ShapeRenderer();
        EnemyFactory enemyFactory = new EnemyFactory();
        enemyFactory.setEnemies(enemies);
        enemyFactory.setPlayer(player);
    }

    public BossEnemy getBossEnemy() {
        for (Enemy enemy : enemies) {
            if (enemy instanceof BossEnemy) {
                return (BossEnemy) enemy;
            }
        }
        return null;
    }

    public boolean isBossSpawned() {
        return bossSpawned;
    }

    public void renderBossHealth() {
        BossEnemy boss = getBossEnemy();

        if (boss != null) {
            float healthPercentage = boss.getHealth() / (float) boss.getMaxHealth();

            float barWidth = 300;
            float barHeight = 30;
            float barX = (Gdx.graphics.getWidth() - barWidth) / 2;
            float barY = Gdx.graphics.getHeight() - barHeight - 10;

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(barX - 2, barY - 2, barWidth + 4, barHeight + 4);

            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(barX, barY, barWidth, barHeight);

            Color startColor = Color.GREEN;
            Color endColor = Color.RED;
            Color healthColor = startColor.cpy().lerp(endColor, 1 - healthPercentage);
            shapeRenderer.setColor(healthColor);
            shapeRenderer.rect(barX, barY, barWidth * healthPercentage, barHeight);

            shapeRenderer.end();
        }
    }

    private Wave generateNextWave() {
        EnemyFactory.EnemyType enemyType = EnemyFactory.EnemyType.BASIC_ENEMY;
        int enemyCount = currentWaveNumber < 6 ? currentWaveNumber : (int) Math.min(50, Math.pow(2, (currentWaveNumber - 5) / 6f));

        if (currentWaveNumber % 6 == 0) {
            enemyType = EnemyFactory.EnemyType.BOSS_ENEMY;
            enemyCount = 1;
            cumulativeEnemyCount += enemyCount;
        }
        else if (cumulativeEnemyCount > 30) {
            cumulativeEnemyCount++;
        }

        else {
            cumulativeEnemyCount += enemyCount;
        }

        return new Wave(currentWaveNumber++, enemyType, (enemyType == EnemyFactory.EnemyType.BOSS_ENEMY) ? enemyCount : cumulativeEnemyCount);
    }

    public void startWave() {
        if (!waveStarted && isWaveOver()) {
            waveStarted = true;
            timeSinceLastSpawn = 0;
            currentWave = generateNextWave();
        }
    }

    public Wave getCurrentWave() {
        return currentWave;
    }

    public void update(float deltaTime) {
        if (waveStarted) {
            timeSinceLastSpawn += deltaTime;
            if (timeSinceLastSpawn >= spawnInterval) {
                if (currentWave.getEnemyCount() > 0) {
                    spawnEnemy(currentWave.getEnemyType());
                    currentWave.setEnemyCount(currentWave.getEnemyCount() - 1);
                    timeSinceLastSpawn -= spawnInterval;
                    spawnInterval = 0.4f + new Random().nextFloat() * (0.6f - 0.4f);
                }
            }
            if (isWaveOver()) {
                waveStarted = false;
                currentWave = generateNextWave();
            }
        }
    }

    private void spawnEnemy(EnemyFactory.EnemyType enemyType) {
        Enemy enemy = EnemyFactory.createEnemy(enemyType);
        enemies.add(enemy);

        if (currentWave.getWaveNumber() % 6 == 0) {
            if (enemy instanceof BossEnemy) {
                bossSpawned = true;
            } else {
                enemy.increaseHealth();
            }

        }
    }


    public boolean isWaveOver() {
        if (waveStarted) {
            if (currentWave.getEnemyCount() != 0) {
                return false;
            }
            for (Enemy enemy : enemies) {
                if (!enemy.isDead()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean gameOver() {
        return currentWave.getWaveNumber() >= 50;
    }

    public void stopWave() {
        if (waveStarted) {
            waveStarted = false;
        }
    }

    public void setBossSpawned(boolean bossSpawned) {
        this.bossSpawned = bossSpawned;
    }
}