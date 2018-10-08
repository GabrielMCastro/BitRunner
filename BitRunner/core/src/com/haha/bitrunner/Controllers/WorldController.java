/**
 * This class will control all the inputs and give the proper reaction 
 */

package com.haha.bitrunner.Controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.haha.bitrunner.BitRunner;
import com.haha.bitrunner.BitRunner.ApplicationType;
import com.haha.bitrunner.World;
import com.haha.bitrunner.Actors.DuckPlank;
import com.haha.bitrunner.Actors.JumpPlank;
import com.haha.bitrunner.Actors.Matt;
import com.haha.bitrunner.Actors.Matt.State;
import com.haha.bitrunner.Actors.OrPlank;
import com.haha.bitrunner.Screens.DeathScreen;

public class WorldController {
	
	public enum Keys {
		JUMP, ROLL
	}
	
	private BitRunner game;
	private World world;
	private Matt matt;
	private Random rand = new Random();
	private float dtme = 0; // Time between cycles
	private float rtme = 0; // Roll time
	private static final float GRAVITY    = 9.81f; //Gravity of Earth
	private static final float MAGIC_PUSH = 9f;
	private DeathScreen deathscreen;
	
	public static Map<Keys, Boolean> keys = new HashMap<WorldController.Keys, Boolean>();
	
	private boolean gameoversoundplayed = false;
	public boolean rendering = false;
	
	static {
		keys.put(Keys.JUMP, false);
		keys.put(Keys.ROLL, false);
	}
	
	public WorldController(World world, BitRunner game){
		this.world = world;
		this.game = game;
		this.matt = world.getMatt();
		deathscreen = new DeathScreen(world.distanceran, game, world, this, false, world.gamebeat);
	}
	
	
	// * Key presses and touches *
	
	public void JumpPressed(){
		rtme = 0;
		if(world.paused == false){
		matt.HEIGHT = 1f;
		if((matt.state != Matt.State.FALLING) && (matt.state != Matt.State.DYING) && (matt.state != Matt.State.DEAD)){
		matt.setState(Matt.State.JUMPING);
		}
		}
	}
	
	public void RollPressed(){
		if((world.paused == false) && (matt.state != Matt.State.DYING) && (matt.state != Matt.State.DEAD)){
		matt.setState(Matt.State.ROLLING);
		matt.HEIGHT = .5f;
		}
	}
	
	public void JumpReleased(){
		if((!world.paused) && (matt.state != Matt.State.DYING) && (matt.state != Matt.State.DEAD)){
		keys.get(keys.put(Keys.JUMP, false));
		}
	}
	
	public void RollReleased(){
		if((!world.paused) && (matt.state != Matt.State.DYING) && (matt.state != Matt.State.DEAD)){
		keys.get(keys.put(Keys.ROLL, false));
		}
	}
	
	
	/* The Main update method */
	public void update(float delta){
		if(world.paused == false && rendering){
		processInput(delta);
		checkForCollisions();
		updateBounds(delta);
		if(matt.position.y <= matt.floor){
			matt.position.y = matt.floor;
			matt.velocity.y = 0;
		}
	  }
	}
	
	// Checks whether 
	private void checkForCollisions() {
		matt.onBlock = false;
		
		for(int i = 0; i < world.getDuckPlanks().size; i++){
			if((matt.LR.x >= world.getDuckPlanks().get(i).UL.x) && 
			   (matt.UL.x <= world.getDuckPlanks().get(i).LR.x) &&
			   (matt.LR.y >= world.getDuckPlanks().get(i).UL.y - (DuckPlank.SIZE / 4))){
				matt.onBlock = true;
				matt.floor = world.getDuckPlanks().get(i).UL.y;
			}
			
			if((matt.LR.x > world.getDuckPlanks().get(i).UL.x) &&
			   (matt.LR.x < world.getDuckPlanks().get(i).LR.x) &&
			   (matt.LR.y < world.getDuckPlanks().get(i).UL.y - (DuckPlank.SIZE / 3)));// &&
			   if(world.getDuckPlanks().get(i).bounds.overlaps(matt.standhitbox)){
				   matt.setState(State.DYING);
			   }
		}
		
		for(int i = 0; i < world.getJumpPlanks().size; i++){
			if((matt.LR.x >= world.getJumpPlanks().get(i).UL.x) && 
			   (matt.UL.x <= world.getJumpPlanks().get(i).LR.x) &&
			   (matt.LR.y >= world.getJumpPlanks().get(i).UL.y - (JumpPlank.SIZE / 3))){
				matt.onBlock = true;
				matt.floor = JumpPlank.SIZE;
			}
			
			if((matt.LR.x > world.getJumpPlanks().get(i).UL.x) &&
			   (matt.LR.x < world.getJumpPlanks().get(i).LR.x) &&
			   (matt.LR.y < world.getJumpPlanks().get(i).UL.y - (JumpPlank.SIZE / 3))){
				matt.setState(State.DYING);
			}
			
		}
		
		for(int i = 0; i < world.getOrPlanks().size; i++){
			if((matt.LR.x >= world.getOrPlanks().get(i).UL.x) && 
			   (matt.UL.x <= world.getOrPlanks().get(i).LR.x) &&
			   (matt.LR.y >= world.getOrPlanks().get(i).UL.y - (OrPlank.HEIGHT / 2))){
				matt.onBlock = true;
				matt.floor = world.getOrPlanks().get(i).UL.y;
			}
					
			if((matt.LR.x > world.getOrPlanks().get(i).UL.x) &&
			   (matt.LR.x < world.getOrPlanks().get(i).LR.x) &&
		       (matt.LR.y < world.getOrPlanks().get(i).UL.y - 2*(OrPlank.HEIGHT / 3)) &&
			   (matt.UL.y > world.getOrPlanks().get(i).LR.y)){
				matt.setState(State.DYING);
			}
		}
		
		for(int i = 0; i < world.getFireBalls().size; i++){
			if(matt.state.equals(State.ROLLING)){
				if(world.getFireBalls().get(i).bounds.overlaps(matt.bounds)) matt.setState(Matt.State.DYING);
			}else{
				if(world.getFireBalls().get(i).bounds.overlaps(matt.standhitbox)) matt.setState(Matt.State.DYING);
				if(world.getFireBalls().get(i).bounds.overlaps(matt.lowerhitbox)) matt.setState(Matt.State.DYING);
			}
		}
		
		for(int i = 0; i < world.getDroppingBricks().size; i++){
			if(matt.state.equals(State.ROLLING)){
				if(world.getDroppingBricks().get(i).bounds.overlaps(matt.bounds)) matt.setState(Matt.State.DYING);
			}else{
				if(world.getDroppingBricks().get(i).bounds.overlaps(matt.standhitbox)) matt.setState(Matt.State.DYING);
			}
		}
		
		if(!matt.onBlock){
			matt.floor = 0;
			if(matt.position.y > matt.floor){
				if(matt.state == State.RUNNING)	matt.setState(State.FALLING);
			}
		}
	}


