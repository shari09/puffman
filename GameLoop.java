import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.*;

import util.Timer;
import world.GameWindow;
import world.*;
import world.Menu;

public class GameLoop {

  //decreases cpu intensity
  private static void timeOut() {
    //decreases cpu intensity
    int fps = 60;
    int timePerFrame = 1000/fps;
    try {
      Thread.sleep(timePerFrame);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  
  public static void main(String[] args) throws IOException {
    
    boolean quit = false;

    World world = null;
    Menu startMenu = MenuFactory.getStartMenu();
    Menu choosemapMenu = MenuFactory.getChooseMapMenu();
    Menu instructionMenu = MenuFactory.getInstructionMenu();
    Menu gameOverMenu = MenuFactory.getGameOverMenu();
    GameWindow window = new GameWindow(startMenu);
    JPanel curPanel = window.getCurPanel();


    while (!quit) {
      String action = "";
      if (curPanel instanceof Menu) {
        action = ((Menu)(curPanel)).getAction();
      }

      //start menu
      if (curPanel == startMenu) {
        if (action.equals("Play")) {
          window.switchPanel(choosemapMenu);
        } else if (action.equals("Instruction")) {
          window.switchPanel(instructionMenu);
        } else if (action.equals("Quit")) {
          quit = true;
        }

        //map selection
      } else if (curPanel == choosemapMenu) {
        if (!action.equals("")) {
          world = new World(action);
          window.switchPanel(world);
        }

        //world main loop
      } else if (curPanel == world) {
        if (world.isRunning()) {
          Timer.update();
        } else {
          window.switchPanel(gameOverMenu);
          Timer.reset();
        }

        //game over menu
      } else if (curPanel == gameOverMenu) {
        if (action.equals("Rematch")) {
          world.reset();
          window.switchPanel(world);
        } else if (action.equals("Main Menu")) {
          window.switchPanel(startMenu);
        } else if (action.equals("Quit")) {
          quit = true;
        }
      }

      window.update();
      curPanel = window.getCurPanel();
      timeOut();
      
    }
    
    window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
  }
}