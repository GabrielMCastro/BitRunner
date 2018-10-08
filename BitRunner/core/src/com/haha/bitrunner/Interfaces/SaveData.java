package com.haha.bitrunner.Interfaces;

/**
 * This class provides an interface to save data.
 * @author gabemac
 */

public interface SaveData {
	
	public void setHighScore(int highscore);
	
	public void setHighScore(String highscore);
	
	public int getHighScore();
	
	public void saveGameData();
	
	public void loadGameData();
	
	public void playMusic(boolean playmusic);
	
	public boolean playMusicState();
}
