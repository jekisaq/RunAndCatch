package ru.laptew.runAndCatch.managers;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.ecs.Entity;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.component.IDComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import ru.laptew.runAndCatch.RunAndCatchType;
import ru.laptew.runAndCatch.component.BotComponent;

import java.util.Arrays;
import java.util.List;

public class BlunderManager extends CollisionHandler {

    private List<GameEntity> potentialBlunderList;

    private ObjectProperty<GameEntity> currentBlunder;

    public BlunderManager(GameEntity... potentialBlunders) {
        super(RunAndCatchType.CHARACTER, RunAndCatchType.CHARACTER);

        potentialBlunderList = Arrays.asList(potentialBlunders);
    }

    @Override
    protected void onCollision(Entity primaryEntity, Entity secondaryEntity) {
        IDComponent characterIDComponent = primaryEntity.getComponentUnsafe(IDComponent.class);

        GameEntity blunder, blundered;

        if (characterIDComponent == currentBlunder.get().getComponentUnsafe(IDComponent.class)) {
            blunder = (GameEntity) primaryEntity;
            blundered = (GameEntity) secondaryEntity;
        } else {
            blunder = (GameEntity) secondaryEntity;
            blundered = (GameEntity) primaryEntity;
        }

        if (!potentialBlunderList.contains(blunder) || !potentialBlunderList.contains(blundered)) {
            return;
        }


        currentBlunder.setValue(blundered);

        if (blunder.hasComponent(BotComponent.class)) {
            blunder.getComponentUnsafe(BotComponent.class).makeBotMovingAway();

        } else if (blundered.hasComponent(BotComponent.class)) {
            blundered.getComponentUnsafe(BotComponent.class).makeBotProximity();
        }


    }

    public void defineBlunder() {
        GameEntity blunder = potentialBlunderList.get(FXGLMath.random(0, potentialBlunderList.size() - 1));
        if (blunder == null) {
            FXGL.getLogger(BlunderManager.class).fatal("No blunder was defined");
            throw new IllegalStateException("No blunder was defined");
        }

        currentBlunder = new SimpleObjectProperty<>(blunder);

        if (blunder.hasComponent(BotComponent.class)) {
            blunder.getComponentUnsafe(BotComponent.class).makeBotProximity();
        } else {
            makeBotsRunning();
        }
    }

    private void makeBotsRunning() {
        for (GameEntity entity : potentialBlunderList) {
            if (entity.hasComponent(BotComponent.class)) {
                entity.getComponentUnsafe(BotComponent.class).makeBotMovingAway();
            }
        }
    }

    public GameEntity getCurrentBlunder() {
        return currentBlunder.get();
    }

    public ObjectProperty<GameEntity> getCurrentBlunderProperty() {
        return currentBlunder;
    }
}
