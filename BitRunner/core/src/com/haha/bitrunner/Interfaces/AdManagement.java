package com.haha.bitrunner.Interfaces;

/**
 * This class provides an interface to the admanagement for the android project
 * @author gabemac
 */

public interface AdManagement {

	public void displayBannerAd();
	
	public void removeBannerAd();
	
	public void loadNewBanner();
	
	public void displayInterstitialAd();
	
	public boolean interstitialAdLoaded();
	
	public boolean isNetworkAvailable();
}
