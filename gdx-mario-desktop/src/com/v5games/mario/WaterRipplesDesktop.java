package com.v5games.mario;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class WaterRipplesDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "gdx-mario";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 600;
		
		new LwjglApplication(new WaterRipples(), cfg);
	}
}
