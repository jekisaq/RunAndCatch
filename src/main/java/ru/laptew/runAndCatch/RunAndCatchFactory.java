package ru.laptew.runAndCatch;

import com.almasb.fxgl.annotation.SetEntityFactory;
import com.almasb.fxgl.annotation.Spawns;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.entity.component.IDComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.jbox2d.dynamics.BodyType;
import org.jetbrains.annotations.NotNull;
import ru.laptew.runAndCatch.control.CharacterControl;

@SetEntityFactory
public class RunAndCatchFactory implements EntityFactory {

    private static int lastID = 0;

    @Spawns("policeman")
    public GameEntity getPoliceman(SpawnData spawnData) {
        return getCharacter(spawnData, "policeman");
    }

    @Spawns("dawn")
    public GameEntity getDawn(SpawnData spawnData) {
        return getCharacter(spawnData, "dawn");
    }

    @Spawns("rock")
    public GameEntity getRock(SpawnData spawnData) {
        PhysicsComponent physicsComponent = new PhysicsComponent();

        GameEntity rock = Entities.builder()
                .from(spawnData)
                .type(RunAndCatchType.ROCK)
                .with(physicsComponent)
                .with(new CollidableComponent(true))
                .viewFromTexture("rock.png")
                .at(getRockSpawnPoint())
                .build();

        rock.getBoundingBoxComponent().addHitBox(new HitBox("BODY",
                new Point2D(5, 5), BoundingShape.box(28, 24)));

        return rock;
    }

    @NotNull
    private Point2D getRockSpawnPoint() {
        Point2D spawnPoint;
        Rectangle2D selection;
        int entitiesInSelection;

        do {
            spawnPoint = new Point2D(FXGLMath.random(0, FXGL.getAppWidth()), FXGLMath.random(0, FXGL.getAppHeight()));
            selection = new Rectangle2D(spawnPoint.getX(), spawnPoint.getY(), 30, 30);
            entitiesInSelection = FXGL.getApp().getGameWorld().getEntitiesInRange(selection).size();
        } while (entitiesInSelection > 1);

        return spawnPoint;
    }

    private GameEntity getCharacter(SpawnData data, String name) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        GameEntity character = Entities.builder()
                .from(data)
                .type(RunAndCatchType.CHARACTER)
                .with(new CollidableComponent(true))
                .with(new IDComponent(name, getUniqueID()))
                .with(physics)
                .with(new CharacterControl(name + "Run.png"))
                .build();

        character.getBoundingBoxComponent().addHitBox(new HitBox("BODY",
                new Point2D(10, 10), BoundingShape.box(17, 20)));

        return character;

    }

    private int getUniqueID() {
        return lastID++;
    }

}
