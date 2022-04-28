package com.jumpdontdie.entities;

import static com.jumpdontdie.Constants.PIXELS_IN_METER;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jumpdontdie.Constants;

public class FloorEntity extends Actor {

    private Texture floor, overfloor;
    private World world;
    private Body body, leftBody;
    private Fixture fixture, leftFixture;

    public FloorEntity(World world, Texture floor, Texture overfloor, float x, float width, float y) {
        this.world = world;
        this.floor = floor;
        this.overfloor = overfloor;

        //Coloco el suelo en la posicion que le corresponde
        BodyDef def = new BodyDef();
        def.position.set(x + width / 2, y - 0.5f);
        body = world.createBody(def);

        //Le doy forma de caja
        PolygonShape box = new PolygonShape();
        box.setAsBox(width / 2, 0.5f);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("floor");
        box.dispose();

        BodyDef leftDef = new BodyDef();
        leftDef.position.set(x, y - 0.55f);
        leftBody = world.createBody(leftDef);

        //Le doy forma de caja
        PolygonShape leftBox = new PolygonShape();
        leftBox.setAsBox(0.02f, 0.45f);
        leftFixture = leftBody.createFixture(leftBox, 1);
        leftFixture.setUserData("spike");
        leftBox.dispose();

        setSize(width * PIXELS_IN_METER, PIXELS_IN_METER);
        setPosition(x * PIXELS_IN_METER, (y - 1) * PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Render both textures.
        batch.draw(floor, getX(), getY(), getWidth(), getHeight());
        batch.draw(overfloor, getX(), getY() + 0.9f * getHeight(), getWidth(), 0.1f * getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

}
