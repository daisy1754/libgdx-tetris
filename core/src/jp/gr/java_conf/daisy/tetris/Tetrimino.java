package jp.gr.java_conf.daisy.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

import static jp.gr.java_conf.daisy.tetris.Constants.INDEX_COLUMN;
import static jp.gr.java_conf.daisy.tetris.Constants.INDEX_ROW;
import static jp.gr.java_conf.daisy.tetris.Stage.NUM_COLUMNS;
import static jp.gr.java_conf.daisy.tetris.Stage.NUM_ROWS;

/**
 * Group of four square that falling together.
 */
public enum Tetrimino {
      // xx
      // xo
      SQUARE(new int[][] {new int[]{-1, 0}, new int[]{0, 0}, new int[]{0, 1}, new int[]{-1, 1}}, new float[] {0.f, -1f}),
      //  x
      // xox
      MOUNTAIN(new int[][] {new int[]{-1, 0}, new int[]{0, 0}, new int[]{1, 0}, new int[]{0, 1}}, new float[] {-0.5f, -1f}),
      // x
      // xox
      MIRROR_L(new int[][] {new int[]{-1, 0}, new int[]{0, 0}, new int[]{1, 0}, new int[]{-1, 1}}, new float[] {-0.5f, -1f}),
      //   x
      // xox
      L(new int[][] {new int[]{-1, 0}, new int[]{0, 0}, new int[]{1, 0}, new int[]{1, 1}}, new float[] {-0.5f, -1f}),
      // xx
      //  ox
      Z(new int[][] {new int[]{1, 0}, new int[]{0, 0}, new int[]{0, 1}, new int[]{-1, 1}}, new float[] {-0.5f, -1f}),
      //  xx
      // xo
      S(new int[][] {new int[]{-1, 0}, new int[]{0, 0}, new int[]{0, 1}, new int[]{1, 1}}, new float[] {-0.5f, -1f}),
      // xoxx
      BAR(new int[][] {new int[]{-1, 0}, new int[]{0, 0}, new int[]{1, 0}, new int[]{2, 0}}, new float[] {-1f, -0.5f});

  private static final Tetrimino[] VALUES = values();
  private static Random random = new Random();

  // Position of "origin" of this tetrimino
  private int originColumn;
  private int originRow;
  // Position of blocks relative to origin of this block
  private int[][] relativePositions;
  private final int[][] relativePositionsOriginal;
  private final float[] originDelta;

  public static Tetrimino getInstance() {
    Tetrimino tetrimino = VALUES[random.nextInt(VALUES.length)];
    tetrimino.originColumn = NUM_COLUMNS / 2;
    tetrimino.originRow = NUM_ROWS - 1;
    return tetrimino;
  }

  Tetrimino(int[][] relativePositions, float[] originDelta) {
    this.relativePositionsOriginal = relativePositions;
    this.relativePositions = relativePositions;
    this.originDelta = originDelta;
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
    if (this == SQUARE) {
      return;
    }
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

  public void render(ShapeRenderer renderer, int startX, int startY, int boxSize) {
    renderer.setColor(Color.GREEN);
    int originX = startX + boxSize * 2;
    int originY = startY + boxSize * 2;
    for (int[] block: relativePositionsOriginal) {
      renderer.rect(originX + (block[INDEX_COLUMN] + originDelta[INDEX_COLUMN]) * boxSize,
          originY + (block[INDEX_ROW] + originDelta[INDEX_ROW]) * boxSize,
          boxSize,
          boxSize);
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
