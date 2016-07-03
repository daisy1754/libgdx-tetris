package jp.gr.java_conf.daisy.tetris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tetris extends ApplicationAdapter {
  private OrthographicCamera camera;
  private SpriteBatch batch;

  @Override
  public void create() {
    camera = new OrthographicCamera();
    camera.setToOrtho(false, 480, 800);
    batch = new SpriteBatch();
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(1, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    camera.update();

    batch.setProjectionMatrix(camera.combined);
    batch.begin();
    batch.end();
  }

  @Override
  public void dispose() {
    batch.dispose();
  }
}
