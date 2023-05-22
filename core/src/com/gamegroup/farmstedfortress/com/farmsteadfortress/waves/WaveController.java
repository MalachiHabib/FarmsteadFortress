package com.farmsteadfortress.waves;

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
    private boolean waveStarted = false;

    public WaveController(List<Enemy> enemies) {
        this.enemies = enemies;
        this.currentWaveNumber = 1;
        this.timeSinceLastSpawn = 0f;
        this.spawnInterval = 0.4f + new Random().nextFloat() * (0.6f - 0.4f);

        this.currentWave = generateNextWave();
    }

    private Wave generateNextWave() {
        EnemyFactory.EnemyType enemyType = EnemyFactory.EnemyType.BASIC_ENEMY;
        int enemyCount = (int) Math.pow(2, currentWaveNumber / 6);
        if (currentWaveNumber % 6 == 0) {
            enemyType = EnemyFactory.EnemyType.BASIC_ENEMY; //Change to boss when implemented
            enemyCount = 1;
        }
        return new Wave(currentWaveNumber++, enemyType, enemyCount);
    }

    public void startWave() {
        if (!waveStarted) {
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
                } else {
                    waveStarted = false;
                }
            }
        }
    }

    private void spawnEnemy(EnemyFactory.EnemyType enemyType) {
        Enemy enemy = EnemyFactory.createEnemy(enemyType);
        enemies.add(enemy);
    }

    public boolean isWaveOver() {
        if (waveStarted) {
            return currentWave.getEnemyCount() == 0;
        }
        return true;
    }

    public void stopWave() {
        if (waveStarted) {
            waveStarted = false;
        }
    }
}