package com.haha.bitrunner.Actors;

import java.util.Random;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class DroppingBrick extends Obstacle {
    public static final float HEIGHT = .6f;
    public static final float WIDTH = .325f;
    public Rectangle   bounds = new Rectangle();
    public Vector2 position = new Vector2();
    public float yMoveSpeed = 0;
    public boolean onScreen = false;
    private int worldWidth = 0;
    private int worldHeight = 0;
    private float speed = 0;
    private Random rand = new Random();
    public int yDestination = 0;
    
    public DroppingBrick(Vector2 position, float worldwidth, float worldheight, float speed) {
    	worldWidth = (int) worldwidth;
    	worldHeight = (int) worldheight;
    	this.speed = speed;
       	setPosition(position);
    }
    
	public void update(float delta, float speed){ 
		this.speed = speed;
		this.bounds.x = this.position.x;
		this.bounds.y = this.position.y;
		
		if(this.bounds.y <= 0) {
			yMoveSpeed = -yMoveSpeed;
			setPosition(new Vector2(this.bounds.x, .01f));
		}
	}
	
	public void setYSpeed(){
		
		if(rand.nextBoolean()){ 
			yMoveSpeed = (float) (speed*((float)(worldHeight - 0)/(worldWidth - 1)));
			yDestination = 1;
		}else {
			yMoveSpeed = (float) (speed*((float)(worldHeight - .8)/(worldWidth - 1)));
			yDestination = 2;
		}
		
	}
	
	public void setPosition(Vector2 position){
		this.position = position;
    	this.bounds.width = WIDTH;
    	this.bounds.height = HEIGHT;
    	this.bounds.x = this.position.x;
		this.bounds.y = this.position.y;
	}
	
	
}