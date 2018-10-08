/**
 * This will contain all the definitions for Matt, the main character
 */

package com.haha.bitrunner.Actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Matt {
	
	public enum State {
		 RUNNING, ROLLING, JUMPING, DYING, DEAD, FALLING
	}
	
	public float SPEED = 1.8f; // units per second
	public final float JUMPFORCE = 5f; // force of the jump
	public static float HEIGHT = 1f; // a unit
	public static float WIDTH = 0.5f; // half a unit
	public Vector2 velocity = new Vector2();
	public Vector2 position = new Vector2();
	public Vector2 UL = new Vector2(); // Matt's upper left position
	public Vector2 LR = new Vector2(); // Matt's lower right position
	public float   floor 	= 0; // Where Matt will land
	public float dbugvelocity = 0; // Is used to display Matt's velocity
	public boolean onBlock = false; // If Matt is on a block
	public State state = State.RUNNING;
	public Rectangle bounds = new Rectangle(); // Matt's bounds
	public Rectangle standhitbox = new Rectangle(); // The hitbox for Matt when he is running
	public Rectangle lowerhitbox = new Rectangle(); // The the hitbox for the lower half of Matt
	private float statetime = 0;
	
	public Matt(Vector2 position) {
			  this.position = position;
		      this.bounds.height = HEIGHT;
		      this.bounds.width = WIDTH;
			  this.bounds.y = this.position.y;
			  this.bounds.x = this.position.x;
			  this.standhitbox.height = HEIGHT / 1.5f;
			  this.standhitbox.width = WIDTH / 3;
			  this.standhitbox.y = ((this.position.y + HEIGHT) - this.standhitbox.height);
			  this.standhitbox.x = this.position.x + (WIDTH / 3);
			  this.lowerhitbox.height = HEIGHT / 3;
			  this.lowerhitbox.width = WIDTH;
			  this.lowerhitbox.y = this.position.y;
			  this.lowerhitbox.x = this.position.x;
			  this.UL.y = this.position.y + HEIGHT;
			  this.UL.x = this.position.x;
			  this.LR.y = this.position.y;
			  this.LR.x = this.position.x + WIDTH;
		}

	public void setState(State newstate){
		this.state = newstate;
	}
	
	public float getStateTime(){
		return statetime;
	}
	
	// This updates Matt's position, size and velocity
	public void update(float delta){
		this.statetime += delta;
		this.position.add(velocity.cpy().scl(delta));
		this.bounds.height = HEIGHT;
		this.bounds.width = WIDTH;
		this.bounds.x = this.position.x;
		this.bounds.y = this.position.y;
		this.standhitbox.height = HEIGHT / 1.5f;
		this.standhitbox.width = WIDTH / 3;
		this.standhitbox.y = ((this.position.y + HEIGHT) - this.standhitbox.height);
		this.standhitbox.x = this.position.x + (WIDTH / 3);
		this.lowerhitbox.height = HEIGHT / 3;
		this.lowerhitbox.width = WIDTH;
		this.lowerhitbox.y = this.position.y;
		this.lowerhitbox.x = this.position.x;
		this.UL.y = this.position.y + HEIGHT;
		this.UL.x = this.position.x;
		this.LR.y = this.position.y;
		this.LR.x = this.position.x + WIDTH;
		dbugvelocity = velocity.y;
		velocity.y = 0;
	}
}