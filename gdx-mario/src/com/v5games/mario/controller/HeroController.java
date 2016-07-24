package com.v5games.mario.controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.v5games.mario.model.Block;
import com.v5games.mario.model.Hero;
import com.v5games.mario.model.Hero.State;
import com.v5games.mario.model.Enemy;
import com.v5games.mario.model.World;

public class HeroController {

	enum Keys {
		LEFT, RIGHT, JUMP, FIRE
	}

	private static final long LONG_JUMP_PRESS 	= 150l;
	private static final float ACCELERATION 	= 20f;
	private static final float GRAVITY 			= -20f;
	private static final float MAX_JUMP_SPEED	= 7f;
	private static final float DAMP 			= 0.90f;
	private static final float MAX_VEL 			= 4f;
	
	private World 	world;
	private Hero 	bob;
	private long	jumpPressedTime;
	private boolean jumpingPressed;
	private boolean firePressed;
	private boolean grounded = false;

	// This is the rectangle pool used in collision detection
	// Good to avoid instantiation each frame
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	
	static Map<Keys, Boolean> keys = new HashMap<HeroController.Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.JUMP, false);
		keys.put(Keys.FIRE, false);
	};

	// Blocks that Hero can collide with any given frame
	private Array<Block> collidable = new Array<Block>();
	// Enemy that Hero can collide with any given frame
	private Array<Enemy> eCollidable = new Array<Enemy>();
	
	public HeroController(World world) {
		this.world = world;
		this.bob = world.getHero();
		//每次进入一个关卡，bob不动
		keysRelesed();
	}

	// ** Key presses and touches **************** //
	
	public void keysRelesed() {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.JUMP, false);
		keys.put(Keys.FIRE, false);
	}
	
	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}
	
	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}
	
	public void jumpPressed() {
		keys.get(keys.put(Keys.JUMP, true));
	}
	
	public void firePressed() {
		keys.get(keys.put(Keys.FIRE, true));
	}
	
	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}
	
	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}
	
	public void jumpReleased() {
		keys.get(keys.put(Keys.JUMP, false));
		jumpingPressed = false;
	}
	
	public void fireReleased() {
		keys.get(keys.put(Keys.FIRE, false));
		firePressed = false;
	}
	
	/** The main update method **/
	public void update(float delta) {
		// Processing the input - setting the states of Hero
		processInput();
		
		// If Hero is grounded then reset the state to IDLE 
		if (grounded && bob.getState().equals(State.JUMPING)) {
			bob.setState(State.IDLE);
		}
		
		// Setting initial vertical acceleration 
		bob.getAcceleration().y = GRAVITY;
		
		// Convert acceleration to frame time
		bob.getAcceleration().scl(delta);
		
		// apply acceleration to change velocity
		bob.getVelocity().add(bob.getAcceleration().x, bob.getAcceleration().y);

		// checking collisions with the surrounding blocks depending on Hero's velocity
		checkCollisionWithBlocks(delta);

		// apply damping to halt Hero nicely 
		bob.getVelocity().x *= DAMP;
		
		// ensure terminal velocity is not exceeded
		if (bob.getVelocity().x > MAX_VEL) {
			bob.getVelocity().x = MAX_VEL;
		}
		if (bob.getVelocity().x < -MAX_VEL) {
			bob.getVelocity().x = -MAX_VEL;
		}
		
		// simply updates the state time
		bob.update(delta);

		//LEVEL
		world.getLevel().mountains.updateScrollPosition(bob.getPosition());
		world.getLevel().clouds.update(delta);
	}

	/** Collision checking **/
	private void checkCollisionWithBlocks(float delta) {
		// scale velocity to frame units 
		bob.getVelocity().scl(delta);
		
		// Obtain the rectangle from the pool instead of instantiating it
		Rectangle bobRect = rectPool.obtain();
		// set the rectangle to bob's bounding box
		bobRect.set(bob.getBounds().x, bob.getBounds().y, bob.getBounds().width, bob.getBounds().height);
		
		// we first check the movement on the horizontal X axis
		int startX, endX;
		int startY = (int) bob.getBounds().y;
		int endY = (int) (bob.getBounds().y + bob.getBounds().height);
		// if Hero is heading left then we check if he collides with the block on his left
		// we check the block on his right otherwise
		if (bob.getVelocity().x < 0) {
			startX = endX = (int) Math.floor(bob.getBounds().x + bob.getVelocity().x);
		} else {
			startX = endX = (int) Math.floor(bob.getBounds().x + bob.getBounds().width + bob.getVelocity().x);
		}

		// get the block(s) bob can collide with
		populateCollidableBlocks(startX, startY, endX, endY);
		
//		System.out.println("startx:"+startX+" startY:"+startY+" endX:"+endX+" endY:"+endY);
		// get the enemy(s) bob can collide with
		populateCollidableEnemies(startX-1, startY-1, endX+1, endY+1);

		// simulate bob's movement on the X
		bobRect.x += bob.getVelocity().x;
		
		// clear collision boxes in world
		world.getCollisionRects().clear();
		
		// if bob collides, make his horizontal velocity 0
		for (Block block : collidable) {
			if (block == null) continue;
			if (bobRect.overlaps(block.getBounds())) {
				bob.getVelocity().x = 0;
				world.getCollisionRects().add(block.getBounds());
				break;
			}
		}

		// reset the x position of the collision box
		bobRect.x = bob.getPosition().x;
		
		// the same thing but on the vertical Y axis
		startX = (int) bob.getBounds().x;
		endX = (int) (bob.getBounds().x + bob.getBounds().width);
		if (bob.getVelocity().y < 0) {
			startY = endY = (int) Math.floor(bob.getBounds().y + bob.getVelocity().y);
		} else {
			startY = endY = (int) Math.floor(bob.getBounds().y + bob.getBounds().height + bob.getVelocity().y);
		}
		
		populateCollidableBlocks(startX, startY, endX, endY);
		
		bobRect.y += bob.getVelocity().y;
		
		for (Block block : collidable) {
			if (block == null) continue;
			if (bobRect.overlaps(block.getBounds())) {
				if (bob.getVelocity().y < 0) {
					grounded = true;
				}
				bob.getVelocity().y = 0;
				world.getCollisionRects().add(block.getBounds());
				break;
			}
		}
		// reset the collision box's position on Y
		bobRect.y = bob.getPosition().y;
		
		// update Hero's position
		bob.getPosition().add(bob.getVelocity());
		bob.getBounds().x = bob.getPosition().x;
		bob.getBounds().y = bob.getPosition().y;
		
		//if bob collidable with enemy
		for (Enemy enemy : eCollidable) {
			if (enemy == null) continue;
//			System.out.println("Hero("+bob.getBounds().x+","+bob.getBounds().y+","+bob.getBounds().width+","+bob.getBounds().height+");");
			if(bob.getState().equals(State.FIRE)){
//				System.out.println("Enemy("+enemy.getBounds().x+","+enemy.getBounds().y+","+enemy.getBounds().width+","+enemy.getBounds().height+");");
				Rectangle blade = rectPool.obtain();
				if(bob.isFacingLeft()){
					blade.set(bob.getBounds().x-bob.getBounds().width, bob.getBounds().y, bob.getBounds().width, bob.getBounds().height);
//					System.out.println("Bland("+blade.x+","+blade.y+","+blade.width+","+blade.height+");");
					if(blade.overlaps(enemy.getBounds())){
						System.out.println("enemy is dead");
						world.getLevel().removeEnemy((int)enemy.getPosition().x,(int)enemy.getPosition().y);
						break;
					}
				}else{
					blade.set(bob.getBounds().x+bob.getBounds().width, bob.getBounds().y, bob.getBounds().width, bob.getBounds().height);
//					System.out.println("Bland("+blade.x+","+blade.y+","+blade.width+","+blade.height+");");
					if(blade.overlaps(enemy.getBounds())){
						System.out.println("enemy is dead");
						world.getLevel().removeEnemy((int)enemy.getPosition().x,(int)enemy.getPosition().y);
						break;
					}
				}
			}
			if (bobRect.overlaps(enemy.getBounds())) {
				System.out.println("Hero's dead");
//				bob.getVelocity().x = 0;
//				bob.setState(State.DYING);
				break;
			}
		}
		
		// un-scale velocity (not in frame time)
		bob.getVelocity().scl(1 / delta);
		
	}

	/** populate the collidable array with the blocks found in the enclosing coordinates **/
	private void populateCollidableBlocks(int startX, int startY, int endX, int endY) {
		collidable.clear();
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				if (x >= 0 && x < world.getLevel().getWidth() && y >=0 && y < world.getLevel().getHeight()) {
					collidable.add(world.getLevel().get(x, y));
				}
			}
		}
	}
	
	/** populate the collidable array with the blocks found in the enclosing coordinates **/
	private void populateCollidableEnemies(int startX, int startY, int endX, int endY) {
		eCollidable.clear();
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				if (x >= 0 && x < world.getLevel().getWidth() && y >=0 && y < world.getLevel().getHeight()) {
					eCollidable.add(world.getLevel().getEnemiesByCoodinate(x, y));
				}
			}
		}
