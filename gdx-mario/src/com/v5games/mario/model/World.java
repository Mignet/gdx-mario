package com.v5games.mario.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.v5games.mario.level.Level;

public class World {

	/** Our player controlled hero **/
	Hero bob;
	/** A world has a level through which Hero needs to go through **/
	Level level;
	
	/** The collision boxes **/
	Array<Rectangle> collisionRects = new Array<Rectangle>();

	// Getters -----------
	
	public Array<Rectangle> getCollisionRects() {
		return collisionRects;
	}
	public Hero getHero() {
		return bob;
	}
	public Level getLevel() {
		return level;
	}
	/**
	 * Return only the blocks that need to be drawn
	 * @param width - camera's width
	 * @param height - camera's height
	 * @return
	 */
	public List<Block> getDrawableBlocks(int width, int height) {
		int x1 = (int)bob.getPosition().x - width;
		int y1 = (int)bob.getPosition().y - height;
		if (x1 < 0) {
			x1 = 0;
		}
		if (y1 < 0) {
			y1 = 0;
		}
		int x2 = x1 +  2*width;
		int y2 = y1 +  2*height;
		if (x2 >= level.getWidth()) {
			x2 = level.getWidth() - 1;
		}
		if (y2 >= level.getHeight()) {
			y2 = level.getHeight() - 1;
		}
		List<Block> blocks = new ArrayList<Block>();
		Block block;
		for (int col = x1; col <= x2; col++) {
			for (int row = y1; row <= y2; row++) {
				block = level.getBlocks()[col][row];
				if (block != null) {
					blocks.add(block);
				}
			}
		}
		return blocks;
	}
	
	/**
	 * Return only the Enemies that need to be drawn
	 * @param width - camera's width
	 * @param height - camera's height
	 * @return
	 */
	public List<Enemy> getDrawableEnemies(int width, int height) {
		int x1 = (int)bob.getPosition().x - width;
		int y1 = (int)bob.getPosition().y - height;
		if (x1 < 0) {
			x1 = 0;
		}
		if (y1 < 0) {
			y1 = 0;
		}
		int x2 = x1 +  2*width;
		int y2 = y1 +  2*height;
		if (x2 >= level.getWidth()) {
			x2 = level.getWidth() - 1;
		}
		if (y2 >= level.getHeight()) {
			y2 = level.getHeight() - 1;
		}
		List<Enemy> enemys = new ArrayList<Enemy>();
		Enemy enemy;
		for (int col = x1; col <= x2; col++) {
			for (int row = y1; row <= y2; row++) {
				enemy = level.getEnemies()[col][row];
				if (enemy != null) {
					enemys.add(enemy);
				}
			}
		}
		return enemys;
	}

	// --------------------
	public World() {
		createDemoWorld();
	}

	private void createDemoWorld() {
		bob = new Hero(new Vector2(5, 1));
		level = new Level();
	}
}
