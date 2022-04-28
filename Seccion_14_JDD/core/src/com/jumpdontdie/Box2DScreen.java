package com.jumpdontdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DScreen extends BaseScreen {

    private World world;

    private Box2DDebugRenderer renderer;
    private OrthographicCamera camera;

    private Body minijoeBody, sueloBody, pinchoBody;
    private Fixture minijoeFixture, sueloFixture, pinchoFixture;

    private boolean debeSaltar, joeSaltando, joeVivo = true;

    public Box2DScreen(MyJDDgame game) {
        super(game);
    }

    @Override
    public void show() {
        world = new World(new Vector2(0, -10), true);
        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(16, 9);
        camera.translate(0, 1);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();

                if (fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("floor") ||
                        fixtureA.getUserData().equals("floor") && fixtureB.getUserData().equals("player")) {
                    if (Gdx.input.isTouched()) {
                        debeSaltar = true;
                    }
                    joeSaltando = false;
                }
                if (fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("spike") ||
                        fixtureA.getUserData().equals("spike") && fixtureB.getUserData().equals("player")) {
                    joeVivo = false;
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();
                if (fixtureA == minijoeFixture && fixtureB == sueloFixture) {
                    joeSaltando = true;
                }
                if (fixtureA == sueloFixture && fixtureB == minijoeFixture) {
                    joeSaltando = true;
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });


        minijoeBody = world.createBody(createJoeBodyDef());
        sueloBody = world.createBody(createSueloBodyDef());
        pinchoBody = world.createBody(createPinchoBodyDef(4));


        PolygonShape minijoeShape = new PolygonShape();
        // tamaño de la caja 1 metro (0.5 se multiplica por 2)
        minijoeShape.setAsBox(0.5f, 0.5f);
        minijoeFixture = minijoeBody.createFixture(minijoeShape, 1);
        minijoeShape.dispose();

        PolygonShape sueloShape = new PolygonShape();
        sueloShape.setAsBox(500, 1);
        sueloFixture = sueloBody.createFixture(sueloShape, 1);
        sueloShape.dispose();

        pinchoFixture = createPinchoFixture(pinchoBody);

        minijoeFixture.setUserData("player");
        sueloFixture.setUserData("floor");
        pinchoFixture.setUserData("spike");

    }

    private BodyDef createPinchoBodyDef(float x) {
        BodyDef def = new BodyDef();
        def.position.set(x, 0.5f);
        return def;
    }

    private BodyDef createSueloBodyDef() {
        BodyDef def = new BodyDef();
        def.position.set(0, -1);
        return def;
    }

    private BodyDef createJoeBodyDef() {
        BodyDef def = new BodyDef();
        def.position.set(0.5f, 0);
        def.type = BodyDef.BodyType.DynamicBody;
        return def;
    }

    private Fixture createPinchoFixture(Body pinchoBody) {
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(-0.5f, -0.5f);
        vertices[1] = new Vector2(0.5f, -0.5f);
        vertices[2] = new Vector2(0, 0.5f);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        Fixture fixture = pinchoBody.createFixture(shape, 1);
        shape.dispose();
        return fixture;
    }

    @Override
    public void dispose() {
        minijoeBody.destroyFixture(minijoeFixture);
        pinchoBody.destroyFixture(pinchoFixture);
        sueloBody.destroyFixture(sueloFixture);
        world.destroyBody(minijoeBody);
        world.destroyBody(pinchoBody);
        world.destroyBody(sueloBody);
        world.dispose();
        renderer.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (debeSaltar) {
            debeSaltar = false;
            saltar();
        }

        if (Gdx.input.justTouched() && !joeSaltando) {
            debeSaltar = true;
        }

        if (joeVivo) {
            float velocidadY = minijoeBody.getLinearVelocity().y;
            minijoeBody.setLinearVelocity(2, velocidadY);
        }
        world.step(delta, 6, 2);

        camera.update();
        renderer.render(world, camera.combined);
    }

    private void saltar() {
        Vector2 position = minijoeBody.getPosition();
        minijoeBody.applyLinearImpulse(0, 6, position.x, position.y, true);
    }
}
