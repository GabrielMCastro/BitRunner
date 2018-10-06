/**
 * This will render all of the items on the screen
 */

package com.haha.bitrunner.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.haha.bitrunner.World;
import com.haha.bitrunner.Actors.DroppingBrick;
import com.haha.bitrunner.Actors.DuckPlank;
import com.haha.bitrunner.Actors.FireBall;
import com.haha.bitrunner.Actors.JumpPlank;
import com.haha.bitrunner.Actors.Matt;
import com.haha.bitrunner.Actors.Matt.State;
import com.haha.bitrunner.Actors.OrPlank;
import com.haha.bitrunner.Controllers.WorldController;

public class WorldRenderer implements Screen {
	private World world;
	private OrthographicCamera camera;
	private float width; // screen width
	private float height; // screen height
	private float WORLDWIDTH; // world width
	private float WORLDHEIGHT; // world height
	private float ppuX; // pixels per unit on X
	private float ppuY; // pixels per unit on Y
	private float numfontheightave = 100f; // Average height of the number font in pixels
	private float numfontwidthave = 61.5f; // Average width of the number font in pixels
	private float deltatime = 0;
	private float explosionaniminterval = .05f;
	private SpriteBatch batch;
	private BitmapFont score; // The current score
	private BitmapFont rand; // Bitmap font for random uses
	private BitmapFont dbuginfo = new BitmapFont(); // Debug info for memory and fps
	private BitmapFont mdbuginfo = new BitmapFont(); // Matt dbug info
	private WorldController worldcontroller;
	
	/* textures */
	private TextureRegion Ducktex;
	private TextureRegion Jumptex;
	private TextureRegion Ortex;
	private TextureRegion Firetex;
	private TextureRegion Bricktex;
	private TextureRegion Matttex;
	private TextureRegion Mattjump;
	private TextureRegion Mattfall;
	private TextureRegion Mattroll;
	private TextureRegion Mattexplode;
	
	/* Animation */
	private Animation runanimation;
	private Animation rollanimation;
	private Animation explodeanimation;
	
	/* For debug rendering */
	private ShapeRenderer debugrenderer = new ShapeRenderer();
	
	
	public void setSize(float w, float h){
		camera.setToOrtho(false);
		batch.setProjectionMatrix(camera.combined);
		this.width = w;
		this.height = h;
		ppuX = (float) width / WORLDWIDTH;
		ppuY = (float) height / WORLDHEIGHT;
		score.setScale((Gdx.graphics.getHeight() / 10) / numfontheightave);
		rand.setScale((Gdx.graphics.getHeight() / 7) / numfontheightave);
	}
	
	public WorldRenderer(World world, GameScreen game, WorldController worldcontroller){
		WORLDWIDTH = World.WIDTH;
		WORLDHEIGHT = World.HEIGHT;
		
		this.camera = new OrthographicCamera();
		this.world = world;
		this.worldcontroller = worldcontroller;
		
		batch = new SpriteBatch();
				
		loadTextures();
		
		score.setScale((Gdx.graphics.getHeight() / 10) / numfontheightave);
		rand.setScale((Gdx.graphics.getHeight() / 7) / numfontheightave);
	}
	
	private void loadTextures(){
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("bitims.pack"));
		score = new BitmapFont(Gdx.files.internal("BitRunnerFont.fnt"));
		rand = new BitmapFont(Gdx.files.internal("BitRunnerFont.fnt"));
		
		Ducktex = atlas.findRegion("DuckPlank");
		Jumptex = atlas.findRegion("JumpPlank");
		Ortex = atlas.findRegion("OrPlank");
		Firetex = atlas.findRegion("FireBall");
		Bricktex = atlas.findRegion("Dropbrick");
		Matttex = atlas.findRegion("Matt");
		Mattjump = atlas.findRegion("MattJump");
		Mattfall = atlas.findRegion("MattFall");
		Mattexplode = atlas.findRegion("Explosion1");
		
		TextureRegion[] run = new TextureRegion[5];
		for(int i = 0; i < 5; i++){ 
			run[i] = atlas.findRegion("Matt" + (i + 1));
		}
		runanimation = new Animation(.06f, run);
		
