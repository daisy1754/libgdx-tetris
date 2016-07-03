package jp.gr.java_conf.daisy.tetris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Tetris extends ApplicationAdapter {

  private static final int NUM_ROWS = 10;
  private static final int NUM_COLUMNS = 22;
  private static final int STAGE_START_X = 70;
  private static final int STAGE_START_Y = 20;
  private static final int CELL_SIZE = 32;

  private OrthographicCamera camera;
  private SpriteBatch batch;
  private ShapeRenderer renderer;

  @Override
  public void create() {
    camera = new OrthographicCamera();
    camera.setToOrtho(false, 480, 800);
    batch = new SpriteBatch();
    renderer = new ShapeRenderer();
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    camera.update();

    renderStage();

    batch.setProjectionMatrix(camera.combined);
    batch.begin();
    batch.end();
  }

  @Override
  public void dispose() {
    batch.dispose();
  }

  private void renderStage() {
    renderer.setProjectionMatrix(camera.combined);

    renderer.begin(ShapeRenderer.ShapeType.Line);
    renderer.setColor(Color.RED);
    renderer.rect(STAGE_START_X - 1, STAGE_START_Y - 1, CELL_SIZE * NUM_ROWS + 2, CELL_SIZE * NUM_COLUMNS + 2);
    renderer.end();

    renderer.begin(ShapeRenderer.ShapeType.Filled);
    renderer.setColor(Color.BLACK);
    renderer.rect(STAGE_START_X, STAGE_START_Y, CELL_SIZE * NUM_ROWS, CELL_SIZE * NUM_COLUMNS);
    renderer.end();
  }
}
