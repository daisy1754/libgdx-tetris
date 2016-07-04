package jp.gr.java_conf.daisy.tetris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;

public class Tetris extends ApplicationAdapter {

  private static final int NUM_COLUMNS = 10;
  private static final int NUM_ROWS = 22;
  private static final int STAGE_START_X = 70;
  private static final int STAGE_START_Y = 20;
  private static final int CELL_SIZE = 32;

  private long lastFallMillis;
  private float fallingSpeed;
  private int currentBlockColumn;
  private int currentBlockRow;
  private OrthographicCamera camera;
  private SpriteBatch batch;
  private ShapeRenderer renderer;

  @Override
  public void create() {
    camera = new OrthographicCamera();
    camera.setToOrtho(false, 480, 800);
    batch = new SpriteBatch();
    renderer = new ShapeRenderer();
    fallingSpeed = 5.5f; // 7 blocks per seconds
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    if (TimeUtils.millis() - lastFallMillis > (1 / fallingSpeed) * 1000) {
      lastFallMillis = TimeUtils.millis();
      if (fall()) {
        generateNewBlock();
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

    renderer.setColor(Color.GRAY);
    renderer.rect(STAGE_START_X + currentBlockColumn * CELL_SIZE, STAGE_START_Y + currentBlockRow * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    renderer.end();
  }

  private void generateNewBlock() {
    currentBlockColumn = NUM_COLUMNS / 2;
    currentBlockRow = NUM_ROWS - 1;
  }

  /**
   * Move current block down.
   * @return whether block needs to top at current position.
   */
  private boolean fall() {
    currentBlockRow--;
    return currentBlockRow < 0;
  }
}
