package com.jumpdontdie;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyJDDgame /*extends ApplicationAdapter*/ extends Game {
/*
    private Texture miniJoe, pinchos;

    private TextureRegion regionPinchos;

    private SpriteBatch batch;

    private int width, height;
    private int widthJoe, heightJoe;

    @Override
    public void create() {
*//*        miniJoe = new Texture("minijoe.png");
        pinchos = new Texture("pinchos.png");
        batch = new SpriteBatch();

        //Recortar imagenes
        regionPinchos = new TextureRegion(pinchos, 0, 64, 128, 64);

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        widthJoe = miniJoe.getWidth();
        heightJoe = miniJoe.getHeight();*//*

       // Procesador p = new Procesador();
       // Gdx.input.setInputProcessor(p);

    }

    // Liberar los recursos de la gpu al dejar de utilizarlos
    @Override
    public void dispose() {
//        miniJoe.dispose();
//        pinchos.dispose();
//        batch.dispose();
    }

    @Override
    public void render() {
        //Colo para limpiar la pantalla
      //  Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
        //Limpiar la pantalla antes de comenzar a dibujar
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Batch para dibujar todas las texturas de golpe en lugar de 1 por 1
//        batch.begin();
//        batch.draw(miniJoe, 50, 0);
//        batch.draw(miniJoe, width - widthJoe, 0);
//        batch.draw(miniJoe, 0, height - heightJoe);
//        batch.draw(miniJoe, width - widthJoe, height - heightJoe);
////        batch.draw(miniJoe, 100, 100, 300, 250);
//        batch.draw(regionPinchos, 250, 0);
//
//        batch.end();

    }*/

    private AssetManager manager;

    public GameScreen gameScreen;
    public GameOverScreen gameOverScreen;
    public MenuScreen menuScreen;
    public LoadingScreen loadingScreen;

    public AssetManager getManager() {
        return manager;
    }


    @Override
    public void create() {
        manager = new AssetManager();
        manager.load("floor.png", Texture.class);
        manager.load("overfloor.png", Texture.class);
        manager.load("spike.png", Texture.class);
        manager.load("player.png", Texture.class);
        manager.load("gameover.png", Texture.class);
        manager.load("logo.png", Texture.class);
        manager.load("die.ogg", Sound.class);
        manager.load("jump.ogg", Sound.class);
        manager.load("song.ogg", Music.class);


        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);

    }

    public void finishLoading(){
        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);
        gameOverScreen = new GameOverScreen(this);
        setScreen(menuScreen);
    }
}
