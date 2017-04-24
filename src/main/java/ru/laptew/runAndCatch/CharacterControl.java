package ru.laptew.runAndCatch;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.ecs.AbstractControl;
import com.almasb.fxgl.ecs.Entity;
import com.almasb.fxgl.ecs.component.Required;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.component.ViewComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

@SuppressWarnings("ALL")
@Required(PhysicsComponent.class)
public class CharacterControl extends AbstractControl {
    private static final int CHARACTER_WIDTH = 32;
    private static final int CHARACTER_HEIGHT = 32;

    private static final int INSTANT_SPEED = 2 * 60;
    private static final int MAX_SPEED = 4 * 60;
    private static final double SLOW_FACTOR = 0.5;

    private static final int FRAME_COUNT = 4;
    private static final int FRAME_DELAY = 6;


    public enum MovementDirection {
        DOWN, UP, LEFT, RIGHT
    }

    private PhysicsComponent physics;
    private Texture characterSpriteSheet;

    private int currentFrame;
    private MovementDirection movementDirection;

    private Point2D acceleration = Point2D.ZERO;

    private double lastTimePerFrame;

    @Override
    public void onAdded(Entity entity) {
        movementDirection = MovementDirection.DOWN;
        currentFrame = 0;

        physics = Entities.getPhysics(entity);

        ViewComponent viewComponent = Entities.getView(entity);

        characterSpriteSheet = FXGL.getAssetLoader().loadTexture("policemanRun.png");
        viewComponent.setView(characterSpriteSheet);
    }

    @Override
    public void onUpdate(Entity entity, double timePerFrame) {
        lastTimePerFrame = timePerFrame;

        physics.setLinearVelocity(acceleration);

        limitVelocity();

        acceleration = new Point2D(acceleration.multiply(SLOW_FACTOR).getX(), acceleration.multiply(SLOW_FACTOR).getY());


        characterSpriteSheet.setViewport(new Rectangle2D(
                currentFrame / FRAME_DELAY * CHARACTER_WIDTH,
                movementDirection.ordinal() * CHARACTER_HEIGHT,
                CHARACTER_WIDTH, CHARACTER_HEIGHT));

    }

    public int getFrameCount() {
        return FRAME_COUNT * FRAME_DELAY;
    }

    private void limitVelocity() {
        Point2D vel = physics.getLinearVelocity();

        if (vel.getX() < -MAX_SPEED)
            vel = new Point2D(-MAX_SPEED, vel.getY());

        if (vel.getX() > MAX_SPEED)
            vel = new Point2D(MAX_SPEED, vel.getY());

        if (vel.getY() < -MAX_SPEED)
            vel = new Point2D(vel.getX(), -MAX_SPEED);

        if (vel.getY() > MAX_SPEED)
            vel = new Point2D(vel.getX(), MAX_SPEED);

        physics.setLinearVelocity(vel);
    }

    public void moveUp() {
        if (movementDirection != MovementDirection.UP) {
            movementDirection = MovementDirection.UP;
            acceleration = Point2D.ZERO;
            currentFrame = -1;
        } else {
            acceleration = acceleration.add(0, INSTANT_SPEED * lastTimePerFrame * -100);
        }


        currentFrame = ++currentFrame % getFrameCount();

    }

    public void moveDown() {
        if (movementDirection != MovementDirection.DOWN) {
            movementDirection = MovementDirection.DOWN;
            acceleration = Point2D.ZERO;
            currentFrame = -1;
        } else {
            acceleration = acceleration.add(0, INSTANT_SPEED * lastTimePerFrame * 100);
        }

        currentFrame = ++currentFrame % getFrameCount();
    }

    public void moveLeft() {
        if (movementDirection != MovementDirection.LEFT) {
            movementDirection = MovementDirection.LEFT;
            acceleration = Point2D.ZERO;
            currentFrame = -1;
        } else {
            acceleration = acceleration.add(INSTANT_SPEED * lastTimePerFrame * -100, 0);
        }

        currentFrame = ++currentFrame % getFrameCount();
    }

    public void moveRight() {
        if (movementDirection != MovementDirection.RIGHT) {
            acceleration = Point2D.ZERO;
            movementDirection = MovementDirection.RIGHT;
            currentFrame = -1;
        } else {
            acceleration = acceleration.add(INSTANT_SPEED * lastTimePerFrame * 100, 0);
        }


        currentFrame = ++currentFrame % getFrameCount();
    }
}
