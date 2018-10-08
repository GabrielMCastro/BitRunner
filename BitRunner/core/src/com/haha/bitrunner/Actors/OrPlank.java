/**
 * A plank that can be crossed by jumping of rolling
 */
package com.haha.bitrunner.Actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class OrPlank extends Obstacle {
    public static final float WIDTH = .5f;
    public static final float HEIGHT = .3f;
    public Rectangle   bounds = new Rectangle();
    public Vector2 position = new Vector2();
	public Vector2 UL = new Vector2(); // Upper left position
	public Vector2 LR = new Vector2(); // Lower right position
    public boolean onscreen = false;
    
    public OrPlank(Vector2 position) {
    	setPosition(position);
    }
    
	public void update(float delta){
		this.bounds.x = this.position.x;
		this.bounds.y = this.position.y;
		this.UL.y = this.position.y + HEIGHT;
		this.UL.x = this.position.x;
		this.LR.y = this.position.y;
		this.LR.x = this.position.x + WIDTH;
	}
	
	public void setPosition(Vector2 position){
		this.position = position;
    	this.bounds.width = WIDTH;
    	this.bounds.height = HEIGHT;
    	this.bounds.x = this.position.x;
		this.bounds.y = this.position.y;
		this.UL.y = this.position.y + HEIGHT;
		this.UL.x = this.position.x;
		this.LR.y = this.position.y;
		this.LR.x = this.position.x + WIDTH;
	}
}