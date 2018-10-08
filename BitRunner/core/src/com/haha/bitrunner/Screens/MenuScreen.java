/**
 * The first screen seen in the game
 */

package com.haha.bitrunner.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.haha.bitrunner.BitRunner;
import com.haha.bitrunner.World;
import com.haha.bitrunner.Controllers.WorldController;

public class MenuScreen implements Screen, InputProcessor{

	private InputMultiplexer inputmultiplexer = new InputMultiplexer();
	private BitRunner game;
	private float WORLDWIDTH; // world width
	private float WORLDHEIGHT; // world height
	private SpriteBatch batch;
	private Sprite logo;
	private Sprite help;
	private World world;
	private WorldController worldcontroller;
	private Animation runanimation;
	private OrthographicCamera camera;
	private BitmapFont sf;
	private float ppuX; // pixels per unit on X
	private float ppuY; // pixels per unit on Y
	private float numfontheightave = 100f; // Average height of the number font in pixels
	private float statetime;
	private String typed = "";
	private ImageButton musicbutton; // The button that handles whether the music is on or not
	private ImageButton helpbutton; // The button that shows you how to play
	private Skin skin;
	private Stage stage;
	private boolean showhelp = false;
	
	int touchy = 0;

	
	public MenuScreen(BitRunner game, World world, WorldController worldcontroller){
		this.game = game;
		this.world = world;
		this.worldcontroller = worldcontroller;
		
		camera = new OrthographicCamera();
		
		skin = new Skin();
		skin.addRegions(new TextureAtlas(Gdx.files.internal("bitims.pack")));
		
		WORLDWIDTH = World.WIDTH;
		WORLDHEIGHT = World.HEIGHT;
		
		ppuX = (float) Gdx.graphics.getWidth() / WORLDWIDTH;
		ppuY = (float) Gdx.graphics.getHeight() / WORLDHEIGHT;
		
		sf = new BitmapFont(Gdx.files.internal("BitRunnerFont.fnt"));
		sf.setScale((Gdx.graphics.getHeight() / 10f) / numfontheightave);
		
		stage = new Stage();
		
		ImageButtonStyle musicstyle = new ImageButtonStyle();
		musicstyle.imageChecked = skin.getDrawable("MusicOffButton");
		musicstyle.imageUp = skin.getDrawable("MusicOnButton");
		
		musicbutton = new ImageButton(musicstyle);
		musicbutton.setSize(Gdx.graphics.getHeight() / 8, Gdx.graphics.getHeight() / 8);
		musicbutton.setPosition(Gdx.graphics.getWidth() - (musicbutton.getWidth() * 1.5f), Gdx.graphics.getHeight() - (musicbutton.getHeight() * 1.5f));
		
		if(world.playmusic){
			musicbutton.setChecked(false);
		}else{
			musicbutton.setChecked(true);
		}
		
		stage.addActor(musicbutton);
		
		musicbutton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x,float y){
				if(showhelp == false){
					changeMusicState();
				}else if(showhelp == true){
					showhelp = false;
				}
			}
		});
		

		ImageButtonStyle helpstyle = new ImageButtonStyle();
		helpstyle.imageUp = skin.getDrawable("HelpButton");
		
		helpbutton = new ImageButton(helpstyle);
		helpbutton.setSize(Gdx.graphics.getHeight() / 8, Gdx.graphics.getHeight() / 8);
		helpbutton.setPosition(Gdx.graphics.getWidth() - ((musicbutton.getWidth() * 1.5f) * 2f), Gdx.graphics.getHeight() - (musicbutton.getHeight() * 1.5f));
		
		stage.addActor(helpbutton);
		
		helpbutton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x,float y){
				if(showhelp == false){
					showHelp();
				}
			}
		});
		
		batch = new SpriteBatch();	
		loadTextures();
	}
	
	
	private void showHelp(){
		inputmultiplexer.removeProcessor(stage);
		showhelp = true;
		world.press.play(.3f);
	}
	
	
	private void changeMusicState(){
		world.press.play(.3f);
		world.playmusic = !world.playmusic;
		game.playMusic(world.playmusic);
		game.saveGame();
		musicbutton.setChecked(musicbutton.isChecked());
	}
	
	
	private void loadTextures() {		
		logo = new Sprite(new Texture(Gdx.files.internal("Game/Logo.png")));
		logo.setSize((Gdx.graphics.getHeight() / 2) * 1.92f, (Gdx.graphics.getHeight() / 2));
		logo.setBounds((Gdx.graphics.getWidth() - logo.getWidth()) / 2, (Gdx.graphics.getHeight() - (logo.getHeight() * 1.2f)), logo.getWidth(), logo.getHeight());
		
		help = new Sprite(new Texture(Gdx.files.internal("Game/HelpInstruction.png")));
		help.setSize(Gdx.graphics.getHeight(), Gdx.graphics.getHeight());
		help.setPosition((Gdx.graphics.getWidth() - help.getWidth()) / 2, 0);
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("bitims.pack"));
		TextureRegion[] run = new TextureRegion[5];
		for(int i = 0; i < 5; i++){ 
			run[i] = atlas.findRegion("Matt" + (i + 1));
		}
		runanimation = new Animation(.06f, run);
	}

	public void setSize(float w, float h){
		camera.setToOrtho(false);
		batch.setProjectionMatrix(camera.combined);
		
		stage.getViewport().update((int)w,(int)h);

		
		ppuX = (float) Gdx.graphics.getWidth() / WORLDWIDTH;
		ppuY = (float) Gdx.graphics.getHeight() / WORLDHEIGHT;
		
		sf.setScale((camera.viewportHeight / 10f) / numfontheightave);
		
		helpbutton.setSize(stage.getHeight() / 8, stage.getHeight() / 8);
		helpbutton.setX(musicbutton.getX() - (musicbutton.getWidth() * 1.5f));
		helpbutton.setPosition(musicbutton.getX() - (musicbutton.getWidth() * 1.5f), stage.getHeight() - (musicbutton.getHeight() * 1.5f));
		
		musicbutton.setSize(stage.getHeight() / 8, stage.getHeight() / 8);
		musicbutton.setPosition(stage.getWidth() - (musicbutton.getWidth() * 1.5f), stage.getHeight() - (musicbutton.getHeight() * 1.5f));

		
		help.setSize(camera.viewportHeight, camera.viewportHeight);
		help.setPosition((camera.viewportWidth - help.getWidth()) / 2, 0);
		
		logo.setSize((camera.viewportHeight / 2) * 1.92f, (camera.viewportHeight / 2));
		logo.setBounds((camera.viewportWidth - logo.getWidth()) / 2, (camera.viewportHeight - (logo.getHeight() * 1.2f)), logo.getWidth(), logo.getHeight());
		
	} 
	
	@Override
	public void render(float delta)     {
		Gdx.gl.glClearColor(249/255.0f, 242/255.0f, 183/255.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(!showhelp){
			statetime += delta;
		
			batch.begin();
			batch.draw(runanimation.getKeyFrame(statetime, true), 1 * ppuX, 0, .5f * ppuX, 1 * ppuY);
			logo.draw(batch);
			sf.draw(batch, "High Score: " + world.distancerecord, ((camera.viewportWidth - (sf.getBounds("High Score: " + world.distancerecord).width + 20))), (camera.viewportHeight / 4));
			batch.end();
			stage.act();
			stage.draw();
		}else{
			batch.begin();
			help.draw(batch);
			batch.end();
		}
	}

	@Override
	public void resize(int width, int height) {
		setSize(width, height);
	}

	@Override
	public void show() {
		inputmultiplexer.addProcessor(this);
		inputmultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputmultiplexer);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {	
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if(character != '\r'){
			typed += character;
		}else{
		 if(typed.equals("debug")){
			world.dbug = !world.dbug;
		 }else if(typed.equals("rton")){
			 world.rton = !world.rton;
		 }else{
		 }
		 typed = "";
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touchy = screenY;
		if((!((screenX >= helpbutton.getX()) && (screenY <= (stage.getHeight() - musicbutton.getY()))) && !showhelp)){
			Gdx.input.setInputProcessor(null);
			world.press.play(.3f);
			game.getScreen().dispose();
			world.paused = false;
			world.setTimeBetweenObstacles(World.NEW_GAME_OBSTACLE_BUFFER);
			game.setScreen(new GameScreen(game,world,worldcontroller,world.dbug));
		}else if(showhelp){
			inputmultiplexer.addProcessor(stage);
			showhelp = false;
			world.press.play(.3f);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}