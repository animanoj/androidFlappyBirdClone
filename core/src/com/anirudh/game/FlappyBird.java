package com.anirudh.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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

		birdAnimation = new Animation<TextureRegion>(0.08f, birdFrames);
		stateTime = 0f;
	}

	@Override
	public void render () {
		if(Gdx.input.justTouched()) {
			Gdx.app.log("Touched", "Yes");
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stateTime = (stateTime + Gdx.graphics.getDeltaTime()) % 0.2f;
		TextureRegion currentFrame = birdAnimation.getKeyFrame(stateTime, true);

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(currentFrame, (Gdx.graphics.getWidth() / 2) - (currentFrame.getRegionWidth() / 2), (Gdx.graphics.getHeight() / 2) - (currentFrame.getRegionHeight() / 2));

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
