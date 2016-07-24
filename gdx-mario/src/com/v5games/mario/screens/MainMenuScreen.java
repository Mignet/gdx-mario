package com.v5games.mario.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.v5games.mario.Mario;

public class MainMenuScreen implements InputProcessor,Screen{
	private static String TAG = MainMenuScreen.class.getName();
	
	Skin skin;
	Stage stage;
	SpriteBatch batch;
	Texture bg;
	float game_width;
	float game_height;
	Mario game; // Note it's "MyGame" not "Game"
	Window dialog;
	private boolean hasdialog;
	 //对话框上的两个按钮
    Button ok;
    Button cancel;

     // constructor to keep a reference to the main Game class
       public MainMenuScreen(Mario game){
               this.game = game;
       }

	
	@Override
	public void show() {
		 Gdx.input.setCatchBackKey(true);
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		game_width=Gdx.graphics.getWidth();
		game_height=Gdx.graphics.getHeight();
		stage = new Stage(game_width,game_height ,false);
		Gdx.input.setInputProcessor(stage);
		bg = new Texture(Gdx.files.internal("menu/background.jpg"));
		Texture texture1 = new Texture(Gdx.files.internal("menu/btn_start.jpg"));
		Texture texture2 = new Texture(Gdx.files.internal("menu/btn_load.jpg"));
		Texture texture3 = new Texture(Gdx.files.internal("menu/btn_exit.jpg"));
		TextureRegion image1 = new TextureRegion(texture1);
		TextureRegion image2 = new TextureRegion(texture2);
		TextureRegion image3 = new TextureRegion(texture3);
		// button one
		Button btn1 = new Button(new Image(image1),skin);
		btn1.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(game.gameScreen);
			}
		});
		//button two
		Button btn2 = new Button(new Image(image2),skin);
		btn2.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.debug(TAG,"you click me!");
			}
		});
		//button three
		Button btn3 = new Button(new Image(image3),skin);
		btn3.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.debug(TAG,"exit!");
				if (!hasdialog) {
					stage.addActor(dialog);
					hasdialog = true;
				} else {
					stage.getRoot().removeActor(dialog);
					hasdialog = false;
				}
			}
		});
		//布局
		Table tab = new Table(skin);
		tab.setPosition(game_width - 180, 200);
		tab.row();
		tab.add(btn1).padBottom(20);
		tab.row();
		tab.add(btn2).padBottom(20);
		tab.row();
		tab.add(btn3).padBottom(20);
		tab.layout();
		stage.addActor(tab);
		//Exit Dialog
		BitmapFont bf = new BitmapFont(Gdx.files.internal("data/num.fnt"), Gdx.files.internal("data/num.png"), false);
		TextureRegionDrawable txr = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/exit-dialog.png")), 450, 225));
        dialog=new Window("EXIT",new Window.WindowStyle(bf, new Color(), txr));
        //做一个简单的适配,乘以1.2是为了让图片显示出来的时候大一点
        dialog.setWidth(450*Gdx.graphics.getWidth()/800f);
        dialog.setHeight(225*Gdx.graphics.getHeight()/480f);
        //为了让图片保持居中
        dialog.setX((Gdx.graphics.getWidth()-dialog.getWidth())/2);
        dialog.setY((Gdx.graphics.getHeight()-dialog.getHeight())/2);
        ok=new Button(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/btn_yes.png")), 150, 75)));
        ok.setX(60*Gdx.graphics.getWidth()/800f);
        ok.setY(45*Gdx.graphics.getHeight()/480f);
        //给ok这个按钮添加监听器
        ok.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                        //关闭程序
                	Gdx.app.exit();
                }
        });
        cancel=new Button(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/btn_no.png")), 150, 75)));
        cancel.setX(240*Gdx.graphics.getWidth()/800f);
        cancel.setY(45*Gdx.graphics.getHeight()/480f);
        cancel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                        //移除对话框
                        stage.getRoot().removeActor(dialog);
                        hasdialog=false;
                }
        });
        dialog.addActor(ok);
        dialog.addActor(cancel);
        //stage.addActor(dialog);
        InputMultiplexer multiplexer=new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
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
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
        batch.draw(bg, 0,0,game_width,game_height);
        batch.end();
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);
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
	public boolean keyUp(int keycode) {
		Gdx.app.debug(TAG, "you click "+keycode);
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
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
		return false;
	}


	@Override
	public boolean keyDown(int keycode) {
		Gdx.app.debug(TAG,"keyDown:"+keycode+","+(keycode==Input.Keys.BACK));
        if(keycode==Input.Keys.BACK){
                if(!hasdialog){
                        stage.addActor(dialog);
                        hasdialog=true;
                }
                else{
                        stage.getRoot().removeActor(dialog);
                        hasdialog=false;
                }
        }
        return false;
	}
}
