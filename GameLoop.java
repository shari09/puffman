import java.io.IOException;


import world.*;

public class GameLoop {
  public static void main(String[] args) throws IOException {
    int fps = 60;
    int timePerFrame = 1000/fps;

    World world = new World();
    GameWindow window = new GameWindow(world);

    // long lastTime = System.currentTimeMillis();
    // long elapsedTime = 0;

    while (true) {
      // elapsedTime = System.currentTimeMillis() - lastTime;
      // if (elapsedTime >= timePerFrame) {
      //   elapsedTime = 0;
      //   lastTime = System.currentTimeMillis();
      //   window.update(world);
      // }
      window.update(world);
      //decreases cpu intensity
      try {Thread.sleep(timePerFrame);} catch (Exception e) {}

    }
  }
}