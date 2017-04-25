package ru.laptew.runAndCatch;

import com.almasb.fxgl.annotation.SetEntityFactory;
import com.almasb.fxgl.annotation.Spawns;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import org.jbox2d.dynamics.BodyType;

@SetEntityFactory
public class RunAndCatchFactory implements EntityFactory {

    @Spawns("policeman")
    public GameEntity getPoliceman(SpawnData spawnData) {
        return getCharacter(spawnData, "policemanRun.png");
    }

    @Spawns("dawn")
    public GameEntity getDawn(SpawnData spawnData) {
        return getCharacter(spawnData, "dawnRun.png");
    }

    private GameEntity getCharacter(SpawnData data, String textureName) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        GameEntity character = Entities.builder()
                .from(data)
                .type(RunAndCatchType.CHARACTER)
                .with(new CollidableComponent(true))
                .with(physics)
                .with(new CharacterControl(textureName))
                .build();

        character.getBoundingBoxComponent().addHitBox(new HitBox("BODY",
                new Point2D(10, 10), BoundingShape.box(17, 20)));

        return character;

    }

}
