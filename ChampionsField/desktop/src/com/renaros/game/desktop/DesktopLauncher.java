package com.renaros.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.renaros.game.ChampionsField;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new ChampionsField(), config);
		config.title = "Champions Field";
		config.width = Gdx.graphics.getWidth();
        config.height = Gdx.graphics.getHeight();
	}
}
