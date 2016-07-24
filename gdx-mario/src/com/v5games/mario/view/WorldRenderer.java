package com.v5games.mario.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.v5games.mario.model.Block;
import com.v5games.mario.model.Enemy;
import com.v5games.mario.model.Hero;
import com.v5games.mario.model.Hero.State;
import com.v5games.mario.model.World;
import com.v5games.mario.utils.Constants;

public class WorldRenderer implements Disposable {

	private World world;
	private Hero bob;
	private OrthographicCamera cam;
	//HUD
	private OrthographicCamera hudCamera;

	/** for debug rendering <b>ShapeRenderer</b>**/
	ShapeRenderer debugRenderer = new ShapeRenderer();

	/** Textures **/
	private TextureRegion knightIdleLeft;
	private TextureRegion blockTexture;
	/** Bob */
	private TextureRegion bobIdleLeft;
	private TextureRegion bobIdleRight;
	private TextureRegion bobFrame;
	private TextureRegion bobJumpLeft;
	private TextureRegion bobFallLeft;
//	private TextureRegion bobFireLeft;
//	private TextureRegion bobFireRight;
	private TextureRegion bobJumpRight;
	private TextureRegion bobFallRight;
	/**button*/
	private TextureRegion btnLeft;
	private TextureRegion btnRight;
	private TextureRegion btnJump;
	private TextureRegion btnFire;
	
	/** Animations **/
	private Animation walkLeftAnimation;
	private Animation walkRightAnimation;
	
	private Animation fireLeftAnimation;
	private Animation fireRightAnimation;
	/** 画刷 */
	private SpriteBatch spriteBatch;
	private SpriteBatch hudBatch;
	BitmapFont bf = new BitmapFont(Gdx.files.internal("data/default.fnt"), Gdx.files.internal("data/default.png"), false);
	
	private boolean debug = false;
	private int width;
	private int height;
	
