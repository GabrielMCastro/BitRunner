package com.haha.bitrunner.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.haha.bitrunner.BitRunner;

public class SplashScreen implements Screen {
	private float alphamod = 0;
	private float alpha = 0;
	private float deltatime;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Sprite hahalogo;
	private boolean switchbool = true;
	private boolean startgame = false;
	private BitRunner game;

	
	public SplashScreen(BitRunner game){
		this.game = game;
		this.camera = new OrthographicCamera();
	}
	
	
	@Override
	public void render(float delta) {
			if(startgame){
				game.getScreen().dispose();
				game.startGame();
			}
		
			Gdx.gl.glClearColor(249/255.0f, 242/255.0f, 183/255.0f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
			batch.begin();
			
			if(alpha > 1){
				batch.setColor(1, 1, 1, 1);
			}else if(alpha < .011){
				batch.setColor(1, 1, 1, 0);
			}else{
				batch.setColor(1, 1, 1, alpha);
			}
			
			batch.draw(hahalogo.getTexture(), hahalogo.getX(), hahalogo.getY(), hahalogo.getWidth(), hahalogo.getHeight());
			batch.end();
			deltatime += delta;
			
			if((alpha > 1.5f) && (switchbool == true)){
				switchbool = false;
			}
			
			if((switchbool) && (deltatime >= .05)){
				alpha += .03f;
				deltatime = 0;
			}else if((switchbool == false) && (deltatime >= .05)){
				alphamod += .03f;
				alpha = 1 - alphamod;
				deltatime = 0;
				if(alpha < .011){
					startgame = true;
				}
			}
			
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false);
		batch.setProjectionMatrix(camera.combined);
		hahalogo.setSize((camera.viewportHeight / 2), (camera.viewportHeight / 2) * .8f);
		hahalogo.setBounds((camera.viewportWidth - hahalogo.getWidth()) / 2, (camera.viewportHeight - hahalogo.getHeight()) / 2, hahalogo.getWidth(), hahalogo.getHeight());		
	}

	@Override
	public void show() {
		batch = new SpriteBatch();	
		
		hahalogo = new Sprite(new Texture(Gdx.files.internal("Game/HahaLogo2.png")));
		hahalogo.setSize((Gdx.graphics.getHeight() / 2), (Gdx.graphics.getHeight() / 2) * .8f);
		hahalogo.setBounds((Gdx.graphics.getWidth() - hahalogo.getWidth()) / 2, (Gdx.graphics.getHeight() - hahalogo.getHeight()) / 2, hahalogo.getWidth(), hahalogo.getHeight());
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
