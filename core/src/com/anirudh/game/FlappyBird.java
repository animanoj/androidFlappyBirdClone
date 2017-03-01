package com.anirudh.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

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
    private Circle birdCircle;

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
    private Rectangle[] topRectangle;
    private Rectangle[] bottomRectangle;

    private int gameState;
    private int score;
    private int nearestTube;
    private BitmapFont font;
    private GlyphLayout layout;

    private Texture gameOver;

    private void startGame() {
        birdY = (Gdx.graphics.getHeight() / 2) - (birds[0].getHeight() / 2);

        for(int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = ((randomGenerator.nextFloat() * 2f) - 1f) * maxTubeOffset;
            tubeX[i] = (i * distanceBetweenTubes) + Gdx.graphics.getWidth();
            topRectangle[i] = new Rectangle();
            bottomRectangle[i] = new Rectangle();
        }
    }

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
        velocity = 0f;
        gravity = 2f;
        birdCircle = new Circle();

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
        topRectangle = new Rectangle[numberOfTubes];
        bottomRectangle = new Rectangle[numberOfTubes];

        gameState = 0;
        score = 0;
        nearestTube = 0;
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
        layout = new GlyphLayout();

        gameOver = new Texture("gameover.png");

        startGame();
    }

    @Override
    public void render () {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if(gameState == 0) {
            if (Gdx.input.justTouched())
                gameState = 1;
        }
        else if(gameState == 1) {
            if(Gdx.input.justTouched()) {
                velocity = -30;
            }

            if(tubeX[nearestTube] + topTube.getWidth() < (Gdx.graphics.getWidth() / 2)) {
                score++;
                nearestTube = (nearestTube + 1) % numberOfTubes;
            }

            for(int i = 0; i < numberOfTubes; i++) {
                if(tubeX[i] < -(topTube.getWidth())) {
                    tubeOffset[i] = ((randomGenerator.nextFloat() * 2f) - 1f) * maxTubeOffset;
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                }
                else
                    tubeX[i] -= tubeVelocity;

                batch.draw(topTube, tubeX[i], (Gdx.graphics.getHeight() / 2) + (gap / 2) + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - (gap / 2) - bottomTube.getHeight() + tubeOffset[i]);

                topRectangle[i].set(tubeX[i], (Gdx.graphics.getHeight() / 2) + (gap / 2) + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomRectangle[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 - (gap / 2) - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
            }

            if(birdY > 0) {
                velocity += gravity;
                birdY -= velocity;
            }
            else
                gameState = 2;

            font.draw(batch, String.valueOf(score), 100, 200);
        }
        else if(gameState == 2) {
            batch.draw(gameOver, (Gdx.graphics.getWidth() / 2) - (gameOver.getWidth()), (Gdx.graphics.getHeight() / 2) + (gameOver.getHeight()), gameOver.getWidth() * 2, gameOver.getHeight() * 2);
            layout.setText(font, String.valueOf(score));
            font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2 - layout.width / 2, Gdx.graphics.getHeight() / 2 + layout.height / 2);
            if (Gdx.input.justTouched()) {
                gameState = 1;
                startGame();
                score = 0;
                nearestTube = 0;
                velocity = 0;
            }
        }

        stateTime = (stateTime + Gdx.graphics.getDeltaTime()) % 0.2f;
        TextureRegion currentFrame = birdAnimation.getKeyFrame(stateTime, true);

        batch.draw(currentFrame, (Gdx.graphics.getWidth() / 2) - (currentFrame.getRegionWidth() / 2), birdY);

        batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + (currentFrame.getRegionHeight() / 2), (currentFrame.getRegionWidth() / 2));

        for(int i = 0; i < numberOfTubes; i++) {
            if(Intersector.overlaps(birdCircle, topRectangle[i]) || Intersector.overlaps(birdCircle, bottomRectangle[i])) {
                gameState = 2;
            }
        }
    }

    @Override
    public void dispose () {
        batch.dispose();
        background.dispose();
        birds[0].dispose();
        birds[1].dispose();
        topTube.dispose();
        bottomTube.dispose();
        font.dispose();
    }
}
