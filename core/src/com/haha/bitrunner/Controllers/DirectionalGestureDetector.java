package com.haha.bitrunner.Controllers;

import com.badlogic.gdx.input.GestureDetector;

public class DirectionalGestureDetector extends GestureDetector {
	public interface DirectionListener {
		void onLeft();
		
		void onRight();
		
		void onUp();
		
		void onDown();
	}
	
	public DirectionalGestureDetector(DirectionListener directionlistener){
			super(new DirectionGestureListener(directionlistener));
	}

	private static class DirectionGestureListener extends GestureAdapter {
		DirectionListener directionlistener;
		
		public DirectionGestureListener(DirectionListener directionlistener){
				this.directionlistener = directionlistener;
		}
		
		@Override
		
		public boolean fling(float velocityX, float velocityY, int button){
			if(Math.abs(velocityX)>Math.abs(velocityY)){
				if(velocityX>0){
						directionlistener.onRight();
				}else{
						directionlistener.onLeft();
				}
			}else{
				if(velocityY>0){
						directionlistener.onDown();
				}else{                                  
						directionlistener.onUp();
				}
			}
			return super.fling(velocityX, velocityY, button); 
		}
	}
}