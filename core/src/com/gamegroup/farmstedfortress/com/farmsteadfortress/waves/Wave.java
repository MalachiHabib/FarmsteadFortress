package com.farmsteadfortress.waves;

import com.farmsteadfortress.entities.enemies.EnemyFactory;

public class Wave {
    private int waveNumber;
    private EnemyFactory.EnemyType enemyType;
    private int enemyCount;

    public Wave(int number, EnemyFactory.EnemyType enemyType, int enemyCount) {
        this.waveNumber = number;
        this.enemyType = enemyType;
        this.enemyCount = enemyCount;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public EnemyFactory.EnemyType getEnemyType() {
        return enemyType;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }
}