		TextureRegion[] roll = new TextureRegion[4];
		for(int i = 0; i < 4; i++){
			roll[i] = atlas.findRegion("Mattrollin" + (i + 1));
		}
		rollanimation = new Animation(.06f, roll);
		
		TextureRegion[] explode = new TextureRegion[4];
		for(int i = 0; i < 4; i++){
			explode[i] = atlas.findRegion("Explosion" + (i + 1));
		}
		explodeanimation = new Animation(explosionaniminterval, explode);
	}
	
	public void render(float delta){
		if(deltatime > ((explosionaniminterval * 4) + .07f)){
			world.getMatt().setState(State.DEAD);
			deltatime = 0;
		}
		if(world.getMatt().state == State.DYING){
			deltatime += delta;
		}
		
		batch.begin();
		renderObstacles();
		renderMatt();
		
		score.draw(batch, world.distanceran + "", (float)(Gdx.graphics.getWidth() - (score.getBounds(Integer.toString(world.distanceran)).width * 1.2)), (float)(Gdx.graphics.getHeight() + (score.getBounds(Integer.toString(world.distanceran)).height)));
				
		if(world.dbug == true){
		dbuginfo.setColor(0,0,0,1);
		dbuginfo.draw(batch, "TMem:" + Runtime.getRuntime().totalMemory() + ":FMem:" + Runtime.getRuntime().freeMemory() + ":UMem:" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + ":FPS:" + (1/delta), 0 * ppuX, 2.65f * ppuY);
		mdbuginfo.setColor(0,0,0,1);
		mdbuginfo.draw(batch, "MSpd:" + world.getMatt().SPEED + ":MHt:" + world.getMatt().HEIGHT + ":MYV:" + world.getMatt().dbugvelocity + ":State:" + world.getMatt().state  + ":YPos:" + world.getMatt().position.y, 0 * ppuX, 2.40f * ppuY);
		}
		
		if(world.paused){
			rand.draw(batch,"" + (3 - Math.round(world.getDeltaTime())), (Gdx.graphics.getWidth() - rand.getBounds("" + (3 - Math.round(world.getDeltaTime()))).width) / 2, ((Gdx.graphics.getHeight() + score.getBounds("" + (3 - Math.round(world.getDeltaTime()))).height) / 3) * 1.8f);
			rand.setScale((Gdx.graphics.getHeight() / 7) / numfontheightave);
		}
		
		batch.end();
		if(world.dbug){
		renderDebug();
		}
		worldcontroller.rendering = true;
	}

	// Renders the images of for the obstacles
	private void renderObstacles(){
		
		for(DuckPlank ducks : world.getDuckPlanks()){
			batch.draw(Ducktex, ducks.bounds.x * ppuX, ducks.bounds.y * ppuY, DuckPlank.WIDTH * ppuX, DuckPlank.HEIGHT * ppuY);
		}
		
		for(JumpPlank jumps : world.getJumpPlanks()){
			batch.draw(Jumptex, jumps.bounds.x * ppuX, jumps.bounds.y * ppuY, JumpPlank.SIZE * ppuX, JumpPlank.SIZE * ppuY);
		}
		
		for(OrPlank ors : world.getOrPlanks()){
			batch.draw(Ortex, ors.bounds.x * ppuX, ors.bounds.y * ppuY, OrPlank.WIDTH * ppuX, OrPlank.HEIGHT * ppuY);
		}
		
		for(FireBall fballs : world.getFireBalls()){
			batch.draw(Firetex, fballs.bounds.x * ppuX, fballs.bounds.y * ppuY, FireBall.WIDTH * ppuX, FireBall.HEIGHT * ppuY);
		}
		
		for(DroppingBrick dbrick : world.getDroppingBricks()){
			batch.draw(Bricktex, dbrick.bounds.x * ppuX, dbrick.bounds.y * ppuY, DroppingBrick.WIDTH * ppuX, DroppingBrick.HEIGHT * ppuY);
		}
	}
	
	private void renderMatt(){
		if(world.getMatt().state == Matt.State.RUNNING){
		Matttex = runanimation.getKeyFrame (world.getMatt().getStateTime(), true);
		batch.draw(Matttex, world.getMatt().bounds.x * ppuX, world.getMatt().bounds.y * ppuY, Matt.WIDTH * ppuX, Matt.HEIGHT * ppuY);
		
		}else if(world.getMatt().state == Matt.State.JUMPING){
		batch.draw(Mattjump, world.getMatt().bounds.x * ppuX, world.getMatt().bounds.y * ppuY, Matt.WIDTH * ppuX, Matt.HEIGHT * ppuY);
		
		}else if(world.getMatt().state == Matt.State.FALLING){
		batch.draw(Mattfall, world.getMatt().bounds.x * ppuX, world.getMatt().bounds.y * ppuY, Matt.WIDTH * ppuX, Matt.HEIGHT * ppuY);
		
		}else if(world.getMatt().state == Matt.State.ROLLING){
		Mattroll = rollanimation.getKeyFrame(world.getMatt().getStateTime(), true);
		batch.draw(Mattroll, world.getMatt().bounds.x * ppuX, world.getMatt().bounds.y * ppuY, Matt.WIDTH * ppuX, Matt.HEIGHT * ppuY);
		
		}else if(world.getMatt().state == Matt.State.DYING){
		Mattexplode = explodeanimation.getKeyFrame(deltatime, false);
		batch.draw(Mattexplode, world.getMatt().bounds.x * ppuX, (world.getMatt().bounds.y * ppuY)  + (((world.getMatt().HEIGHT - world.getMatt().WIDTH)/2) * ppuY), (Matt.WIDTH * 1.5f) * ppuX, (Matt.WIDTH * 1.5f) * ppuX);
		}
	}
	
	// Renders the a debug version of everything
	private void renderDebug() {
		debugrenderer.setProjectionMatrix(camera.combined);
		debugrenderer.begin(ShapeType.Line);
		
		// render obstacles		
		for(DuckPlank dplank : world.getDuckPlanks()){
			Rectangle dbounds = dplank.bounds;
			float x1 = dbounds.x * ppuX;
			float y1 = dbounds.y * ppuY;
			debugrenderer.setColor(1,0,0,1);
			debugrenderer.rect(x1, y1, dbounds.width * ppuX, dbounds.height * ppuY);
		}
		for(JumpPlank jplank : world.getJumpPlanks()){
			Rectangle jbounds = jplank.bounds;
			float x1 = jbounds.x * ppuX;
			float y1 = jbounds.y * ppuY;
			debugrenderer.setColor(0,0,1,1);
			debugrenderer.rect(x1, y1, jbounds.width * ppuX, jbounds.height * ppuY);
		}
		for(OrPlank orplank : world.getOrPlanks()){
			Rectangle obounds = orplank.bounds;
			float x1 = obounds.x * ppuX;
			float y1 = obounds.y * ppuY;
			debugrenderer.setColor(0,1,1,1);
			debugrenderer.rect(x1, y1, obounds.width * ppuX, obounds.height * ppuY);
		}
		
		for(FireBall fireball : world.getFireBalls()){
			Rectangle fbbounds = fireball.bounds;
			float x1 = fbbounds.x * ppuX;
			float y1 = fbbounds.y * ppuY;
			debugrenderer.setColor(0,1,1,1);
			debugrenderer.rect(x1, y1, fbbounds.width * ppuX, fbbounds.height * ppuY);
		}
		
		for(DroppingBrick dropbrick : world.getDroppingBricks()){
			Rectangle dbbounds = dropbrick.bounds;
			float x1 = dbbounds.x * ppuX;
			float y1 = dbbounds.y * ppuY;
			debugrenderer.setColor(0,1,1,1);
			debugrenderer.rect(x1, y1, dbbounds.width * ppuX, dbbounds.height * ppuY);
		}
		
		// render matt
		Matt matt = world.getMatt();
		Rectangle mbounds = matt.bounds;
		float x1 = mbounds.x * ppuX;
		float y1 = mbounds.y * ppuY;
		debugrenderer.setColor(1,0,1,1);
		debugrenderer.rect(x1, y1, mbounds.width * ppuX, mbounds.height * ppuY);
		
		debugrenderer.end();
	}

	@Override
	public void resize(int width, int height) {
		setSize(width, height);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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