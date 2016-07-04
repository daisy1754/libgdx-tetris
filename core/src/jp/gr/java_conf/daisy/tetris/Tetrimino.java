package jp.gr.java_conf.daisy.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static jp.gr.java_conf.daisy.tetris.Constants.INDEX_COLUMN;
import static jp.gr.java_conf.daisy.tetris.Constants.INDEX_ROW;
import static jp.gr.java_conf.daisy.tetris.Stage.NUM_COLUMNS;
import static jp.gr.java_conf.daisy.tetris.Stage.NUM_ROWS;

/**
 * Group of four square that falling together.
 */
public class Tetrimino {

  // Position of "origin" of this tetrimino
  private int originColumn;
  private int originRow;
  // Position of blocks relative to origin of this block
  private int[][] relativePositions = new int[][] {
      new int[]{-1, 0}, new int[]{0, 0}, new int[]{1, 0}, new int[]{2, 0}};

  public Tetrimino() {
    originColumn = NUM_COLUMNS / 2;
    originRow = NUM_ROWS - 1;
  }

  public int[][] getBlocks() {
    return new int[][] {
        new int[] {originColumn + relativePositions[0][INDEX_COLUMN], originRow + relativePositions[0][INDEX_ROW]},
        new int[] {originColumn + relativePositions[1][INDEX_COLUMN], originRow + relativePositions[1][INDEX_ROW]},
        new int[] {originColumn + relativePositions[2][INDEX_COLUMN], originRow + relativePositions[2][INDEX_ROW]},
        new int[] {originColumn + relativePositions[3][INDEX_COLUMN], originRow + relativePositions[3][INDEX_ROW]}
    };
  }

  /**
   * Move current block down.
   */
  public void fall() {
    originRow--;
  }

  public void render(ShapeRenderer renderer) {
    renderer.setColor(Color.GREEN);
    for (int[] block: getBlocks()) {
      Tetris.renderBlock(renderer, block[INDEX_COLUMN], block[INDEX_ROW]);
    }
  }
}
