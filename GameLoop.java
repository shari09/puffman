import java.io.IOException;


import world.*;
import java.awt.event.*;
import util.Timer;

public class GameLoop {
  public static void main(String[] args) throws IOException {
    int fps = 60;
    int timePerFrame = 1000/fps;

    World world = new World();
    GameWindow window = new GameWindow(world);

    while (world.isRunning()) {
      Timer.update();
      window.update(world);
      //decreases cpu intensity
      try {
        Thread.sleep(timePerFrame);
      } catch (Exception e) {
        System.err.println(e);
      }

    }
    window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
  }
}