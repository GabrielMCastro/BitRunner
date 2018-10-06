/**
 * Contains all the definitions of a plank that must be jumped over
 */
package com.haha.bitrunner.Actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class JumpPlank extends Obstacle {
    public static final float SIZE = .579f;
    public Rectangle   bounds = new Rectangle();
    public Vector2 position = new Vector2();
	public Vector2 UL = new Vector2(); // Upper left position
	public Vector2 LR = new Vector2(); // Lower right position
    public boolean onscreen = false;
    
    public JumpPlank(Vector2 position) {
    	setPosition(position);
    }
    
	public void update(float delta){
		this.bounds.x = this.position.x;
		this.bounds.y = this.position.y;
		this.UL.y = this.position.y + SIZE;
		this.UL.x = this.position.x;
		this.LR.y = this.position.y;
		this.LR.x = this.position.x + SIZE;
	}
	
	public void setPosition(Vector2 position){
		this.position = position;
    	this.bounds.width = SIZE;
    	this.bounds.height = SIZE;
    	this.bounds.x = this.position.x;
		this.bounds.y = this.position.y;
		this.UL.y = this.position.y + SIZE;
		this.UL.x = this.position.x;
		this.LR.y = this.position.y;
		this.LR.x = this.position.x + SIZE;
	}
}