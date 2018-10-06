/**
 * This class will encompass all the definitions and objects in the world 
 */

package com.haha.bitrunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.haha.bitrunner.Actors.DroppingBrick;
import com.haha.bitrunner.Actors.DuckPlank;
import com.haha.bitrunner.Actors.FireBall;
import com.haha.bitrunner.Actors.JumpPlank;
import com.haha.bitrunner.Actors.Matt;
import com.haha.bitrunner.Actors.Matt.State;
import com.haha.bitrunner.Actors.OrPlank;

public class World {
	public static final float NEW_GAME_OBSTACLE_BUFFER = 1.5f; // Number of seconds between a new game beginning and an obstacle appearing
	public static final float WIDTH = 5; // units wide
	public static final float HEIGHT = 3; // units high
	private final Rectangle bounds = new Rectangle();
	public int distanceran = 0; // The distance matt has been running
	public int distancerecord = 0; // Matt's record distance, will be loaded from a save file
	public boolean dbug = false; // Debug mode
	public boolean rton = false; // roll timer
	public boolean gamebeat = false; // if the game is beat or not
	public boolean paused = false; // if the game is paused or not
	boolean changefirebally = true; // Changes the height at which the fireball shoots
	int num = 0; // Used when a random number is needed
	int recentobstacle = 0; // The obstacle just added
	float timecheck = 0; // The time running since the last obstacle was placed
	float runtimecheck = 0; // The time running since last speed up
	float timeaddend = 0; // The time that is added to timecheck  to calculate when an object should be placed. The time it takes to cross the last object placed
	float distancetillspeedup = 400; // The distance until the speed is increased
	float deltatime = 0; // The time between every update
	float newobstacletime = 0; // The time between a new obstacle is added
	Array<DroppingBrick> dropbricks = new Array<DroppingBrick>();
	Array<FireBall> fireballs = new Array<FireBall>(); 
	Array<DuckPlank> duckplanks = new Array<DuckPlank>();
	Array<JumpPlank> jumpplanks = new Array<JumpPlank>();
	Array<OrPlank> orplanks = new Array<OrPlank>();
	Matt matt;
	Vector2 veloc = new Vector2();
	int dbyDestination = 0; // where the dropping brick will land. 1 = on the floor 2 = right above you
	public Sound press = Gdx.audio.newSound(Gdx.files.internal("Audio/Press.mp3")); // sound a button makes
	public Sound gameover = Gdx.audio.newSound(Gdx.files.internal("Audio/GameOver.mp3")); // sound when you die
	public boolean playmusic = true;
	public BitRunner game;
	
	public Array<DuckPlank> getDuckPlanks(){
		return duckplanks;
	}
	
	public Array<JumpPlank> getJumpPlanks(){
		return jumpplanks;
	}
	
	public Array<OrPlank> getOrPlanks(){
		return orplanks;
	}
	
	public Array<FireBall> getFireBalls(){
		return fireballs;
	}
	
	public Array<DroppingBrick> getDroppingBricks(){
		return dropbricks;
	}
	
	public Matt getMatt(){
		return matt;
	}

	public void addDuckPlank(DuckPlank obstacle){
		duckplanks.add(obstacle);
	}
	
	public void addJumpPlank(JumpPlank obstacle){
		jumpplanks.add(obstacle);
	}
	
	public void addOrPlank(OrPlank obstacle){
		orplanks.add(obstacle);
	}
	
	public void addFireBall(FireBall obstacle){
		fireballs.add(obstacle);
	}
	
	public void addDroppingBrick(DroppingBrick obstacle){
		dropbricks.add(obstacle);
	}
	
	public Rectangle getBounds(){
		return bounds;
	}
	
