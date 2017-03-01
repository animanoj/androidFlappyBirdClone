package com.anirudh.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

class FlappyBird extends ApplicationAdapter {
    private SpriteBatch batch;

    private Texture background;

    private Texture[] birds;
    private Animation<TextureRegion> birdAnimation;
    private float stateTime;
    private float birdY;
    private float velocity;
    private float gravity;

    private int numberOfTubes;
    private Texture topTube;
    private Texture bottomTube;
    private float gap;
    private Random randomGenerator;
    private float maxTubeOffset;
    private float[] tubeOffset;
    private float[] tubeX;
    private float tubeVelocity;
    private float distanceBetweenTubes;

    private int gameState;

    @Override
    public void create () {
        batch = new SpriteBatch();
        background = new Texture("bg.png");

        birds = new Texture[2];
        birds[0] = new Texture("bird_0.png");
        birds[1] = new Texture("bird_1.png");
        TextureRegion[] birdFrames = new TextureRegion[2];
        birdFrames[0] = new TextureRegion(birds[0]);
        birdFrames[1] = new TextureRegion(birds[1]);
        birdAnimation = new Animation<TextureRegion>(0.1f, birdFrames);
        stateTime = 0f;
        birdY = (Gdx.graphics.getHeight() / 2) - (birds[0].getHeight() / 2);
        velocity = 0f;
        gravity = 2f;

        numberOfTubes = 4;
        distanceBetweenTubes = (Gdx.graphics.getWidth() * 0.75f);
        gap = 400f;
        maxTubeOffset = (Gdx.graphics.getHeight() / 2) - (gap / 2) - 100;
        randomGenerator = new Random();
        tubeVelocity = 4;
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        tubeOffset = new float[numberOfTubes];
        tubeX = new float[numberOfTubes];
        for(int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = ((randomGenerator.nextFloat() * 2f) - 1f) * maxTubeOffset;
            tubeX[i] = (Gdx.graphics.getWidth() / 2) - (topTube.getWidth() / 2) + (i * distanceBetweenTubes);
        }

        gameState = 0;
    }

    @Override
    public void render () {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if(Gdx.input.justTouched()) {
            gameState = 1;
        }

        if(gameState != 0) {
            if(Gdx.input.justTouched()) {
                velocity = -30;
            }

            for(int i = 0; i < numberOfTubes; i++) {
                if(tubeX[i] < -(topTube.getWidth()))
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                else
                    tubeX[i] -= tubeVelocity;

                batch.draw(topTube, tubeX[i], (Gdx.graphics.getHeight() / 2) + (gap / 2) + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
            }

            if(birdY > 0 || velocity < 0) {
                velocity += gravity;
                birdY -= velocity;
            }
        }

        stateTime = (stateTime + Gdx.graphics.getDeltaTime()) % 0.2f;
        TextureRegion currentFrame = birdAnimation.getKeyFrame(stateTime, true);

        batch.draw(currentFrame, (Gdx.graphics.getWidth() / 2) - (currentFrame.getRegionWidth() / 2), birdY);
        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();
        background.dispose();
        birds[0].dispose();
        birds[1].dispose();
    }
}
