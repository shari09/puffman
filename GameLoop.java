import java.io.IOException;

import world.*;
import java.awt.event.*;
import java.util.Scanner;
import util.Timer;

public class GameLoop {

  //decreases cpu intensity
  private static void timeOut() {
    //decreases cpu intensity
    int fps = 60;
    int timePerFrame = 1000/fps;
    try {
      Thread.sleep(timePerFrame);
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  
  public static void main(String[] args) throws IOException {
    
    boolean quit = false;
    Scanner input = new Scanner(System.in);

    World world = new World();
    GameOverScreen gameOverScreen = new GameOverScreen();
    GameWindow window = new GameWindow(world);

    while (!quit) {
      Timer.reset();
      world.requestFocus();
      while (world.isRunning()) {
        Timer.update();
        window.update();
        timeOut();
      }

      //game over screen
      window.switchPanel(gameOverScreen);
      while (gameOverScreen.getAction() == null) {
        window.update();
        timeOut();
      }
      if (gameOverScreen.getAction().equalsIgnoreCase("rematch")) {
        world = new World();
        window.switchPanel(world);
        
      } else {
        quit = true;
      }
      gameOverScreen = new GameOverScreen();
      
    }
    input.close();
    
    window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
  }
}