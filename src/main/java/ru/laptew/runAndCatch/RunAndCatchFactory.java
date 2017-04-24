package ru.laptew.runAndCatch;

import com.almasb.fxgl.annotation.SetEntityFactory;
import com.almasb.fxgl.annotation.SpawnSymbol;
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
import org.jbox2d.dynamics.FixtureDef;

@SetEntityFactory
public class RunAndCatchFactory implements EntityFactory {

    @Spawns("Character")
    public GameEntity getCharacter(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();

        FixtureDef fd = new FixtureDef();
        fd.setDensity(0.3f);

        physics.setFixtureDef(fd);
        physics.setBodyType(BodyType.DYNAMIC);

        GameEntity character = Entities.builder()
                .from(data)
                .type(RunAndCatchType.CHARACTER)
                .with(new CollidableComponent(true))
                //.with(physics)
                .with(new CharacterControl())
                .build();

        character.getBoundingBoxComponent().addHitBox(new HitBox("BODY",
                new Point2D(5, 5), BoundingShape.box(27, 31)));

        return character;

    }

}