	public World(int distancerecord, BitRunner game){
		this.game = game;
		this.bounds.width = WIDTH;
		this.bounds.height = HEIGHT;
		this.distancerecord = distancerecord;
		this.playmusic = game.playMusicState();
		
		matt = new Matt(new Vector2(1,0));
		for(int i = 0; i < 4; i++){		
			addDuckPlank(new DuckPlank(new Vector2(WIDTH + 1,.80f)));
			addJumpPlank(new JumpPlank(new Vector2(WIDTH + 1,0)));
			addOrPlank(new OrPlank(new Vector2(WIDTH + 1,.70f)));
			addFireBall(new FireBall(new Vector2(WIDTH + 1,1), true));
			addDroppingBrick(new DroppingBrick(new Vector2(WIDTH, HEIGHT), WIDTH, HEIGHT, matt.SPEED));
			}
	}	
		
	public void update(float delta){
		if(paused == false){
		moveObstacles(delta);	 	
		checkObstacles();
		timecheck += delta;
		runtimecheck += delta;
		
		if(runtimecheck > ((1/matt.SPEED)/8)){
			distanceran++;
			runtimecheck = 0;
		}
		
		if(distanceran >= 214748000){
			matt.setState(State.DEAD);
			gamebeat = true;
		}
		
		if(matt.SPEED < 3.8f && distanceran > distancetillspeedup){
			matt.SPEED += .5;
			distancetillspeedup += (2 * (200 * (matt.SPEED - 1)));	
		}

		if(timecheck > newobstacletime){
			addObstacle();
			if(newobstacletime == NEW_GAME_OBSTACLE_BUFFER){
				setTimeBetweenObstacles(1f);
			}
			timecheck = 0;
		}
		}else{
			deltatime += delta;
			if(deltatime > 2){
				deltatime = 0;
				paused = false;
			}
		}
		
	}	

	

