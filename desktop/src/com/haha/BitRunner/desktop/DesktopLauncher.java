package com.haha.BitRunner.desktop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.haha.bitrunner.BitRunner;
import com.haha.bitrunner.Interfaces.SaveData;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		BitRunner game = new BitRunner();
		game.setSaveData(new DesktopSaveData());
		new LwjglApplication(game, config);
	}
}

class DesktopSaveData implements SaveData {
	private int highscore = 0;
	private boolean playmusic = true;
	private File gamedata = new File(new java.io.File("").getAbsolutePath() + "/gamedata.txt");
	
	public DesktopSaveData(){
		
	}
	
	@Override
	public void setHighScore(int highscore) {
		this.highscore = highscore;
	}

	@Override
	public void setHighScore(String highscore) {
		this.highscore = Integer.parseInt(highscore);
	}

	@Override
	public int getHighScore() {
		return this.highscore;
	}

	@Override
	public void playMusic(boolean playmusic){
		this.playmusic = playmusic;
	}

	public boolean playMusicState(){
		return this.playmusic;
	}
	
	@Override
	public void saveGameData() {
		PrintWriter pwriter = null;
		try {
			pwriter = new PrintWriter(gamedata);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pwriter.println(highscore);
		pwriter.println(playmusic);
		pwriter.flush();
		pwriter.close();
	}

	@Override
	public void loadGameData() {
		PrintWriter pwriter = null;
		if(gamedata.exists()){ // Reading the data if the file exist
			FileReader freader = null;
			BufferedReader breader;
			try {
				freader = new FileReader(gamedata);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			breader = new BufferedReader(freader);
			
			try {
				if(breader.ready()){
					try {
						highscore = Integer.decode(breader.readLine());
						playmusic = Boolean.parseBoolean(breader.readLine());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				freader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				breader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{ // Creating a file if it doesn't exist
			try {
				pwriter = new PrintWriter(gamedata);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
				pwriter.println("0");
				pwriter.println("true");
				pwriter.flush();
				pwriter.close();
		}
	}
}
