package com.renaros.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import States.GameStateManager;
import States.MenuState;

public class ChampionsField extends ApplicationAdapter {
	private SpriteBatch batch;
	private GameStateManager gsm;
	private Music crownNoise;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(0, 0, 0, 1);

		//crownNoise = Gdx.audio.newMusic(Gdx.files.internal("CrownNoise.mp3"));
		//crownNoise.setVolume(0.3f);
		//crownNoise.setLooping(true);
		//crownNoise.play();
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
}
