package com.anirudh.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class FlappyBird extends ApplicationAdapter {
    private SpriteBatch batch;

    private Texture background;
    private Texture[] birds;

    private Animation<TextureRegion> birdAnimation;
    private float stateTime;

    private float birdY;
    private float velocity;
    private float gravity;

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

        gameState = 0;
    }

    @Override
    public void render () {
        if(Gdx.input.justTouched()) {
            gameState = 1;
        }

        stateTime = (stateTime + Gdx.graphics.getDeltaTime()) % 0.2f;
        TextureRegion currentFrame = birdAnimation.getKeyFrame(stateTime, true);

        if(gameState != 0) {
            if(Gdx.input.justTouched()) {
                velocity = -30;
            }

            if(birdY > 0 || velocity < 0) {
                velocity += gravity;
                birdY -= velocity;
            }
        }

        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
