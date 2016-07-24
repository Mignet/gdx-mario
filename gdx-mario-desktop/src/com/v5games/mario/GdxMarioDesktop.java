package com.v5games.mario;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.v5games.mario.utils.Constants;

public class GdxMarioDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Thorn in the Dark";
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 600;
		Constants.CAMERA_HEIGHT = ((float)cfg.height /cfg.width)*10;
//		System.out.println(Constants.CAMERA_HEIGHT);
		new LwjglApplication(new Mario(), cfg);
	}
}
