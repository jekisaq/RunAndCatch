package ru.laptew.runAndCatch;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.scene.menu.MenuStyle;
import com.almasb.fxgl.service.Input;
import com.almasb.fxgl.settings.GameSettings;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class RunAndCatchApp extends GameApplication {
    private CharacterControl playerControl;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Догонялки");
        gameSettings.setWidth(600);
        gameSettings.setHeight(400);
        gameSettings.setIntroEnabled(false);
        gameSettings.setProfilingEnabled(false);
        gameSettings.setCloseConfirmation(false);
        gameSettings.setMenuEnabled(false);
        gameSettings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                playerControl.moveLeft();
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                playerControl.moveRight();
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                playerControl.moveUp();
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                playerControl.moveDown();
            }
        }, KeyCode.S);
    }

    @Override
    protected void initGame() {
        playerControl = new CharacterControl();

        GameEntity player = new GameEntity();
        player.getBoundingBoxComponent().addHitBox(new HitBox("BODY",
                new Point2D(5, 5), BoundingShape.box(27, 31)));

        player.addControl(playerControl);

        getGameWorld().addEntity(player);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
