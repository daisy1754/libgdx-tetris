package jp.gr.java_conf.daisy.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashSet;
import java.util.Set;

import static jp.gr.java_conf.daisy.tetris.Constants.INDEX_COLUMN;
import static jp.gr.java_conf.daisy.tetris.Constants.INDEX_ROW;

/**
 * Holds the state of current stage, i.e., where blocks exist.
 */
public class Stage {

  public static final int NUM_COLUMNS = 10;
  public static final int NUM_ROWS = 22;

  private boolean[][] isFilled = new boolean[NUM_COLUMNS][NUM_ROWS];

  public void setBlocks(int[][] blocks) {
    for (int[] block: blocks) {
      isFilled[block[INDEX_COLUMN]][block[INDEX_ROW]] = true;
    }

    // Check if any row is filled
    Set<Integer> rowsToDelete = new HashSet<Integer>();
    for (int[] block: blocks) {
      if (isRowFilled(block[INDEX_ROW])) {
        rowsToDelete.add(block[INDEX_ROW]);
      }
    }
    if (rowsToDelete.isEmpty()) {
      return;
    }
    int delta = 0;
    for (int r = 0; r < NUM_ROWS; r++) {
      if (rowsToDelete.contains(r)) {
        delta++;
      }
      for (int c = 0;  c < NUM_COLUMNS; c++) {
        isFilled[c][r] = r + delta >= NUM_ROWS ? false : isFilled[c][r + delta];
      }
    }
  }

  public boolean isOnGround(int[][] blocks) {
    for (int[] block: blocks) {
      if (block[INDEX_ROW] <= 0 || isFilled[block[INDEX_COLUMN]][block[INDEX_ROW] - 1]) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns if given blocks are inside the stage and has no conflict with existing blocks.
   */
  public boolean canPlaceBlocks(int[][] blocks) {
    for (int[] block: blocks) {
      int row = block[INDEX_ROW];
      int column = block[INDEX_COLUMN];
      if (row < 0 || column < 0 || row >= NUM_ROWS || column >= NUM_COLUMNS || isFilled[column][row]) {
        return false;
      }
    }
    return true;
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

  private boolean isRowFilled(int r) {
    for (int c = 0;  c < NUM_COLUMNS; c++) {
      if (!isFilled[c][r]) {
        return false;
      }
    }
    return true;
  }
}
