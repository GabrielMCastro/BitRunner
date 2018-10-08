/**
 * Contains all the definitions of a plank that must be rolled under
 */
package com.haha.bitrunner.Actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class DuckPlank extends Obstacle {
    public static final float HEIGHT = .75f;
    public static final float WIDTH = .5f;
    public Rectangle   bounds = new Rectangle();
    public Vector2 position = new Vector2();
    public boolean onscreen = false;
	public Vector2 UL = new Vector2(); // Upper left position
	public Vector2 LR = new Vector2(); // Lower right position
	
    public DuckPlank(Vector2 position) {
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