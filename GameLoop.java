import java.io.IOException;

import world.*;
import java.awt.event.*;
import java.util.Scanner;
import util.Timer;

public class GameLoop {
  public static void main(String[] args) throws IOException {
    int fps = 60;
    int timePerFrame = 1000/fps;
    boolean quit = false;
    Scanner input = new Scanner(System.in);

    World world = new World();
    GameWindow window = new GameWindow(world);
    while (!quit) {
      Timer.reset();
      while (world.isRunning()) {
        Timer.update();
        window.update();
        //decreases cpu intensity
        try {
          Thread.sleep(timePerFrame);
        } catch (Exception e) {
          System.err.println(e);
        }
      }
      System.out.print("Play again: ");
      String answer = input.nextLine();
      if (answer.charAt(0) != 'y') {
        quit = true;
      } else {
        world.reset();
      }
    }
    input.close();
    
    window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
  }
}