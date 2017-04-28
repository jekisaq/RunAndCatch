package ru.laptew.runAndCatch;

import com.almasb.fxgl.annotation.OnUserAction;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.component.IDComponent;
import com.almasb.fxgl.input.InputMapping;
import com.almasb.fxgl.service.Input;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ru.laptew.runAndCatch.component.BotComponent;
import ru.laptew.runAndCatch.control.CharacterControl;
import ru.laptew.runAndCatch.managers.BlunderManager;

import java.util.*;

public class RunAndCatchApp extends GameApplication {
    private CharacterControl playerControl;
    private GameEntity player, rival;
    private BlunderManager blunderManager;
    private CharacterControl rivalControl;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Blunders");
        gameSettings.setVersion("1.0");
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
        input.addInputMapping(new InputMapping("Pause", KeyCode.ENTER));
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

    @OnUserAction(name = "Pause")
    public void pauseGame() {
        if (getGameState().getBoolean("isPaused")) {
            playerControl.resume();
            rivalControl.resume();

            getGameState().setValue("isPaused", false);
        } else {
            playerControl.pause();
            rivalControl.pause();

            getGameState().setValue("isPaused", true);
        }

    }

    @Override
    protected void initGame() {

        getPhysicsWorld().setGravity(0, 0);
        getGameWorld().addEntity(Entities.makeScreenBounds(40));
        initBackground();
        spawnPlayer();
        spawnRival();
        spawnRocks();

        blunderManager = new BlunderManager(player, rival);
        blunderManager.defineBlunder();

        getPhysicsWorld().addCollisionHandler(blunderManager);

        getMasterTimer().runAtInterval(() -> getGameState().increment("time", -1), Duration.seconds(1));
    }

    private void initBackground() {
        EntityView backgroundView = new EntityView(getAssetLoader().loadTexture("background.png", getWidth(), getHeight()));
        getGameScene().addGameView(backgroundView);
    }


    private void spawnRocks() {
        int rockCount = FXGLMath.random(10, 25);

        for (int i = 0; i < rockCount; i++) {
            getGameWorld().spawn("rock");
        }
    }

    private void spawnPlayer() {
        player = (GameEntity) getGameWorld().spawn("dawn", getWidth() / 2 - getWidth() / 4, getHeight() / 2);
        playerControl = player.getControlUnsafe(CharacterControl.class);
        addToStatistics(player);
    }

    private void spawnRival() {
        rival = (GameEntity) getGameWorld().spawn("policeman", getWidth() / 2 + getWidth() / 4, getHeight() / 2);
        rivalControl = rival.getControlUnsafe(CharacterControl.class);
        rival.addComponent(new BotComponent(player));
        addToStatistics(player);
    }

    private void addToStatistics(GameEntity player) {
        HashMap<Integer, List<Integer>> statistics = getGameState().getObject("statistics");
        statistics.put(player.getComponentUnsafe(IDComponent.class).getID(), new LinkedList<>());
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("isPaused", false);
        vars.put("time", 190);
        vars.put("statistics", new HashMap<Integer, List<Integer>>());
    }

    @Override
    protected void initUI() {
        Text uiTime = getUIFactory().newText("Время: ", Color.BLACK, 18);
        uiTime.setTranslateX(getWidth() - 125);
        uiTime.setTranslateY(20);
        uiTime.textProperty().bind(getGameState().intProperty("time").asString("Время: %d"));


        Label uiBlunder = new Label();
        uiBlunder.setFont(new Font(18));
        uiBlunder.setText("Ляпа — " + blunderManager.getCurrentBlunderIDComponent().getName());

        blunderManager.getCurrentBlunderIDComponentProperty().addListener((observable, oldValue, newValue) ->
                uiBlunder.setText("Ляпа — " + newValue.getName()));

        Text uiPause = getUIFactory().newText("PAUSE", Color.FUCHSIA, 48);

        getUIFactory().centerText(uiPause);

        uiPause.visibleProperty().bind(getGameState().booleanProperty("isPaused"));

        getGameScene().addUINode(uiBlunder);
        getGameScene().addUINode(uiPause);
        getGameScene().addUINode(uiTime);
    }

    @Override
    protected void onPostUpdate(double tpf) {
        if (getGameState().getInt("time") <= 0) {
            getDisplay().showMessageBox("Игра окончена!", this::exit);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
