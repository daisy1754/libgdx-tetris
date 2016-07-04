package jp.gr.java_conf.daisy.tetris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;

import static jp.gr.java_conf.daisy.tetris.Stage.NUM_COLUMNS;
import static jp.gr.java_conf.daisy.tetris.Stage.NUM_ROWS;

public class Tetris extends ApplicationAdapter {

  private static final int STAGE_START_X = 70;
  private static final int STAGE_START_Y = 20;
  private static final int CELL_SIZE = 32;

  private long lastFallMillis;
  private float fallingSpeed;
  private Tetrimino currentTetrimino;
  private OrthographicCamera camera;
  private SpriteBatch batch;
  private ShapeRenderer renderer;
  private Stage stage;

  public static void renderBlock(ShapeRenderer renderer, int column, int row) {
    renderer.rect(STAGE_START_X + column * CELL_SIZE, STAGE_START_Y + row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
  }

  @Override
  public void create() {
    camera = new OrthographicCamera();
    camera.setToOrtho(false, 480, 800);
    batch = new SpriteBatch();
    renderer = new ShapeRenderer();
    fallingSpeed = 5.5f; // 7 blocks per seconds
    stage = new Stage();
    currentTetrimino = new Tetrimino();
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    if (TimeUtils.millis() - lastFallMillis > (1 / fallingSpeed) * 1000) {
      lastFallMillis = TimeUtils.millis();
      currentTetrimino.fall();
      if (stage.isOnGround(currentTetrimino.getBlocks())) {
        stage.setBlocks(currentTetrimino.getBlocks());
        currentTetrimino = new Tetrimino();
      }
    }

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
    renderer.rect(STAGE_START_X - 1, STAGE_START_Y - 1, CELL_SIZE * NUM_COLUMNS + 2, CELL_SIZE * NUM_ROWS + 2);
    renderer.end();

    renderer.begin(ShapeRenderer.ShapeType.Filled);
    renderer.setColor(Color.BLACK);
    renderer.rect(STAGE_START_X, STAGE_START_Y, CELL_SIZE * NUM_COLUMNS, CELL_SIZE * NUM_ROWS);

    currentTetrimino.render(renderer);
    stage.render(renderer);

    renderer.end();
  }
}
