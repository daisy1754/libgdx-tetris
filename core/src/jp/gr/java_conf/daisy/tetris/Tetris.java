package jp.gr.java_conf.daisy.tetris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;

import static jp.gr.java_conf.daisy.tetris.Stage.NUM_COLUMNS;
import static jp.gr.java_conf.daisy.tetris.Stage.NUM_ROWS;

public class Tetris extends ApplicationAdapter {

  private static final int STAGE_START_X = 25;
  private static final int STAGE_START_Y = 20;
  private static final int CELL_SIZE = 32;
  private static final int NEXT_TETROIMINO_SIZE = 80;
  private static final int MIN_HORIZONTAL_MOVE_INTERVAL_MILLIS = 50;
  private static final int MIN_FALL_INTERVAL_MILLIS = 50;
  private static final int MIN_ROTATE_INTERVAL_MILLIS = 150;

  private long lastRotateMillis;
  private long lastHorizontalMoveMillis;
  private long lastFallMillis;
  private float fallingSpeed;
  private Tetrimino currentTetrimino;
  private Tetrimino nextTetrimino;
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
    fallingSpeed = 4.5f; // blocks per seconds
    stage = new Stage();
    currentTetrimino = Tetrimino.getInstance();
    nextTetrimino = Tetrimino.getInstance();
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    boolean rotateInput = Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE);
    if (TimeUtils.millis() - lastFallMillis > (1 / fallingSpeed) * 1000) {
      lastFallMillis = TimeUtils.millis();
      currentTetrimino.fall();
    } else if (rotateInput && TimeUtils.millis() - lastRotateMillis > MIN_ROTATE_INTERVAL_MILLIS) {
      currentTetrimino.rotate(stage);
      lastRotateMillis = TimeUtils.millis();
    }

    if (stage.isOnGround(currentTetrimino.getBlocks())) {
      stage.setBlocks(currentTetrimino.getBlocks());
      currentTetrimino = nextTetrimino;
      nextTetrimino = Tetrimino.getInstance();
    } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
        && TimeUtils.millis() - lastHorizontalMoveMillis > MIN_HORIZONTAL_MOVE_INTERVAL_MILLIS) {
      currentTetrimino.moveToLeft(stage);
      lastHorizontalMoveMillis = TimeUtils.millis();
    } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
        && TimeUtils.millis() - lastHorizontalMoveMillis > MIN_HORIZONTAL_MOVE_INTERVAL_MILLIS) {
      currentTetrimino.moveToRight(stage);
      lastHorizontalMoveMillis = TimeUtils.millis();
    } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)
        && TimeUtils.millis() - lastFallMillis > MIN_FALL_INTERVAL_MILLIS) {
      lastFallMillis = TimeUtils.millis();
      currentTetrimino.fall();
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

    int nextTetriminoBoxX = CELL_SIZE * NUM_COLUMNS + 2 * STAGE_START_X;
    int nextTetriminoBoxY = STAGE_START_Y + CELL_SIZE * NUM_ROWS - NEXT_TETROIMINO_SIZE;
    renderer.rect(nextTetriminoBoxX - 1, nextTetriminoBoxY - 1, NEXT_TETROIMINO_SIZE + 2, NEXT_TETROIMINO_SIZE + 2);
    renderer.end();

    renderer.begin(ShapeRenderer.ShapeType.Filled);
    renderer.setColor(Color.BLACK);
    renderer.rect(STAGE_START_X, STAGE_START_Y, CELL_SIZE * NUM_COLUMNS, CELL_SIZE * NUM_ROWS);
    renderer.rect(nextTetriminoBoxX, nextTetriminoBoxY, NEXT_TETROIMINO_SIZE, NEXT_TETROIMINO_SIZE);
    nextTetrimino.render(renderer, nextTetriminoBoxX, nextTetriminoBoxY, NEXT_TETROIMINO_SIZE / 4);

    currentTetrimino.render(renderer);
    stage.render(renderer);

    renderer.end();
  }
}
