package ru.laptew.runAndCatch;

import com.almasb.fxgl.annotation.OnUserAction;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.input.InputMapping;
import com.almasb.fxgl.service.Input;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.input.KeyCode;

public class RunAndCatchApp extends GameApplication {
    private CharacterControl playerControl, enemyControl;
    private GameEntity player, enemy;

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

        input.addInputMapping(new InputMapping("Move Left", KeyCode.A));
        input.addInputMapping(new InputMapping("Move Up", KeyCode.W));
        input.addInputMapping(new InputMapping("Move Right", KeyCode.D));
        input.addInputMapping(new InputMapping("Move Down", KeyCode.S));
    }

    @OnUserAction(name = "Move Left")
    public void movePlayerLeft() {
        playerControl.moveLeft();
    }

    @OnUserAction(name = "Move Right")
    public void movePlayerRight() {
        playerControl.moveRight();
    }

    @OnUserAction(name = "Move Up")
    public void movePlayerUp() {
        playerControl.moveUp();
    }

    @OnUserAction(name = "Move Down")
    public void movePlayerDown() {
        playerControl.moveDown();
    }

    @Override
    protected void initGame() {

        getPhysicsWorld().setGravity(0, 0);
        getGameWorld().addEntity(Entities.makeScreenBounds(40));
        spawnPlayer();
        spawnEnemy();
    }

    private void spawnPlayer() {
        player = (GameEntity) getGameWorld().spawn("dawn", getWidth() / 2 - getWidth() / 4, getHeight() / 2);
        playerControl = player.getControlUnsafe(CharacterControl.class);
    }

    private void spawnEnemy() {
        enemy = (GameEntity) getGameWorld().spawn("policeman", getWidth() / 2 + getWidth() / 4, getHeight() / 2);
        enemyControl = enemy.getControlUnsafe(CharacterControl.class);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