	public void setSize (int w, int h) {
		this.width = w;
		this.height = h;
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public WorldRenderer(World world, boolean debug) {
		this.world = world;
		this.bob = world.getHero();
		this.cam = new OrthographicCamera(Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
		this.cam.position.set(Constants.CAMERA_WIDTH / 2f, Constants.CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
		this.hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		this.debug = debug;
		spriteBatch = new SpriteBatch();
		hudBatch = new SpriteBatch();
		loadTextures();
	}
	/**
	 * move camera<br>
	 * 保证相机不出界
	 * important:camera's Coordinate is (w/2,h/2)
	 */
	public void moveCamera(){
		float x = bob.getPosition().x;
		float y = bob.getPosition().y;
		boolean moveflag = false;
	    if ((x> cam.position.x &&  cam.position.x <world.getLevel().getWidth() - Constants.CAMERA_WIDTH / 2)) {
	        cam.position.set(x, cam.position.y, 0);
	        moveflag = true;
	    }
	    if ((x< cam.position.x &&  cam.position.x>Constants.CAMERA_WIDTH / 2)) {
	    	cam.position.set(x, cam.position.y, 0);
	    	moveflag = true;
	    }
	    if ((y> cam.position.y &&  cam.position.y <world.getLevel().getHeight() - Constants.CAMERA_HEIGHT / 2)) {
	    	cam.position.set( cam.position.x,y, 0);
	    	moveflag = true;
	    }
	    if ((y< cam.position.y &&  cam.position.y>Constants.CAMERA_HEIGHT / 2)) {
	    	cam.position.set( cam.position.x,y, 0);
	    	moveflag = true;
	    }
	    if(moveflag){
	    	 cam.update();
	    }
	}
	
	/**
	 * 加载资源
	 */
	private void loadTextures() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/textures/textures.pack"));
		knightIdleLeft = atlas.findRegion("knight-01");
		bobIdleLeft = atlas.findRegion("bob-01");
		bobIdleRight = new TextureRegion(bobIdleLeft);
		bobIdleRight.flip(true, false);
		blockTexture = atlas.findRegion("block");
		//预备动画
		TextureRegion[] walkLeftFrames = new TextureRegion[5];
		for (int i = 0; i < 5; i++) {
			walkLeftFrames[i] = atlas.findRegion("bob-0" + (i + 2));
		}
		walkLeftAnimation = new Animation(Constants.RUNNING_FRAME_DURATION, walkLeftFrames);
		TextureRegion[] walkRightFrames = new TextureRegion[5];
		for (int i = 0; i < 5; i++) {
			walkRightFrames[i] = new TextureRegion(walkLeftFrames[i]);
			walkRightFrames[i].flip(true, false);
		}
		walkRightAnimation = new Animation(Constants.RUNNING_FRAME_DURATION, walkRightFrames);
		//fire
		TextureRegion[] fireLeftFrames = new TextureRegion[3];
		for (int i = 0; i < 3; i++) {
			fireLeftFrames[i] =  atlas.findRegion("bob-fire" + (i + 1));
		}
		fireLeftAnimation = new Animation(Constants.FIRE_FRAME_DURATION, fireLeftFrames);
		TextureRegion[] fireRightFrames = new TextureRegion[3];
		for (int i = 0; i < 3; i++) {
			fireRightFrames[i] = new TextureRegion(fireLeftFrames[i]);
			fireRightFrames[i].flip(true, false);
		}
		fireRightAnimation = new Animation(Constants.FIRE_FRAME_DURATION, fireRightFrames);
		//-----------------------------------------------------
		bobJumpLeft = atlas.findRegion("bob-up");
		bobJumpRight = new TextureRegion(bobJumpLeft);
		bobJumpRight.flip(true, false);
		bobFallLeft = atlas.findRegion("bob-down");
		bobFallRight = new TextureRegion(bobFallLeft);
		bobFallRight.flip(true, false);
//		bobFireLeft = atlas.findRegion("bob-fire");
//		bobFireRight = new TextureRegion(bobFireLeft);
//		bobFireRight.flip(true, false);
		//control button
		btnLeft = atlas.findRegion("btn_left");
		btnRight = atlas.findRegion("btn_right");
		btnJump = atlas.findRegion("btn_jump");
		btnFire = atlas.findRegion("btn_fire");
	}
	
	public void render() {
		moveCamera();
		//关卡背景
		drawLevelBackground();
		//框架
		drawCollisionBlocks();
		spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.begin();
			drawBlocks();
			drawEnemy();
			drawHero();
		spriteBatch.end();
		if (debug){
			drawDebug();
		}
		
		drawHUD();
	}

	private void drawHUD() {
		hudBatch.setProjectionMatrix(hudCamera.combined);
		hudBatch.begin();
		bf.draw(hudBatch,"FPS:"+Gdx.graphics.getFramesPerSecond(),hudCamera.position.x-Gdx.graphics.getWidth()/ 2, hudCamera.position.y+Gdx.graphics.getHeight() / 2);
		bf.draw(hudBatch,"Map:"+world.getLevel().getName(),hudCamera.position.x+Gdx.graphics.getWidth()/ 2-10*10, hudCamera.position.y+Gdx.graphics.getHeight() / 2);
		//buttons
		hudBatch.draw(btnLeft,hudCamera.position.x-Gdx.graphics.getWidth()/ 2, hudCamera.position.y-Gdx.graphics.getHeight() / 2, Block.SIZE*80 , Block.SIZE*80);
		hudBatch.draw(btnRight,hudCamera.position.x-Gdx.graphics.getWidth() / 2+Block.SIZE*80, hudCamera.position.y-Gdx.graphics.getHeight()/ 2, Block.SIZE*80 , Block.SIZE*80);
		hudBatch.draw(btnJump,hudCamera.position.x+Gdx.graphics.getWidth() / 2-2*Block.SIZE*80, hudCamera.position.y-Gdx.graphics.getHeight() / 2, Block.SIZE*80 , Block.SIZE*80);
		hudBatch.draw(btnFire,hudCamera.position.x+Gdx.graphics.getWidth()/ 2-Block.SIZE*80, hudCamera.position.y-Gdx.graphics.getHeight() / 2, Block.SIZE*80 , Block.SIZE*80);
		hudBatch.end();
	}
	
	private void drawLevelBackground() {
		hudBatch.setProjectionMatrix(hudCamera.combined);
		hudBatch.begin();
		world.getLevel().renderBackground(hudBatch);
		hudBatch.end();
	}
	
	private void drawBlocks() {
		for (Block block : world.getDrawableBlocks((int)Constants.CAMERA_WIDTH, (int)Constants.CAMERA_HEIGHT)) {
			spriteBatch.draw(blockTexture, block.getPosition().x , block.getPosition().y, Block.SIZE , Block.SIZE);
		}
	}

	private void drawEnemy() {
		for (Enemy enemy : world.getDrawableEnemies((int)Constants.CAMERA_WIDTH, (int)Constants.CAMERA_HEIGHT)) {
			spriteBatch.draw(knightIdleLeft, enemy.getPosition().x , enemy.getPosition().y, enemy.SIZE , enemy.SIZE);
		}
	}
	
	private void drawHero() {
		bobFrame = bob.isFacingLeft() ? bobIdleLeft : bobIdleRight;
		if(bob.getState().equals(State.WALKING)) {
			bobFrame = bob.isFacingLeft() ? walkLeftAnimation.getKeyFrame(bob.getStateTime(), true) : walkRightAnimation.getKeyFrame(bob.getStateTime(), true);
		} else if (bob.getState().equals(State.JUMPING)) {
			if (bob.getVelocity().y > 0) {
				bobFrame = bob.isFacingLeft() ? bobJumpLeft : bobJumpRight;
			} else {
				bobFrame = bob.isFacingLeft() ? bobFallLeft : bobFallRight;
			}
		}else if(bob.getState().equals(State.FIRE)){
			bobFrame = bob.isFacingLeft() ? fireLeftAnimation.getKeyFrame(bob.getStateTime(), true) : fireRightAnimation.getKeyFrame(bob.getStateTime(), true);
//			bobFrame =  bob.isFacingLeft() ?bobFireLeft:bobFireRight;
		}
		//调整fire时的位置
		if(bob.getState().equals(State.FIRE)){
			if(bob.isFacingLeft()){
				spriteBatch.draw(bobFrame, bob.getPosition().x-Hero.SIZE , bob.getPosition().y, Hero.SIZE *2, Hero.SIZE);
			}else{
				spriteBatch.draw(bobFrame, bob.getPosition().x , bob.getPosition().y, Hero.SIZE *2, Hero.SIZE);
			}
		}else{
			spriteBatch.draw(bobFrame, bob.getPosition().x , bob.getPosition().y, Hero.SIZE , Hero.SIZE);
		}
	}
	//Debug mode
	private void drawDebug() {
		// render blocks
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Filled);
		for (Block block : world.getDrawableBlocks((int)Constants.CAMERA_WIDTH, (int)Constants.CAMERA_HEIGHT)) {
			Rectangle rect = block.getBounds();
			debugRenderer.setColor(new Color(1, 0, 0, 1));
			debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		}
		// render Hero
		Hero bob = world.getHero();
		Rectangle rect = bob.getBounds();
		debugRenderer.setColor(new Color(0, 1, 0, 1));
		debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		debugRenderer.end();
	}
	
	private void drawCollisionBlocks() {
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Filled);
		debugRenderer.setColor(new Color(1, 0.5f, 1, 1));
		for (Rectangle rect : world.getCollisionRects()) {
			debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		}
		debugRenderer.end();
		
	}
	@Override
	public void dispose() {
		 spriteBatch.dispose();
		 hudBatch.dispose();
		 bf.dispose();
	}
}
