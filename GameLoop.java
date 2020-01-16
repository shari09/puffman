import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Scanner;

import util.Timer;
import util.Util;
import world.GameWindow;
import world.World;
import world.Menu;

public class GameLoop {

  private static Menu getStartMenu() throws IOException {
    Menu menu = new Menu(3, "background/startMenu.png");
    int width = 800;
    menu.setButton(0,
      GameWindow.width/2-Util.scaleX(width/2), Util.scaleY(550),
      Util.scaleX(width), Util.scaleY(80), "Play");
    menu.setButton(1,
      GameWindow.width/2-Util.scaleX(width/2), Util.scaleY(700),
      Util.scaleX(width), Util.scaleY(80), "Instruction");
    menu.setButton(2,
      GameWindow.width/2-Util.scaleX(width/2), Util.scaleY(850),
      Util.scaleX(width), Util.scaleY(80), "Quit");
    return menu;
  }

  private static Menu getGameOverMenu() throws IOException {
    Menu menu = new Menu(3, "background/gameOver.jpg");
    menu.setButton(0,
      Util.scaleX(400), Util.scaleY(800),
      Util.scaleX(300), Util.scaleY(80), "Rematch");
    menu.setButton(1,
      Util.scaleX(800), Util.scaleY(800),
      Util.scaleX(300), Util.scaleY(80), "Main Menu");
    menu.setButton(2,
      Util.scaleX(1200), Util.scaleY(800),
      Util.scaleX(300), Util.scaleY(80), "Quit");
    return menu;
  }

  private static Menu getChooseMapMenu() throws IOException {
    Menu menu = new Menu(3, "background/chooseMapMenu.jpg");
    menu.setButton(0,
      Util.scaleX(450), Util.scaleY(300),
      Util.scaleX(300), Util.scaleY(300), "Arena");
    menu.setButton(1,
      Util.scaleX(850), Util.scaleY(300),
      Util.scaleX(300), Util.scaleY(300), "Tree");
    menu.setButton(2,
      Util.scaleX(1250), Util.scaleY(300),
      Util.scaleX(300), Util.scaleY(300), "Miami");
    return menu;
  }

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

    World world;
    Menu startMenu = getStartMenu();
    Menu choosemapMenu = getChooseMapMenu();
    Menu gameOverMenu = getGameOverMenu();
    GameWindow window = new GameWindow(startMenu);

    while (startMenu.getAction() == null) {
      window.update();
      timeOut();
    }
    if (startMenu.getAction().equals("Quit")) {
      quit = true;
    } else if (startMenu.getAction().equals("Play")) {
      window.switchPanel(choosemapMenu);
    }

    while (choosemapMenu.getAction() == null) {
      window.update();
      timeOut();
    }

    world = new World(choosemapMenu.getAction());
    window.switchPanel(world);

    while (!quit) {  
      Timer.reset();
      world.requestFocus();
      while (world.isRunning()) {
        Timer.update();
        window.update();
        timeOut();
      }

      //game over screen
      window.switchPanel(gameOverMenu);
      while (gameOverMenu.getAction() == null) {
        window.update();
        timeOut();
      }
      if (gameOverMenu.getAction().equalsIgnoreCase("rematch")) {
        world.reset();
        window.switchPanel(world);
        
      } else {
        quit = true;
      }
      gameOverMenu = getGameOverMenu();
      
    }
    input.close();
    
    window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
  }
}