//		eCollidable.add(world.getLevel().getEnemiesByCoodinate(10, 2));
	}

	/** Change Hero's state and parameters based on input controls **/
	private boolean processInput() {
		//按跳时:bob状态为:没有跳,那么状态变为跳;如果已经在跳,那么二段跳
		if (keys.get(Keys.JUMP)) {
			if (!bob.getState().equals(State.JUMPING)) {
				jumpingPressed = true;
				jumpPressedTime = System.currentTimeMillis();
				bob.setState(State.JUMPING);
				bob.getVelocity().y = MAX_JUMP_SPEED; 
				grounded = false;
			} else {
				if (jumpingPressed && ((System.currentTimeMillis() - jumpPressedTime) >= LONG_JUMP_PRESS)) {
					jumpingPressed = false;
				} else {
					if (jumpingPressed) {
						bob.getVelocity().y = MAX_JUMP_SPEED;
					}
				}
			}
		}
		if (keys.get(Keys.LEFT)) {
			// left is pressed
			bob.setFacingLeft(true);
			if (!bob.getState().equals(State.JUMPING)) {
				bob.setState(State.WALKING);
			}
			bob.getAcceleration().x = -ACCELERATION;
		} else if (keys.get(Keys.RIGHT)) {
			// left is pressed
			bob.setFacingLeft(false);
			if (!bob.getState().equals(State.JUMPING)) {
				bob.setState(State.WALKING);
			}
			bob.getAcceleration().x = ACCELERATION;
		} else {
			if (!bob.getState().equals(State.JUMPING)) {
				bob.setState(State.IDLE);
			}
			bob.getAcceleration().x = 0;
		}
		if(keys.get(Keys.FIRE)){
			if(!firePressed){
				bob.setState(State.FIRE);
				firePressed = true;
			}
		}
		if(bob.getPosition().x<0||bob.getPosition().y<0){
			bob.setState(State.DYING);
		}
		return false;
	}

}
