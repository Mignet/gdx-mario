package com.v5games.mario;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;


public class TexturePackerJob {
	private static String DIR = "../gdx-mario-android/";
	public static void main(String[] args) {
		TexturePacker2.process(DIR+"assets/bob/",DIR+ "assets/images/", "textures.pack");
	}
}