	// Moves the obstacles to give the appearance of running
	private void moveObstacles(float delta) {
		veloc.x = -matt.SPEED;
		veloc.y = 0;
		for(JumpPlank jump : getJumpPlanks()){
			if(jump.onscreen){
			jump.position.add(veloc.cpy().scl(delta));
			}
 		}
		for(DuckPlank ducks : getDuckPlanks()){
			if(ducks.onscreen){
			ducks.position.add(veloc.cpy().scl(delta));
			}
		}
		for(OrPlank ors : getOrPlanks()){
			if(ors.onscreen){
			ors.position.add(veloc.cpy().scl(delta));
			}
		}
		for(FireBall fballs : getFireBalls()){
			if(fballs.onscreen){
			if(fballs.position.y == .5f) veloc.x -= .5;
			veloc.x -= 2*timeaddend;
			fballs.position.add(veloc.cpy().scl(delta));
			veloc.x = -matt.SPEED;
			}
		}
		for(DroppingBrick dbrick : getDroppingBricks()){
			if(dbrick.onScreen){
				veloc.y = -dbrick.yMoveSpeed;
			dbrick.position.add(veloc.cpy().scl(delta));
			veloc.x = -matt.SPEED;
			}
		}
	}

	
	// Checks the obstacles locations, so that if they are not visible they are deleted
	private void checkObstacles(){

		for(int i = 0; i < duckplanks.size; i++){
			if(duckplanks.get(i).position.x <= -1){
				duckplanks.get(i).setPosition(new Vector2(WIDTH + 1, .80f));
				duckplanks.get(i).onscreen = false;
			}
		}
		for(int i = 0; i < jumpplanks.size; i++){
			if(jumpplanks.get(i).position.x <= -1){
				jumpplanks.get(i).setPosition(new Vector2(WIDTH + 1, 0));
				jumpplanks.get(i).onscreen = false;
				
			}
		}
		for(int i = 0; i < orplanks.size; i++){
			if(orplanks.get(i).position.x <= -1){
				orplanks.get(i).setPosition(new Vector2(WIDTH + 1, .70f));
				orplanks.get(i).onscreen = false;
					
			}
		}
		for(int i = 0; i < fireballs.size; i++){
			if(fireballs.get(i).position.x <= -1){
				fireballs.get(i).setPosition(new Vector2(WIDTH + 1, 1), false);
				fireballs.get(i).onscreen = false;
				
			}
		}
		for(int i = 0; i < dropbricks.size; i++){

			if(dropbricks.get(i).position.x <= -1){
				dropbricks.get(i).setPosition(new Vector2(WIDTH, HEIGHT));
				dropbricks.get(i).onScreen = false;
						
			}
		}
		
	}
	
	
	// Adds the next random obstacle to the course
	private void addObstacle() {		
		addRandomObstacle();
	}
	
	
	/* Adds a random obstacle, 
	 * ex specifies the obstacle not considered in the evaluation
	 * 0 = DropBrick
	 * 1 = DuckPlank
	 * 2 = FireBall
	 * 3 = JumpPlank
	 * 4 = OrPlank
	 * 5 = All obstacles considered
	 * h specifies the height above the ground for the fireball
	 */ 
	private void addRandomObstacle() {
		num = (int) Math.round(Math.random() * 5);
		if(num == 0) { 
			l:  for(DroppingBrick dbrick : getDroppingBricks()){
				if(dbrick.onScreen == false){
					dbrick.setPosition((new Vector2(WIDTH,HEIGHT)));
					dbrick.setYSpeed();
					dbrick.onScreen = true;
					dbyDestination = dbrick.yDestination;
					timeaddend = ((dbrick.WIDTH / matt.SPEED));
					changefirebally = true;
					break;
				}
			}
		}
			
		if(num == 1) {
			l: for(DuckPlank dplank : getDuckPlanks()){
				if(dplank.onscreen == false){
					dplank.setPosition(new Vector2(WIDTH,.80f));
					dplank.onscreen = true;
					timeaddend = ((dplank.WIDTH / matt.SPEED));
					changefirebally = true;
					break l;
				}
		    }
		}
		
		if(num == 2) {
			l: for(FireBall fball : getFireBalls()){
				if(fball.onscreen == false){
					if((dbyDestination != 2) && (recentobstacle != 1)){
						fball.setPosition(new Vector2(WIDTH,0),changefirebally);
					}else{
						fball.setPosition(new Vector2(WIDTH,0),false);
					}
					dbyDestination = 0;
					fball.onscreen = true;
					timeaddend = ((fball.WIDTH / matt.SPEED));
					changefirebally = true;
					break l;
				}
		    }
		}
		
		if(num == 3) {
			l: for(JumpPlank jplank : getJumpPlanks()){
				if(jplank.onscreen == false){
					jplank.setPosition(new Vector2(WIDTH,0));
					jplank.onscreen = true;
					timeaddend = ((jplank.SIZE / matt.SPEED));
					changefirebally = false;
					break l;
				}
		    }
		}
		
		if(num == 4) {
			l: for(OrPlank oplank : getOrPlanks()){
				if(oplank.onscreen == false){
					oplank.setPosition(new Vector2(WIDTH,.55f));
					oplank.onscreen = true;
					timeaddend = ((oplank.WIDTH / matt.SPEED));
					changefirebally = true;
					break l;
				}
		    }
		}
		
		dbyDestination = 0;
		recentobstacle = num;
 	}
	
	public int getDistanceRecord(){
		return distancerecord;
	}
	
	
	// Resets the game so that a new one can be played
	public void resetGame(){
		distanceran = 0;
		duckplanks.clear();
		jumpplanks.clear();
		orplanks.clear();
		fireballs.clear();
		dropbricks.clear();
		
		for(int i = 0; i < 4; i++){		
			addDuckPlank(new DuckPlank(new Vector2(WIDTH + 1,.80f)));
			addJumpPlank(new JumpPlank(new Vector2(WIDTH + 1,0)));
			addOrPlank(new OrPlank(new Vector2(WIDTH + 1,.70f)));
			addFireBall(new FireBall(new Vector2(WIDTH + 1,1), true));
			addDroppingBrick(new DroppingBrick(new Vector2(WIDTH, HEIGHT), WIDTH, HEIGHT, matt.SPEED));
		}
		
		matt.position = new Vector2(1,0);
		matt.setState(Matt.State.RUNNING);
    	matt.SPEED = 1.8f;		
    	matt.HEIGHT = 1;
	}
	
	
	// Returns the delta time
	public float getDeltaTime(){
		return deltatime;
	}

	
	public void setTimeBetweenObstacles(float obtime){
		newobstacletime = obtime;
	}
}