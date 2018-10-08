/**
 * Contains all the definitions for a ball of fire that whizzes by the screen
 */
package com.haha.bitrunner.Actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class FireBall extends Obstacle {
	    public static final float WIDTH = .75f;
	    public static final float HEIGHT = .2f;
	    public Rectangle   bounds = new Rectangle();
	    public Vector2 position = new Vector2();
	    public boolean onscreen = false;
	    
	    /* Creates a fire ball, the vector gives it's position, 
	     * the boolean specifies whether the y-pos should be changed.
	     */
	    public FireBall(Vector2 position, boolean ychange) {
	    	setPosition(position, ychange);
	    }
	    
		public void update(float delta){
			this.bounds.x = this.position.x;
			this.bounds.y = this.position.y;
		}
		
		/* the vector gives it's position, the boolean specifies whether the y-pos should be changed.
	     */
		public void setPosition(Vector2 position, boolean ychange){
			this.position = position;
			if(ychange){
				int num = (int) Math.round(Math.random() * 3);
				if(num == 0){
					this.position.y = .25f;
				}else if(num == 1){
					this.position.y = .55f;
				}else if(num == 1){
					this.position.y = (float) Math.random();
				}
			}else{
				this.position.y = .55f;
			}
	    	this.bounds.width = WIDTH;
	    	this.bounds.height = HEIGHT;
	    	this.bounds.x = this.position.x;
			this.bounds.y = this.position.y;	
		}
}