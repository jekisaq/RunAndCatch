package ru.laptew.runAndCatch.control;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.ecs.AbstractControl;
import com.almasb.fxgl.ecs.Entity;
import com.almasb.fxgl.entity.component.PositionComponent;

import static java.lang.Math.abs;

public class AIRunControl extends AbstractControl {

    private CharacterControl characterControl;
    private CharacterControl.MovementDirection lastMovementDirection = CharacterControl.MovementDirection.DOWN;
    private Entity target;

    public AIRunControl(Entity target) {
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


        if (entityPosition.distance(targetPosition) > 100) {
            runToAnyDirection();
        } else {
            runFromTarget(entityPosition, targetPosition);
        }

    }

    private void runFromTarget(PositionComponent entityPosition, PositionComponent targetPosition) {
        if (abs(entityPosition.getX() - targetPosition.getX()) < 40) {
            runVertical(entityPosition, targetPosition);
        } else if (abs(entityPosition.getY() - targetPosition.getY()) < 40) {
            if (entityPosition.getX() < targetPosition.getX()) {

                characterControl.moveLeft();
            } else {
                characterControl.moveRight();
            }
        } else {
            if (entityPosition.getX() > targetPosition.getX()) {
                characterControl.moveRight();
            } else {
                characterControl.moveLeft();
            }
        }

    }

    private void runToAnyDirection() {
        if (FXGLMath.randomBoolean(0.05f)) {
            lastMovementDirection = CharacterControl.MovementDirection.values()[FXGLMath.random(0, 3)];
        }

        switch (lastMovementDirection) {
            case DOWN:
                characterControl.moveDown();
                break;
            case UP:
                characterControl.moveUp();
                break;
            case LEFT:
                characterControl.moveLeft();
                break;
            case RIGHT:
                characterControl.moveRight();
                break;
        }
    }

    private void runVertical(PositionComponent entityPosition, PositionComponent targetPosition) {
        if (entityPosition.getY() > targetPosition.getY()) {
            characterControl.moveDown();
        } else {
            characterControl.moveUp();
        }
    }
}
