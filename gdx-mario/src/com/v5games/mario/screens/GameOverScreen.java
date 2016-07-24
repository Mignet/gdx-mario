package com.v5games.mario.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.v5games.mario.Mario;

public class GameOverScreen implements Screen{
	Skin skin;
	Stage stage;
	Texture bg;
	SpriteBatch batch;
	float game_width;
	float game_height;
	Mario game; 
	 
     // constructor to keep a reference to the main Game class
       public GameOverScreen(Mario game){
               this.game = game;
       }
	
	@Override
	public void show() {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		game_width=Gdx.graphics.getWidth();
		game_height=Gdx.graphics.getHeight();
		stage = new Stage(game_width,game_height ,false);
		Gdx.input.setInputProcessor(stage);
		bg = new Texture(Gdx.files.internal("menu/gameover.jpg"));
		Texture texture = new Texture(Gdx.files.internal("menu/btn_back.jpg"));
		TextureRegion image = new TextureRegion(texture);
		Button btn = new Button(new Image(image),skin);
		btn.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				 game.setScreen(game.mainMenuScreen);
			}
		});
		Table tab = new Table(skin);
		tab.setPosition(game_width/2, game_height/4);
		tab.add(btn);
		tab.row();
		tab.layout();
		stage.addActor(tab);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
        batch.draw(bg, 0,0,game_width,game_height);
        batch.end();
        
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
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
}
