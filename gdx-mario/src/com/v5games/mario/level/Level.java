package com.v5games.mario.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.v5games.mario.model.Block;
import com.v5games.mario.model.Enemy;
import com.v5games.mario.utils.Constants;

public class Level {

	public static final String TAG = Level.class.getName();
	
	private int width;
	private int height;
	private Block[][] blocks;
	private Enemy[][] enemies;
	private TextureRegion background;
	/** 室内还是室外 */
	private boolean isOutside = false;
	private String  name;
	  // decoration
    public Clouds clouds;
    public Mountains mountains;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Block[][] getBlocks() {
		return blocks;
	}

	public void setBlocks(Block[][] blocks) {
		this.blocks = blocks;
	}

	public Enemy[][] getEnemies() {
		return enemies;
	}

	public void setEnemies(Enemy[][] enemies) {
		this.enemies = enemies;
	}
	
	public Level() {
		loadDemoLevel();
	}
	
	public Block get(int x, int y) {
		return blocks[x][y];
	}
	
	public Enemy getEnemiesByCoodinate(int x, int y) {
		return enemies[x][y];
	}
	public void removeEnemy(int x, int y) {
		enemies[x][y] = null;
	}

	private void loadDemoLevel() {
		TiledMap map = new TmxMapLoader().load("level/level1.tmx");
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("blocks");
		TiledMapTileLayer enemiesLayer = (TiledMapTileLayer)map.getLayers().get("enemies");
		width = layer.getWidth();
		height = layer.getHeight();
		//
		MapProperties props = map.getProperties();
		setName(props.get("name").toString());
		isOutside = "1".equals(props.get("isOutside"));
		Texture level1 = new Texture(Gdx.files.internal("level/level1-bg.jpg"));
		background = new TextureRegion(level1,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		blocks = new Block[width][height];
		enemies = new Enemy[width][height];
		//初始化width*height的二维数组
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				blocks[col][row] = null;
				enemies[col][row] = null;
			}
		}
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				Cell cell = layer.getCell(i, j);
				if(cell != null) {
					blocks[i][j] = new Block(new Vector2(i, j));
				}
				Cell c = enemiesLayer.getCell(i, j);
				if(c!=null){
					enemies[i][j] = new Enemy(new Vector2(i, j));
				}
			}
		}
		// decoration
        clouds = new Clouds(layer.getWidth());
        clouds.position.set(0, 2);
        mountains = new Mountains(layer.getWidth());
        mountains.position.set(-1, -1);
        
		//print map
		/*for (int col = height-1; col >= 0; col--) {
			for (int row = 0; row < width; row++) {
				if(blocks[row][col]==null)
					System.out.print("0,");
				else
					System.out.print("1,");
			}
			System.out.println();
		}*/
	}
	
	public void renderBackground(SpriteBatch batch){
		if(isOutside){
			//装饰
			batch.draw(background,-Constants.CAMERA_WIDTH / 2, -Constants.CAMERA_HEIGHT / 2, Constants.CAMERA_WIDTH , Constants.CAMERA_HEIGHT);
			this.mountains.render(batch);
			this.clouds.render(batch);
		}
	}

}
