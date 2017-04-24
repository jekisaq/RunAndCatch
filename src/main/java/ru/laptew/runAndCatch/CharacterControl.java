package ru.laptew.runAndCatch;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.ecs.AbstractControl;
import com.almasb.fxgl.ecs.Entity;
import com.almasb.fxgl.ecs.component.Required;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.component.PositionComponent;
import com.almasb.fxgl.entity.component.ViewComponent;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Rectangle2D;

@Required(PositionComponent.class)
public class CharacterControl extends AbstractControl {
    private static final int CHARACTER_WIDTH = 32;
    private static final int CHARACTER_HEIGHT = 32;

    private static final int FRAME_COUNT = 4;


    public enum MovementDirection {
        DOWN, UP, LEFT, RIGHT
    }

    private PositionComponent position;
    private Texture characterSpriteSheet;

    private int currentFrame;
    private MovementDirection movementDirection;

    private double speed;

    @Override
    public void onAdded(Entity entity) {
        movementDirection = MovementDirection.DOWN;
        currentFrame = 0;

        position = Entities.getPosition(entity);
        ViewComponent viewComponent = Entities.getView(entity);

        characterSpriteSheet = FXGL.getAssetLoader().loadTexture("dawnRun.png");
        viewComponent.setView(characterSpriteSheet);
    }

    @Override
    public void onUpdate(Entity entity, double timePerFrame) {
        speed = timePerFrame * 60;

        characterSpriteSheet.setViewport(new Rectangle2D(
                currentFrame * CHARACTER_WIDTH,
                movementDirection.ordinal() * CHARACTER_HEIGHT,
                CHARACTER_WIDTH, CHARACTER_HEIGHT));

    }

    public void moveUp() {
        if (movementDirection != MovementDirection.UP) {
            movementDirection = MovementDirection.UP;
            currentFrame = -1;
        }

        position.translateY(-5 * speed);
        currentFrame = ++currentFrame % FRAME_COUNT;

    }

    public void moveDown() {
        if (movementDirection != MovementDirection.DOWN) {
            movementDirection = MovementDirection.DOWN;
            currentFrame = -1;
        }

        position.translateY(5 * speed);
        currentFrame = ++currentFrame % FRAME_COUNT;
    }

    public void moveLeft() {
        if (movementDirection != MovementDirection.LEFT) {
            movementDirection = MovementDirection.LEFT;
            currentFrame = -1;
        }

        position.translateX(-5 * speed);
        currentFrame = ++currentFrame % FRAME_COUNT;
    }

    public void moveRight() {
        if (movementDirection != MovementDirection.RIGHT) {
            movementDirection = MovementDirection.RIGHT;
            currentFrame = -1;
        }

        position.translateX(5 * speed);
        currentFrame = ++currentFrame % FRAME_COUNT;
    }
}
