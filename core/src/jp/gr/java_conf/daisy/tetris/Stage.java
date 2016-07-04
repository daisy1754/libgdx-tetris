package jp.gr.java_conf.daisy.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Holds the state of current stage, i.e., where blocks exist.
 */
public class Stage {

  public static final int NUM_COLUMNS = 10;
  public static final int NUM_ROWS = 22;

  private boolean[][] isFilled = new boolean[NUM_COLUMNS][NUM_ROWS];

  public void setBlock(int column, int row) {
    isFilled[column][row] = true;
  }

  public boolean isFilled(int column, int row) {
    return isFilled[column][row];
  }

  public void render(ShapeRenderer renderer) {
    renderer.setColor(Color.GRAY);
    for (int i = 0; i < NUM_COLUMNS; i++) {
      for (int j = 0; j < NUM_ROWS; j++) {
        if (isFilled[i][j]) {
          Tetris.renderBlock(renderer, i, j);
        }
      }
    }
  }
}