	private void updateBounds(float delta) {
		
		matt.update(delta);
		
		for(int i = 0; i < world.getDuckPlanks().size; i++){
			world.getDuckPlanks().get(i).update(delta);
		}
		for(int i = 0; i < world.getJumpPlanks().size; i++){
			world.getJumpPlanks().get(i).update(delta);
		}
		for(int i = 0; i < world.getOrPlanks().size; i++){
			world.getOrPlanks().get(i).update(delta);
		}
		for(int i = 0; i < world.getFireBalls().size; i++){
			world.getFireBalls().get(i).update(delta);
		}
		for(int i = 0; i < world.getDroppingBricks().size; i++){
			world.getDroppingBricks().get(i).update(delta,world.getMatt().SPEED);
		}
	}


	// Processing the input
	private void processInput(float delta) {
		if(matt.state == Matt.State.JUMPING ){
			dtme += delta;
			matt.velocity.y = (matt.JUMPFORCE - (dtme * GRAVITY));
			if(matt.velocity.y < 0){
				matt.state = Matt.State.FALLING;
				dtme = 0;
			}

		 }else if(matt.state == Matt.State.FALLING){
			dtme += delta;
			matt.velocity.y = -(dtme * GRAVITY);
			
		
			if(matt.position.y <= matt.floor){
				matt.position.y = matt.floor;
				dtme = 0;
				matt.velocity.y = 0;
			if(matt.velocity.y == 0){
				matt.state = State.RUNNING;
			}
			}
		
	    }else if(matt.state == Matt.State.ROLLING){
	    	dtme += delta;
	    	
	    	if(matt.position.y > matt.floor){
	    	   matt.velocity.y = -MAGIC_PUSH;
	    	}
	    	
	    	if(world.rton){
	    		if(rtme > 2.5f){
	    			matt.setState(State.RUNNING);
	    			matt.HEIGHT = 1f;
	    			rtme = 0;
	    		}else{
	    			rtme += dtme;
	    		}
	    	}
	    	
	    	dtme = 0;
	    	
	    }else if(matt.state == Matt.State.DEAD){
	    	int tme = world.distanceran;
	    	boolean recordbroken = false;
	    	gameoversoundplayed = false;
	    	
	    	// Reseting the game data
	    	world.resetGame();
	    	this.dtme = 0;
	    	
			game.getScreen().dispose();
			if(tme > world.getDistanceRecord()){
				recordbroken = true;
			}
			
			deathscreen.passInfo(tme, game, world, this, recordbroken, world.gamebeat);
			world.paused = true; // Stops the processes from running
			rendering = false; // We are no longer rendering
			
			if(game.getApplicationType().equals(ApplicationType.Android)){
				game.deaths++;
				
				if((game.deaths >= game.deathstilad) && game.interstitialAdLoaded()){
					game.deaths = 0;
					game.deathstilad = randInt(3,5);
					game.loadBannerAd();
					game.displayInterstitialAd();
				}
				
				game.displayBannerAd();
			}
			
			game.setScreen(deathscreen);
			
	   }else if(matt.state == Matt.State.DYING){
		   	playGameoverSound();
	   }
	}
	
	
	private void playGameoverSound(){
		if(gameoversoundplayed == false){
			world.gameover.play(.3f);
			gameoversoundplayed = true;
		}
	}
	
	
	private int randInt(int min, int max){
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
}