package ru.laptew.runAndCatch;

import com.almasb.fxgl.ecs.Entity;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.physics.CollisionHandler;

import java.util.Arrays;
import java.util.List;

public class BlunderManager extends CollisionHandler {

    private List<GameEntity> potentialBlunderList;

    public BlunderManager(GameEntity... potentialBlunders) {
        super(RunAndCatchType.CHARACTER, RunAndCatchType.CHARACTER);

        potentialBlunderList = Arrays.asList(potentialBlunders);
    }

    @Override
    protected void onCollision(Entity character, Entity character1) {
        if (!potentialBlunderList.contains(character) || !potentialBlunderList.contains(character1)) {
            return;
        }

        System.out.println("Collision has appear.");
    }
}
