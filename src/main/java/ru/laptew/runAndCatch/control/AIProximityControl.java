package ru.laptew.runAndCatch.control;

import com.almasb.fxgl.ecs.AbstractControl;
import com.almasb.fxgl.ecs.Entity;
import com.almasb.fxgl.entity.component.PositionComponent;

public class AIProximityControl extends AbstractControl  {
    private CharacterControl characterControl;
    private Entity target;

    public AIProximityControl(Entity target) {
        this.target = target;
    }

    @Override
    public void onAdded(Entity entity) {
        this.characterControl = entity.getControl(CharacterControl.class).orElseThrow(IllegalStateException::new);
    }

    @Override
    public void onUpdate(Entity entity, double timePerFrame) {
        PositionComponent entityPosition = entity.getComponent(PositionComponent.class).orElseThrow(IllegalStateException::new);
        PositionComponent targetPosition = target.getComponent(PositionComponent.class).orElseThrow(IllegalStateException::new);

        if (entityPosition.getX() > targetPosition.getX()) {
            if (entityPosition.getY() > targetPosition.getY()) {
                characterControl.moveUp();
            } else {
                characterControl.moveLeft();
            }

        } else {
            if (entityPosition.getY() < targetPosition.getY()) {
                characterControl.moveDown();
            } else {
                characterControl.moveRight();
            }
        }
    }
}
