package com.v5games.mario;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class UITestDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "gdx-mario";
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 480;
		
		new LwjglApplication(new UISimpleTest(), cfg);
	}
}
