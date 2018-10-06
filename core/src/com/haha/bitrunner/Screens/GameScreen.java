/**
 * This will be the screen that is seen when the game is playing
 */

package com.haha.bitrunner.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.haha.bitrunner.BitRunner;
import com.haha.bitrunner.World;
import com.haha.bitrunner.Controllers.DirectionalGestureDetector;
import com.haha.bitrunner.Controllers.WorldController;

public class GameScreen implements Screen, InputProcessor {

	private BitRunner game;
	private World world;
	private WorldRenderer renderer;
	private WorldController controller;
	private Skin skin;
	private Stage stage;
	private ImageButton pausebutton;
	private InputProcessor touchprocessor;
	private InputProcessor keyboardprocessor;
	private InputProcessor stageprocessor;
	private InputMultiplexer inputmultiplexer = new InputMultiplexer();
	private Music backgroundmusic = Gdx.audio.newMusic(Gdx.files.internal("Audio/BackgroundMusic.mp3")); // music playing in the background
	
	@Override
	public void render(float delta) {		
		Gdx.gl.glClearColor(249/255.0f, 242/255.0f, 183/255.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		controller.update(delta);
		world.update(delta);
		renderer.render(delta);
		
		stage.act(delta);
		stage.draw();
	}

	
	@Override
	public void resize(int width, int height) { 
		renderer.setSize(width, height);
		stage.getViewport().update(width, height);
		
	}

	public GameScreen(BitRunner game, World world, WorldController worldcontroller, boolean dbug) {
		this.world = world;
		this.game = game;
		
		
		stage = new Stage();
		
		skin = new Skin(new TextureAtlas(Gdx.files.internal("bitims.pack")));
		
		ImageButtonStyle pausestyle = new ImageButtonStyle();
		pausestyle.imageUp = skin.getDrawable("PauseButton");
		pausestyle.imageOver = skin.getDrawable("PauseButtonHighlight");
		pausestyle.imageDown = skin.getDrawable("PauseButtonHighlight");

		pausebutton = new ImageButton(pausestyle);
		pausebutton.setSize(Gdx.graphics.getWidth() / 17, Gdx.graphics.getWidth() / 17);
		pausebutton.setPosition(15, Gdx.graphics.getHeight() - (pausebutton.getHeight() + 10));
		
		stage.addActor(pausebutton);
		
		pausebutton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x,float y){
				pause();
			}
		});
		
		configureInputProcessors();
		
		if(world.playmusic){
			backgroundmusic.setVolume(.2f);
			backgroundmusic.setLooping(true);
			backgroundmusic.play();
		}
		
		renderer = new WorldRenderer(world, this, worldcontroller);
		controller = worldcontroller;
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		Gdx.input.setInputProcessor(null);
		world.press.play(.3f);
		world.paused = true;
		game.setScreen(new PauseScreen(game,world,this));
	}     

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
		backgroundmusic.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.UP || keycode == Keys.W || keycode == Keys.SPACE){
			controller.JumpPressed();
		}else if(keycode == Keys.DOWN || keycode == Keys.S){
			controller.RollPressed();
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputmultiplexer);
 	}
	
	private void configureInputProcessors(){
		touchprocessor = new DirectionalGestureDetector(new DirectionalGestureDetector.DirectionListener() {
			
			@Override
			public void onUp() {
				if(WorldController.keys.get(WorldController.Keys.ROLL) == true){
					controller.RollReleased();
				}else{
					controller.JumpPressed();
				}
			}
			
			@Override
			public void onRight() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLeft() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onDown() {
				if(WorldController.keys.get(WorldController.Keys.JUMP) == true){
					controller.JumpReleased();
				}
				controller.RollPressed();
			}
		});
		
		keyboardprocessor = this;
		stageprocessor = stage;
		inputmultiplexer.addProcessor(touchprocessor);
		inputmultiplexer.addProcessor(keyboardprocessor);
		inputmultiplexer.addProcessor(stageprocessor);
	}

}