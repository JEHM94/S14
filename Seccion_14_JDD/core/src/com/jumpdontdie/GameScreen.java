package com.jumpdontdie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.jumpdontdie.entities.FloorEntity;
import com.jumpdontdie.entities.PlayerEntity;
import com.jumpdontdie.entities.SpikeEntity;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends BaseScreen {

    private Stage stage;
    private World world;

    private PlayerEntity player;

    private List<FloorEntity> floorList = new ArrayList<FloorEntity>();
    private List<SpikeEntity> spikeList = new ArrayList<SpikeEntity>();

    private Sound jumpSound, dieSound;

    private Music bgMusic;

    public GameScreen(final MyJDDgame game) {
        super(game);
        jumpSound = game.getManager().get("jump.ogg");
        dieSound = game.getManager().get("die.ogg");
        bgMusic = game.getManager().get("song.ogg");
        stage = new Stage(new FillViewport(640, 480));
        world = new World(new Vector2(0, -10), true);

        world.setContactListener(new ContactListener() {

            private boolean areCollided(Contact contact, Object userA, Object userB) {
                return (contact.getFixtureA().getUserData().equals(userA) && contact.getFixtureB().getUserData().equals(userB)) ||
                        (contact.getFixtureA().getUserData().equals(userB) && contact.getFixtureB().getUserData().equals(userA));
            }

            @Override
            public void beginContact(Contact contact) {
                if (areCollided(contact, "player", "floor")) {
                    player.setJumping(false);
                    if (Gdx.input.isTouched()) {
                        jumpSound.play();
                        player.setMustJump(true);
                    }
                }
                if (areCollided(contact, "player", "spike")) {
                    if (player.isAlive()) {
                        player.setAlive(false);
                        bgMusic.stop();
                        dieSound.play();

                        stage.addAction(
                                Actions.sequence(
                                        Actions.delay(1.5f),
                                        Actions.run(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            game.setScreen(game.gameOverScreen);
                                                        }
                                                    }
                                        )
                                )
                        );
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    @Override
    public void show() {
        Texture playerTexture = game.getManager().get("player.png");
        Texture floorTexture = game.getManager().get("floor.png");
        Texture overfloorTexture = game.getManager().get("overfloor.png");
        Texture spikeTexture = game.getManager().get("spike.png");
        player = new PlayerEntity(world, playerTexture, new Vector2(1.5f, 1.5f));

        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 0, 1000, 1));
        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 12, 10, 2));
        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 30, 10, 2));
        spikeList.add(new SpikeEntity(world, spikeTexture, 6, 1));
        spikeList.add(new SpikeEntity(world, spikeTexture, 18, 2));
        spikeList.add(new SpikeEntity(world, spikeTexture, 35, 2));
        spikeList.add(new SpikeEntity(world, spikeTexture, 50, 1));

        stage.addActor(player);
        for (FloorEntity floor : floorList) {
            stage.addActor(floor);
        }
        for (SpikeEntity spike : spikeList) {
            stage.addActor(spike);
        }

        bgMusic.setVolume(0.75f);
        bgMusic.play();
    }

    @Override
    public void hide() {
        bgMusic.stop();
        player.detach();
        player.remove();
        for (FloorEntity floor : floorList) {
            floor.detach();
            floor.remove();
        }
        for (SpikeEntity spike : spikeList) {
            spike.detach();
            spike.remove();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (player.getX() > 150 && player.isAlive()) {
            stage.getCamera().translate(Constants.PLAYER_SPEED * delta * Constants.PIXELS_IN_METER, 0, 0);
        }

        if (Gdx.input.justTouched()) {
            jumpSound.play();
            player.jump();
        }

        //Actualizar actores
        stage.act();
        world.step(delta, 6, 2);
        //Dibujar actores
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
    }
}
