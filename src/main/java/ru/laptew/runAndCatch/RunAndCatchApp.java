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

    private HashMap<GameEntity, List<Integer>> statistics = new HashMap<>();
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

        getMasterTimer().runAtInterval(() -> getGameState().increment("gameTime", -1), Duration.seconds(1));
        getMasterTimer().runAtInterval(() -> getGameState().increment("blunderTime", 1), Duration.seconds(1));

        blunderManager.getCurrentBlunderProperty().addListener((observable, oldValue, newValue) -> addToStatistics(oldValue));
    }

    private void addToStatistics(GameEntity gameEntity) {
        List<Integer> blunderTimes = statistics.get(gameEntity);

        blunderTimes.add(getGameState().getInt("blunderTime"));
        getGameState().setValue("blunderTime", 0);
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
        trackForStatistics(player);
    }

    private void spawnRival() {
        rival = (GameEntity) getGameWorld().spawn("policeman", getWidth() / 2 + getWidth() / 4, getHeight() / 2);
        rivalControl = rival.getControlUnsafe(CharacterControl.class);
        rival.addComponent(new BotComponent(player));
        trackForStatistics(rival);
    }

    private void trackForStatistics(GameEntity gameEntity) {
        statistics.put(gameEntity, new LinkedList<>());
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("isPaused", false);
        vars.put("gameTime", 60);
        vars.put("blunderTime", 0);
    }

    @Override
    protected void initUI() {
        Text uiTime = getUIFactory().newText("Время: ", Color.BLACK, 18);
        uiTime.setTranslateX(getWidth() - 125);
        uiTime.setTranslateY(20);
        uiTime.textProperty().bind(getGameState().intProperty("gameTime").asString("Время: %d"));


        Label uiBlunder = new Label();
        uiBlunder.setFont(new Font(18));
        uiBlunder.setText("Ляпа — " + blunderManager.getCurrentBlunder().getComponentUnsafe(IDComponent.class).getName());

        blunderManager.getCurrentBlunderProperty().addListener((observable, oldValue, newValue) ->
                uiBlunder.setText("Ляпа — " + newValue.getComponentUnsafe(IDComponent.class).getName()));

        Text uiPause = getUIFactory().newText("PAUSE", Color.FUCHSIA, 48);

        getUIFactory().centerText(uiPause);

        uiPause.visibleProperty().bind(getGameState().booleanProperty("isPaused"));

        getGameScene().addUINode(uiBlunder);
        getGameScene().addUINode(uiPause);
        getGameScene().addUINode(uiTime);
    }

    @Override
    protected void onPostUpdate(double tpf) {
        if (getGameState().getInt("gameTime") <= 0) {
            List<GameEntity> nonBlunders = blunderManager.getNonBlunders();
            if (nonBlunders.size() > 0) {
                addToStatistics(nonBlunders.get(FXGLMath.random(0, nonBlunders.size() - 1)));
            }

            Map<String, Integer> gameEntityToMaxNonBlunderTime = new HashMap<>();
            statistics.forEach((gameEntity, blunderTimes) ->
                    gameEntityToMaxNonBlunderTime.put(gameEntity.getComponentUnsafe(IDComponent.class).getName(),
                        blunderTimes.stream().max(Comparator.comparingInt(value -> value)).orElse(0)));

            gameEntityToMaxNonBlunderTime.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue))
                    .ifPresent(entry ->
                            getDisplay().showMessageBox("Игра окончена! \n Победитель — " + entry.getKey(), this::exit));


        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
