package com.haha.bitrunner.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.haha.bitrunner.BitRunner;
import com.haha.bitrunner.BitRunner.ApplicationType;
import com.haha.bitrunner.World;

public class PauseScreen implements Screen, InputProcessor{

	private Stage stage;
	private ImageButton resumegame;
	private BitRunner game;
	private GameScreen gamescreen;
	private World world;
	private Skin skin;
	private Sprite paused;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	public PauseScreen(BitRunner game, World world, GameScreen gscreen){
		this.game = game;
		this.world = world;
		this.gamescreen = gscreen;
		this.camera = new OrthographicCamera();
		
		// The Ad
		if(game.getApplicationType().equals(ApplicationType.Android)){
			game.displayBannerAd();
		}
		
		
		batch = new SpriteBatch();
		
		paused = new Sprite(new Texture(Gdx.files.internal("Game/Paused.png")));
		paused.setSize(Gdx.graphics.getWidth() / 3, (Gdx.graphics.getWidth() / 3) / 2);
		paused.setBounds((Gdx.graphics.getWidth() - paused.getWidth()) / 2, ((Gdx.graphics.getHeight() - paused.getHeight()) - (Gdx.graphics.getHeight() / 60)), paused.getWidth(), paused.getHeight());

		
		stage = new Stage();
		
		skin = new Skin(new TextureAtlas(Gdx.files.internal("bitims.pack")));
		
		ImageButtonStyle resumestyle = new ImageButtonStyle();
		resumestyle.imageUp = skin.getDrawable("ResumeButton");
		resumestyle.imageOver = skin.getDrawable("ResumeButtonHighlight");
		resumestyle.imageDown = skin.getDrawable("ResumeButtonHighlight");
		
		resumegame = new ImageButton(resumestyle);
		resumegame.setSize(Gdx.graphics.getHeight() / 4, Gdx.graphics.getHeight() / 4);
		resumegame.setPosition((Gdx.graphics.getWidth() - resumegame.getWidth()) / 2,((Gdx.graphics.getHeight() / 2) - resumegame.getWidth()) );
		
		stage.addActor(resumegame);
		
		resumegame.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x,float y){
				resumeGame();
				}
		});
	}

	
	private void resumeGame() {
		Gdx.input.setInputProcessor(null);
		world.press.play(.3f);
		
		if(game.getApplicationType().equals(ApplicationType.Android)){
			game.removeBannerAd();
		}
		
		game.getScreen().dispose();
		game.setScreen(gamescreen);
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
		paused.draw(batch);
		batch.end();
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width,height);
		camera.setToOrtho(false);
		batch.setProjectionMatrix(camera.combined);
		
		paused.setSize(camera.viewportWidth / 3, (camera.viewportWidth / 3) / 2);
		paused.setBounds((camera.viewportWidth - paused.getWidth()) / 2, ((camera.viewportHeight - paused.getHeight()) - (camera.viewportHeight / 60)), paused.getWidth(), paused.getHeight());
		
		resumegame.setSize(stage.getHeight() / 4, stage.getHeight() / 4);
		resumegame.setPosition((stage.getWidth() - resumegame.getWidth()) / 2,((stage.getHeight() / 2) - resumegame.getWidth()) );

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