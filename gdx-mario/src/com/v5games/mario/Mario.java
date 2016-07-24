package com.v5games.mario;

import com.badlogic.gdx.Game;
import com.v5games.mario.screens.GameOverScreen;
import com.v5games.mario.screens.GameScreen;
import com.v5games.mario.screens.MainMenuScreen;

public class Mario extends Game  {
	 public MainMenuScreen mainMenuScreen;
	 public GameScreen gameScreen;
	 public GameOverScreen gameoverScreen;
	
	@Override
	public void create() {
		mainMenuScreen = new MainMenuScreen(this);
		gameScreen = new GameScreen(this);
		gameoverScreen = new GameOverScreen(this);
        setScreen(mainMenuScreen);              
	}

}
