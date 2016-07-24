package com.v5games.mario.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.v5games.mario.Mario;
import com.v5games.mario.controller.HeroController;
import com.v5games.mario.model.Hero.State;
import com.v5games.mario.model.World;
import com.v5games.mario.utils.Constants;
import com.v5games.mario.utils.OverlapTester;
import com.v5games.mario.view.WorldRenderer;

public class GameScreen implements Screen, InputProcessor {
	private static String TAG = GameScreen.class.getName();
	
	private World 			world;
	private WorldRenderer 	renderer;
	private HeroController	controller;
	
	private int width, height;
	
	Mario game; 
	
	private float ppuX;
	private float ppuY;

	
	public GameScreen(Mario game){
		 this.game = game;
	}
	
	@Override
	public void show() {
		world = new World();
		renderer = new WorldRenderer(world, false);
		controller = new HeroController(world);
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void render(float delta) {
		 // Sets the clear screen color to: Cornflower Blue
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x64 / 255.0f, 0x64 / 255.0f,
                0xff / 255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		controller.update(delta);
		//如果死亡，切换屏幕
		if(world.getHero().getState().equals(State.DYING)){
			game.setScreen(game.gameoverScreen);
		}
		renderer.render();
	}
	
	@Override
	public void resize(int width, int height) {
		renderer.setSize(width, height);
		this.width = width;
		this.height = height;
		ppuX = (float)width / Constants.CAMERA_WIDTH;
		ppuY = (float)height / Constants.CAMERA_HEIGHT;
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
	}

	// * InputProcessor methods ***************************//

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.LEFT || keycode == Keys.A )
			controller.leftPressed();
		if (keycode == Keys.RIGHT || keycode == Keys.D)
			controller.rightPressed();
		if (keycode == Keys.UP || keycode == Keys.W)
			controller.jumpPressed();
		if (keycode == Keys.K)
			controller.firePressed();
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.LEFT || keycode == Keys.A)
			controller.leftReleased();
		if (keycode == Keys.RIGHT || keycode == Keys.D)
			controller.rightReleased();
		if (keycode == Keys.UP || keycode == Keys.W)
			controller.jumpReleased();
		if (keycode == Keys.K)
			controller.fireReleased();
		if (keycode == Keys.M)
			renderer.setDebug(!renderer.isDebug());
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		//屏幕点击的坐标系是左上角
		y = height - y;
		Gdx.app.debug(TAG,"you touched=> x:"+x+",y:"+y);
//		if (!Gdx.app.getType().equals(ApplicationType.Android))
//			return false;
		Rectangle playBounds = new Rectangle(0, 0, ppuX, ppuY);
		boolean leftClicked = OverlapTester.pointInRectangle(playBounds, new Vector2(x, y));;
		if (leftClicked) {
			controller.leftPressed();
		}
		playBounds = new Rectangle(ppuX, 0, ppuX, ppuY);
		boolean rightClicked = OverlapTester.pointInRectangle(playBounds, new Vector2(x, y));;
		if (rightClicked) {
			controller.rightPressed();
		}
		playBounds = new Rectangle(9*ppuX, 0, ppuX, ppuY);
		boolean jumpClicked = OverlapTester.pointInRectangle(playBounds, new Vector2(x, y));;
		if (jumpClicked) {
			controller.jumpPressed();
		}
		playBounds = new Rectangle(8*ppuX, 0, ppuX, ppuY);
		boolean fireClicked = OverlapTester.pointInRectangle(playBounds, new Vector2(x, y));;
		if (fireClicked) {
			controller.firePressed();
		}
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
//		if (!Gdx.app.getType().equals(ApplicationType.Android))
//			return false;
		y = height - y;
		Rectangle playBounds = new Rectangle(0, 0, ppuX, ppuY);
		boolean leftClicked = OverlapTester.pointInRectangle(playBounds, new Vector2(x, y));;
		if (leftClicked) {
			controller.leftReleased();
		}
		playBounds = new Rectangle(ppuX, 0, ppuX, ppuY);
		boolean rightClicked = OverlapTester.pointInRectangle(playBounds, new Vector2(x, y));;
		if (rightClicked) {
			controller.rightReleased();
		}
		playBounds = new Rectangle(9*ppuX, 0, ppuX, ppuY);
		boolean jumpClicked = OverlapTester.pointInRectangle(playBounds, new Vector2(x, y));;
		if (jumpClicked) {
			controller.jumpReleased();
		}
		playBounds = new Rectangle(8*ppuX, 0, ppuX, ppuY);
		boolean fireClicked = OverlapTester.pointInRectangle(playBounds, new Vector2(x, y));;
		if (fireClicked) {
			controller.fireReleased();
		}
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
//		if (!Gdx.app.getType().equals(ApplicationType.Android))
//			return false;
		y = height - y;
		Gdx.app.debug(TAG,"you touchDragged=> x:"+x+",y:"+y);
		controller.keysRelesed();
		Rectangle playBounds = new Rectangle(0, 0, ppuX, ppuY);
		boolean leftClicked = OverlapTester.pointInRectangle(playBounds, new Vector2(x, y));;
		if (leftClicked) {
			controller.leftPressed();
		}
		playBounds = new Rectangle(ppuX, 0, ppuX, ppuY);
		boolean rightClicked = OverlapTester.pointInRectangle(playBounds, new Vector2(x, y));;
		if (rightClicked) {
			controller.rightPressed();
		}
		playBounds = new Rectangle(9*ppuX, 0, ppuX, ppuY);
		boolean jumpClicked = OverlapTester.pointInRectangle(playBounds, new Vector2(x, y));;
		if (jumpClicked) {
			controller.jumpPressed();
		}
		playBounds = new Rectangle(8*ppuX, 0, ppuX, ppuY);
		boolean fireClicked = OverlapTester.pointInRectangle(playBounds, new Vector2(x, y));;
		if (fireClicked) {
			controller.firePressed();
		}
		return true;
//		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	

}

