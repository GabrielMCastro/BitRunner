package com.haha.bitrunner.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.haha.bitrunner.BitRunner;
import com.haha.bitrunner.BitRunner.ApplicationType;
import com.haha.bitrunner.World;
import com.haha.bitrunner.Controllers.WorldController;
	
public class DeathScreen implements Screen, InputProcessor {
  
	private float currentfontwidth;
	private int score;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ImageButton playagain;
	private ImageButton homebutton;
	private Stage stage;
	private BitRunner game;
	private World world;
	private WorldController worldcontroller;
	private BitmapFont sf;
	private Sprite highscore;
	private Sprite gameover;
	private boolean newrecord = false;
	private boolean gamebeat = false;
	private Skin skin;
	private float numfontheightave = 100f; // Average height of the number font in pixels
	private float numfontwidthave = 71.61f; // Average width of the number font in pixels

	
	public DeathScreen(int score, BitRunner game, World world, WorldController worldcontroller, boolean recordbroken, boolean gamebeat){
		this.score = score;
		this.game =  game; 
		this.world = world;
		this.worldcontroller = worldcontroller;
		this.gamebeat = gamebeat;
		this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		skin = new Skin();
		skin.addRegions(new TextureAtlas(Gdx.files.internal("bitims.pack")));

		loadTextures();
				
		
		stage = new Stage();
		
		batch = new SpriteBatch();

		sf = new BitmapFont(Gdx.files.internal("BitRunnerFont.fnt"));
		sf.setScale((camera.viewportHeight / 4f) / numfontheightave);
				
		ImageButtonStyle playstyle = new ImageButtonStyle();
		playstyle.imageUp = skin.getDrawable("PlayButton");
		playstyle.imageDown = skin.getDrawable("PlayButtonClicked");
		playstyle.imageOver = skin.getDrawable("PlayButtonHighlighted");
		
		playagain = new ImageButton(playstyle);
		
		ImageButtonStyle homestyle = new ImageButtonStyle();
		homestyle.imageUp = skin.getDrawable("HomeButton");
		homestyle.imageOver = skin.getDrawable("HomeButtonHighlighted");
		
		homebutton = new ImageButton(homestyle);

		
		stage.addActor(playagain);
		stage.addActor(homebutton);
		
		if(recordbroken){
			newrecord = true;
			recordBroken();
		}
		
		
		// Setting up a new game
		playagain.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x,float y){
				playNewGame();
				}
		});	
		
		
		homebutton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x,float y){
				returnToHome();
				}
		});	
	}
	
	
	private void loadTextures() {
		highscore = new Sprite(new Texture(Gdx.files.internal("Game/HighScore.png")));
		gameover = new Sprite(new Texture(Gdx.files.internal("Game/Over.png")));
	}


	// Takes care of all the record broken stuff
	private void recordBroken() {
		world.distancerecord = score;
		game.setHighScore(score);
		game.saveGame();
	}


	private void returnToHome(){
		Gdx.input.setInputProcessor(null);
		world.press.play(.3f);
		newrecord = false;
		world.gamebeat = false;
		if(game.getApplicationType().equals(ApplicationType.Android)){
			game.removeBannerAd();
		}
		game.getScreen().dispose();
		game.setScreen(new MenuScreen(game, world, worldcontroller));
	}
	
	
	private void playNewGame(){
		Gdx.input.setInputProcessor(null);
		world.press.play(.3f);
		newrecord = false;
		world.gamebeat = false;
		if(game.getApplicationType().equals(ApplicationType.Android)){
			game.removeBannerAd();
		}
		game.getScreen().dispose();
		world.paused = false;
		world.setTimeBetweenObstacles(World.NEW_GAME_OBSTACLE_BUFFER);
		game.setScreen(new GameScreen(game, world, worldcontroller, world.dbug));
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
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(249/255.0f, 242/255.0f, 183/255.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
 		batch.begin();
 		
 		if(gamebeat){
 		sf.setScale((camera.viewportHeight / 6f) / numfontheightave);
 		sf.draw(batch, "GAME BEAT!", (camera.viewportWidth - sf.getBounds("GAME BEAT!").width) / 2, (camera.viewportHeight / 2) + (sf.getBounds("" + score).height * 1.85f));
		sf.setScale((camera.viewportHeight / 4f) / numfontheightave);
 		}else{
 			if(sf.getBounds(""+ score).width > (camera.viewportWidth - 50)){
 				sf.setScale(1);
 				currentfontwidth = sf.getBounds("" + score).width;
 				sf.setScale(((camera.viewportWidth * currentfontwidth) / (camera.viewportWidth + 50)) / currentfontwidth);
 			}
		sf.draw(batch, "" + score, (camera.viewportWidth - sf.getBounds("" + score).width) / 2, (camera.viewportHeight / 2) + (sf.getBounds("" + score).height * 1.85f));
		sf.setScale((camera.viewportHeight / 4f) / numfontheightave);
 		}
 		
		if(newrecord){
			highscore.draw(batch);
		}else{
			gameover.draw(batch);
		}
		
		batch.end();
		Table.drawDebug(stage);
		stage.act(delta);
		stage.draw();
	 }
	
	
	public void passInfo(int score, BitRunner game, World world, WorldController worldcontroller, boolean recordbroken, boolean gamebeat){
		this.score = score;
		this.game =  game; 
		this.world = world;
		this.worldcontroller = worldcontroller;
		this.gamebeat = gamebeat;
		
		if(recordbroken){
			newrecord = true;
			recordBroken();
		}
	}
	
	private void setSize(int width, int height){
		camera.setToOrtho(false);
		batch.setProjectionMatrix(camera.combined);
		stage.getViewport().update(width, height);
		
		highscore.setSize((camera.viewportHeight / 3), (camera.viewportHeight / 3));
		highscore.setBounds((camera.viewportWidth - highscore.getWidth()) / 2, ((camera.viewportHeight - highscore.getHeight()) - (camera.viewportHeight / 100)), highscore.getWidth(), highscore.getHeight());
		
		gameover.setSize(camera.viewportWidth / 2, (camera.viewportWidth / 2) / 5);
		gameover.setBounds((camera.viewportWidth - gameover.getWidth()) / 2, ((camera.viewportHeight - gameover.getHeight()) - (camera.viewportHeight / 80)), gameover.getWidth(), gameover.getHeight());

		playagain.setSize(stage.getWidth() / 6, (stage.getWidth() / 6) / 2);
		playagain.setPosition((stage.getWidth() - playagain.getWidth()) / 2, stage.getHeight() / 4);

		homebutton.setSize(stage.getWidth() / 6, stage.getWidth() / 6);
		homebutton.setPosition((stage.getWidth() - playagain.getWidth()) / 32, stage.getHeight() / 32);
	}
	
	@Override
	public void resize(int width, int height) {	
		setSize(width, height);
	}

	@Override
	public void show() {  
		Gdx.input.setInputProcessor(stage);		
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

}