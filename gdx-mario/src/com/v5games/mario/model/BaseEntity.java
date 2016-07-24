package com.v5games.mario.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * 	 对象基础类
 */
public abstract class BaseEntity{
	/**速度，极限速度，摩擦力，加速度和边界。*/
	public Vector2 velocity;
	public Vector2 terminalVelocity;
	public Vector2 friction;
	public Vector2 acceleration;
	public Rectangle bounds;
	
	/**
	 * 位置
	 */
    public Vector2 position;
    /**
     * 大小
     */
    public Vector2 dimension;
    /**
     * 原点
     */
    public Vector2 origin;
    /**
     * 缩放
     */
    public Vector2 scale;
    /**
     * 旋转度
     */
    public float rotation;

    public BaseEntity() {
        position = new Vector2();
        dimension = new Vector2(1, 1);
        origin = new Vector2();
        scale = new Vector2(1, 1);
        rotation = 0;
        //init you konw
        velocity = new Vector2();
        terminalVelocity = new Vector2();
        friction = new Vector2();
        acceleration = new Vector2();
        bounds = new Rectangle();
    }

    public void update(float deltaTime) {
    }

    /**
     * 画自己
     */
    public abstract void render(SpriteBatch batch);
}
