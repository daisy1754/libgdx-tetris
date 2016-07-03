package jp.gr.java_conf.daisy.tetris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import jp.gr.java_conf.daisy.tetris.Constants;
import jp.gr.java_conf.daisy.tetris.Tetris;

public class DesktopLauncher {
  public static void main(String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.title = "Drop - libgdx tutorial";
    config.width = Constants.STAGE_WIDTH;
    config.height = Constants.STAGE_HEIGHT;
    new LwjglApplication(new Tetris(), config);
  }
}
