/**
 * The main game class. This class instantiates the game.
 * Version : 0.1
 * This version lays the basic framework for the game. It does not have good comments, those will
 * be added in a future version.
 * It adds:
 *
 * States for Matt
 * 
 * Obstacles:
 * 				1. Dropping Bricks
 * 				2. Duck Planks
 * 				3. Fire Balls
 * 				4. Jump Planks
 * 				5. Or Planks
 * Screens:
 * 				1. Menu Screen
 * 				2. Game Screen
 * 				3. Death Screen
 * 				4. World Renderer
 * Controllers:
 * 				1. SimpleDirectionalGestureDetector (For touch screens)
 * 				2. World Controller
 */

package com.haha.bitrunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.haha.bitrunner.Controllers.WorldController;
import com.haha.bitrunner.Interfaces.AdManagement;
import com.haha.bitrunner.Interfaces.SaveData;
import com.haha.bitrunner.Screens.MenuScreen;
import com.haha.bitrunner.Screens.SplashScreen;


public class BitRunner extends Game{
	int distancerecord = 0; // The record distance ran
	public enum ApplicationType {Desktop, Android, Applet, iOS, HeadlessDesktop, WebGL};
	public static ApplicationType applicationtype = ApplicationType.Desktop;
	private SaveData savedata;
	private AdManagement admanager;
	public boolean networkavailable;
	public int deathstilad = 3; // Deaths until an ad appears
	public int deaths = 0; // Number of deaths since an ad has been displayed
		

	@Override
	public void create() {
		if(Gdx.app.getType().toString().equals("Desktop")){
			applicationtype = ApplicationType.Desktop;
		}else if(Gdx.app.getType().toString().equals("Android")){
			applicationtype = ApplicationType.Android;
		}
		setScreen(new SplashScreen(this));
	}
	
	
	public void startGame(){
		loadGameData();
		World world = new World(distancerecord, this);
		WorldController worldcontroller = new WorldController(world,this);
		
		if(applicationtype.equals(ApplicationType.Android)){
			networkavailable = isNetworkAvailable();
		}
		
		setScreen(new MenuScreen(this,world,worldcontroller));
	}
	
	
	private void loadGameData(){
		savedata.loadGameData();
		distancerecord = savedata.getHighScore();
	}
	

	/** Returns the application type. ex: desktop, android, iOS */
	public ApplicationType getApplicationType(){
		return applicationtype;
	}
	
	
	/** Sets the class currently using the SaveData interface
	 * @param savedata
	 */
	public void setSaveData(SaveData savedata){
		this.savedata = savedata;
	}
	
	
	public void setAdManager(AdManagement admanager){
		this.admanager = admanager;
	}
	
	
	public void saveGame(){
		savedata.saveGameData();
	}
	
	
	public void setHighScore(int highscore){
		savedata.setHighScore(highscore);
	}
	
	public void playMusic(boolean playmusic){
		savedata.playMusic(playmusic);
	}
	
	public boolean playMusicState(){
		return savedata.playMusicState();
	}
	
	public void displayInterstitialAd(){
		admanager.displayInterstitialAd();
	}
	
	
	public void displayBannerAd(){
		admanager.displayBannerAd();
	}
	
	
	public void removeBannerAd(){
		admanager.removeBannerAd();
	}
	
	
	public boolean interstitialAdLoaded(){
		return admanager.interstitialAdLoaded();
	}
	
	
	public boolean isNetworkAvailable(){
		return admanager.isNetworkAvailable();
	}
	
	public void loadBannerAd(){
		admanager.loadNewBanner();
	}
}
