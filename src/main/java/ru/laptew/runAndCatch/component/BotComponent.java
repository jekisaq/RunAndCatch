package ru.laptew.runAndCatch.component;

import com.almasb.fxgl.ecs.AbstractComponent;
import com.almasb.fxgl.entity.GameEntity;
import ru.laptew.runAndCatch.control.AIProximityControl;
import ru.laptew.runAndCatch.control.AIRunControl;

public class BotComponent extends AbstractComponent {

    private GameEntity target;

    public BotComponent(GameEntity target) {
        this.target = target;
    }

    public void makeBotProximity() {
        if (getEntity().hasControl(AIRunControl.class)) {
            getEntity().removeControl(AIRunControl.class);
        }

        getEntity().addControl(new AIProximityControl(target));
    }

    public void makeBotMovingAway() {
        if (getEntity().hasControl(AIProximityControl.class)) {
            getEntity().removeControl(AIProximityControl.class);
        }

        getEntity().addControl(new AIRunControl(target));
    }
}
