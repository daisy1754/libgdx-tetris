package jp.gr.java_conf.daisy.tetris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;

import static jp.gr.java_conf.daisy.tetris.Constants.STAGE_HEIGHT;
import static jp.gr.java_conf.daisy.tetris.Constants.STAGE_WIDTH;
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
  private static final int[] SCORES = new int[] {0, 10, 30, 80, 150};

  private boolean isGameGoing = true;
  private long lastRotateMillis;
  private long lastHorizontalMoveMillis;
  private long lastFallMillis;
  private float fallingSpeed;
  private Tetromino currentTetromino;
  private Tetromino nextTetromino;
  private OrthographicCamera camera;
  private SpriteBatch batch;
  private ShapeRenderer renderer;
  private BitmapFont gameoverFont;
  private BitmapFont scoreFont;
  private Stage stage;
  private int score;

  public static void renderBlock(ShapeRenderer renderer, int column, int row) {
    renderer.rect(STAGE_START_X + column * CELL_SIZE, STAGE_START_Y + row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
  }

  @Override
  public void create() {
    camera = new OrthographicCamera();
    camera.setToOrtho(false, 480, 800);
    batch = new SpriteBatch();
    gameoverFont = new BitmapFont();
    gameoverFont.setColor(Color.BLUE);
    scoreFont = new BitmapFont();
    scoreFont.setColor(Color.WHITE);
    renderer = new ShapeRenderer();
    fallingSpeed = 4.5f; // blocks per seconds
    stage = new Stage();
    currentTetromino = Tetromino.getInstance();
    nextTetromino = Tetromino.getInstance();
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    if (!isGameGoing) {
      batch.setProjectionMatrix(camera.combined);
      batch.begin();
      gameoverFont.draw(batch, "Game over", (STAGE_WIDTH - ("Game over".length() / 2 * gameoverFont.getLineHeight())) / 2, STAGE_HEIGHT / 2 - gameoverFont.getLineHeight() / 2);
      batch.end();
      // Restart
      if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        isGameGoing = true;
        stage.reset();
        score = 0;
        currentTetromino = Tetromino.getInstance();
        nextTetromino = Tetromino.getInstance();
      }
      return;
    }

    boolean rotateInput = Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE);
    if (TimeUtils.millis() - lastFallMillis > (1 / fallingSpeed) * 1000) {
      lastFallMillis = TimeUtils.millis();
      currentTetromino.fall();
    } else if (rotateInput && TimeUtils.millis() - lastRotateMillis > MIN_ROTATE_INTERVAL_MILLIS) {
      currentTetromino.rotate(stage);
      lastRotateMillis = TimeUtils.millis();
    }

    if (stage.isOnGround(currentTetromino.getBlocks())) {
      int numDeletedRows = stage.setBlocks(currentTetromino.getBlocks());
      score += SCORES[numDeletedRows];
      currentTetromino = nextTetromino;
      currentTetromino.initPosition();
      nextTetromino = Tetromino.getInstance();
      if (stage.isOnGround(currentTetromino.getBlocks())) {
        isGameGoing = false;
        return;
      }
    } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
        && TimeUtils.millis() - lastHorizontalMoveMillis > MIN_HORIZONTAL_MOVE_INTERVAL_MILLIS) {
      currentTetromino.moveToLeft(stage);
      lastHorizontalMoveMillis = TimeUtils.millis();
    } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
        && TimeUtils.millis() - lastHorizontalMoveMillis > MIN_HORIZONTAL_MOVE_INTERVAL_MILLIS) {
      currentTetromino.moveToRight(stage);
      lastHorizontalMoveMillis = TimeUtils.millis();
    } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)
        && TimeUtils.millis() - lastFallMillis > MIN_FALL_INTERVAL_MILLIS) {
      lastFallMillis = TimeUtils.millis();
      currentTetromino.fall();
    }

    camera.update();

    renderStage();
  }

  @Override
  public void dispose() {
    batch.dispose();
    renderer.dispose();
    gameoverFont.dispose();
    scoreFont.dispose();
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
    nextTetromino.render(renderer, nextTetriminoBoxX, nextTetriminoBoxY, NEXT_TETROIMINO_SIZE / 4);

    currentTetromino.render(renderer);
    stage.render(renderer);

    renderer.end();

    batch.setProjectionMatrix(camera.combined);
    batch.begin();
    scoreFont.draw(batch, String.format("Score: %d", score), nextTetriminoBoxX, nextTetriminoBoxY - 30);
    batch.end();
  }
}
