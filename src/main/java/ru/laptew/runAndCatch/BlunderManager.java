package ru.laptew.runAndCatch;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.ecs.Entity;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.component.IDComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import ru.laptew.runAndCatch.component.BotComponent;

import java.util.Arrays;
import java.util.List;

public class BlunderManager extends CollisionHandler {

    private List<GameEntity> potentialBlunderList;

    private ObjectProperty<IDComponent> currentBlunderIDComponent;

    public BlunderManager(GameEntity... potentialBlunders) {
        super(RunAndCatchType.CHARACTER, RunAndCatchType.CHARACTER);

        potentialBlunderList = Arrays.asList(potentialBlunders);
    }

    @Override
    protected void onCollision(Entity character, Entity character2) {
        if (!potentialBlunderList.contains(character) || !potentialBlunderList.contains(character2)) {
            return;
        }

        IDComponent blunderIDComponent, blunderedIDComponent,
                characterIDComponent = character.getComponent(IDComponent.class).orElseThrow(IllegalStateException::new);
        IDComponent character2IDComponent = character2.getComponent(IDComponent.class).orElseThrow(IllegalStateException::new);

        if (characterIDComponent == currentBlunderIDComponent.get()) {
            blunderIDComponent = characterIDComponent;
            blunderedIDComponent = character2IDComponent;
        } else {
            blunderIDComponent = character2IDComponent;
            blunderedIDComponent = characterIDComponent;
        }

        currentBlunderIDComponent.setValue(blunderedIDComponent);

        BotComponent botComponent = blunderIDComponent.getEntity().getComponentUnsafe(BotComponent.class);
        if (botComponent != null) {
            botComponent.makeBotMovingAway();
        } else {
            botComponent = character2.getComponentUnsafe(BotComponent.class);
            if (botComponent != null) {
                botComponent.makeBotProximity();
            }

        }
    }

    public void defineBlunder() {
        GameEntity blunder = potentialBlunderList.get(FXGLMath.random(0, potentialBlunderList.size() - 1));
        if (blunder == null) {
            FXGL.getLogger(BlunderManager.class).fatal("No blunder was defined");
            throw new IllegalStateException("No blunder was defined");
        }

        currentBlunderIDComponent = new SimpleObjectProperty<>(
                blunder.getComponent(IDComponent.class).orElseThrow(IllegalStateException::new));

        BotComponent botComponent = blunder.getComponentUnsafe(BotComponent.class);
        if (botComponent != null) {
            botComponent.makeBotProximity();
        }
    }

    public IDComponent getCurrentBlunderIDComponent() {
        return currentBlunderIDComponent.get();
    }

    public ObjectProperty<IDComponent> getCurrentBlunderIDComponentProperty() {
        return currentBlunderIDComponent;
    }
}
