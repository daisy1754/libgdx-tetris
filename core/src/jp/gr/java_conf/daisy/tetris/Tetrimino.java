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

  private static final int[][][] TETORIMINOS = new int[][][] {
      // xx
      // xo
      new int[][] {new int[]{-1, 0}, new int[]{0, 0}, new int[]{0, 1}, new int[]{-1, 1}},
      //  x
      // xox
      new int[][] {new int[]{-1, 0}, new int[]{0, 0}, new int[]{1, 0}, new int[]{0, 1}},
      // x
      // xox
      new int[][] {new int[]{-1, 0}, new int[]{0, 0}, new int[]{1, 0}, new int[]{-1, 1}},
      //   x
      // xox
      new int[][] {new int[]{-1, 0}, new int[]{0, 0}, new int[]{1, 0}, new int[]{1, 1}},
      // xx
      //  ox
      new int[][] {new int[]{1, 0}, new int[]{0, 0}, new int[]{0, 1}, new int[]{-1, 1}},
      //  xx
      // xo
      new int[][] {new int[]{-1, 0}, new int[]{0, 0}, new int[]{0, 1}, new int[]{1, 1}},
      // xoxx
      new int[][] {new int[]{-1, 0}, new int[]{0, 0}, new int[]{1, 0}, new int[]{2, 0}}
  };
  // Position of "origin" of this tetrimino
  private int originColumn;
  private int originRow;
  // Position of blocks relative to origin of this block
  private int[][] relativePositions;

  public Tetrimino() {
    originColumn = NUM_COLUMNS / 2;
    originRow = NUM_ROWS - 1;
    relativePositions = TETORIMINOS[(int) (Math.random() * TETORIMINOS.length)];
  }

  public int[][] getBlocks() {
    return getBlocks(relativePositions);
  }

  /**
   * Move current block down.
   */
  public void fall() {
    originRow--;
  }

  public void rotate(Stage stage) {
    int[][] rotated = new int[][] {
        new int[] {-relativePositions[0][INDEX_ROW], relativePositions[0][INDEX_COLUMN]},
        new int[] {-relativePositions[1][INDEX_ROW], relativePositions[1][INDEX_COLUMN]},
        new int[] {-relativePositions[2][INDEX_ROW], relativePositions[2][INDEX_COLUMN]},
        new int[] {-relativePositions[3][INDEX_ROW], relativePositions[3][INDEX_COLUMN]}};
    int[][] newPositions = getBlocks(rotated);
    if (stage.canPlaceBlocks(newPositions)) {
      relativePositions = rotated;
    }
  }

  public void moveToLeft(Stage stage) {
    int[][] blocks = getBlocks();
    for (int[] block: blocks) {
      block[INDEX_COLUMN]--;
    }
    if (stage.canPlaceBlocks(blocks)) {
      originColumn--;
    }
  }

  public void moveToRight(Stage stage) {
    int[][] blocks = getBlocks();
    for (int[] block: blocks) {
      block[INDEX_COLUMN]++;
    }
    if (stage.canPlaceBlocks(blocks)) {
      originColumn++;
    }
  }

  public void render(ShapeRenderer renderer) {
    renderer.setColor(Color.GREEN);
    for (int[] block: getBlocks()) {
      Tetris.renderBlock(renderer, block[INDEX_COLUMN], block[INDEX_ROW]);
    }
  }

  private int[][] getBlocks(int[][] relativePositions) {
    return new int[][] {
        new int[] {originColumn + relativePositions[0][INDEX_COLUMN], originRow + relativePositions[0][INDEX_ROW]},
        new int[] {originColumn + relativePositions[1][INDEX_COLUMN], originRow + relativePositions[1][INDEX_ROW]},
        new int[] {originColumn + relativePositions[2][INDEX_COLUMN], originRow + relativePositions[2][INDEX_ROW]},
        new int[] {originColumn + relativePositions[3][INDEX_COLUMN], originRow + relativePositions[3][INDEX_ROW]}
    };
  }
}
