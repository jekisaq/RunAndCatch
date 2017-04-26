package ru.laptew.runAndCatch;

import com.almasb.fxgl.annotation.SetEntityFactory;
import com.almasb.fxgl.annotation.Spawns;
import com.almasb.fxgl.ecs.Entity;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.entity.component.IDComponent;
import com.almasb.fxgl.parser.tiled.TiledMap;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import ru.laptew.runAndCatch.control.AIProximityControl;
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
                .build();

        rock.getBoundingBoxComponent().addHitBox(new HitBox("BODY",
                new Point2D(5, 5), BoundingShape.box(28, 28)));

        return rock;
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
