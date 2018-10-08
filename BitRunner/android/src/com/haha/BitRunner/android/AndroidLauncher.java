package com.haha.BitRunner.android;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.haha.bitrunner.BitRunner;
import com.haha.bitrunner.Interfaces.AdManagement;
import com.haha.bitrunner.Interfaces.SaveData;

public class AndroidLauncher extends AndroidApplication implements SaveData, AdManagement {
	private int highscore = 0;
	private String gamedatafilename = "gamedata.txt";
	
	private RelativeLayout layout;
	private RelativeLayout.LayoutParams adParams;
	
	private Runnable loadBannerAd;
	private Runnable createAdViews;
	private Runnable createInterstitialAds;
	private Runnable showBannerAdRun;
	private Runnable removeBannerAdRun;
	private Runnable loadInterstitialAd;
	private Runnable showInterstitialAd;
	private Runnable isInterstitialAdLoaded;
	
	private boolean interstitialadloaded;
	private boolean playmusic = true;
	
	// The banner ad
	private AdView bitrunnerbanner;
	
	// The interstitial ad
	private InterstitialAd bitrunnerinterstitial;
	
	private AdRequest adrequest;
	
	private Random rand = new Random();
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// The banner ad
		bitrunnerbanner = new AdView(this);
		
		// The interstitial ad
		bitrunnerinterstitial = new InterstitialAd(this);
		
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		
		//"23A5C17850EEA8E8C4EF6CE8A86C6B03"
		adrequest = new AdRequest.Builder().addTestDevice("23A5C17850EEA8E8C4EF6CE8A86C6B03").build();
		
        createRunnables();
		
		layout = new RelativeLayout(this);
		
		BitRunner game = new BitRunner();
		game.setSaveData(this);
		game.setAdManager(this);
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                			 WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
		View gameView = initializeForView(game, config);
		
		layout.addView(gameView);
        
		createAdViews();
		createInterstitialAds();
	    requestNewInterstitial();
		
	    
        setContentView(layout);
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
	
	
	@Override
	public boolean playMusicState(){
		return this.playmusic;
	}
	
	
	@Override
	public void saveGameData() {
		if(getDir(gamedatafilename,0).exists()){
			try {
				FileOutputStream fos = openFileOutput(gamedatafilename,0);
				OutputStreamWriter osr = new OutputStreamWriter(fos);
				PrintWriter pw = new PrintWriter(osr);
				pw.println(highscore);
				pw.println(playmusic);
				
				try {
					osr.flush();
					osr.close();
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				pw.flush();
				pw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	
	@Override
	public void loadGameData() {
		if(getDir(gamedatafilename,0).exists()){
			try {
			FileInputStream	fis = openFileInput(gamedatafilename);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
				try {
					this.highscore = Integer.decode(br.readLine());
					this.playmusic = Boolean.parseBoolean(br.readLine());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
				try {
					fis.close();
					isr.close();
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			
		}else{ // Creates a file if no file is present
			try {
				FileOutputStream fos = openFileOutput(gamedatafilename,0);
				OutputStreamWriter osr = new OutputStreamWriter(fos);
				PrintWriter pw = new PrintWriter(osr);
				pw.println("0");
				pw.println("true");
				
				try {
					osr.flush();
					osr.close();
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				pw.flush();
				pw.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	
	public void displayInterstitialAd(){
		runOnUiThread(showInterstitialAd);
	}
	
	
	@Override
	public void displayBannerAd() {	
		runOnUiThread(showBannerAdRun);
	}
	
	
	public void removeBannerAd() {
		runOnUiThread(removeBannerAdRun);
	}
	
	
	private void createAdViews(){
		runOnUiThread(createAdViews);
	}
	
	
	private void createInterstitialAds(){
		runOnUiThread(createInterstitialAds);
	}
	
	
	/**
	 * Creates all runnables that will be used on the main UI thread.
	 */
	private void createRunnables(){
		
		// Shows a banner ad
		showBannerAdRun = new Runnable(){
			public void run(){
				layout.addView(bitrunnerbanner, adParams);
				bitrunnerbanner.setVisibility(View.VISIBLE);
			}
		};
		
		
		// Removes the currently displayed banner ad
		removeBannerAdRun = new Runnable(){
			public void run(){
				layout.removeView(bitrunnerbanner);
				bitrunnerbanner.setVisibility(View.INVISIBLE);
			}
		};
		
		
		// Loads a new banner ad, used only when needed
		loadBannerAd = new Runnable(){
			public void run(){
				bitrunnerbanner.loadAd(adrequest);
			}
		};
		
		
		// Loads a new interstitial ad, used after the old interstitial is closed
		loadInterstitialAd = new Runnable(){
			public void run(){
				bitrunnerinterstitial.loadAd(adrequest);
			}
		};
		
		
		// Shows the currently loaded interstitial ad
		showInterstitialAd = new Runnable(){
			public void run(){
				bitrunnerinterstitial.show();
			}
		};
		
		
		
		// Returns if the interstitial ad is loaded
		isInterstitialAdLoaded = new Runnable(){
			public void run(){
				interstitialadloaded = bitrunnerinterstitial.isLoaded();
			}
		};
		
		
		// Creates the banner ads
		createAdViews = new Runnable(){
			public void run(){
				
				bitrunnerbanner.setAdSize(AdSize.BANNER);
				bitrunnerbanner.setAdUnitId(getResources().getString(R.string.bitrunner_banner_id));
				bitrunnerbanner.loadAd(adrequest);
				bitrunnerbanner.setAdListener(new AdListener()
				{
					public void onAdLoaded()
					{
						bitrunnerbanner.bringToFront();
					}
				
				});
				
				adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			}
		};
		
		
		// Creates the interstitial ads
		createInterstitialAds = new Runnable(){
			public void run(){
				
			    bitrunnerinterstitial.setAdUnitId(getResources().getString(R.string.bitrunner_interstitial_id));
			    bitrunnerinterstitial.setAdListener(new AdListener(){
			    	public void onAdClosed() {
			    		requestNewInterstitial();
			    	}
			    }); 
			}
		};
	}


	@Override
	public boolean interstitialAdLoaded() {
		runOnUiThread(isInterstitialAdLoaded);
		return interstitialadloaded;
	}
	
	
	private void requestNewInterstitial(){
		runOnUiThread(loadInterstitialAd);
	}
	
	
	public void loadNewBanner(){
		runOnUiThread(loadBannerAd);
	}
	
	
	public boolean isNetworkAvailable(){
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}
}
