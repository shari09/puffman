package world;


import java.io.*;

import maps.MapFactory;
import util.*;

/**
 * [MenuFactory.java]
 * Static methods that creates different menus.
 * 
 * 2020-01-17
 * @version 0.0.2
 * @author Shari Sun
 */
public class MenuFactory {

  /**
   * Get the start menu.
   * It has a play, instruction, and quit button.
   * @return Menu, the start menu.
   * @throws IOException
   */
  public static Menu getStartMenu() throws IOException {
    Menu menu = new Menu(3, "background/startMenu.png");
    int width = 800;
    menu.setButton(0,
      GameWindow.WIDTH/2-Util.scaleX(width/2), Util.scaleY(550),
      Util.scaleX(width), Util.scaleY(80), "Play");
    menu.setButton(1,
      GameWindow.WIDTH/2-Util.scaleX(width/2), Util.scaleY(700),
      Util.scaleX(width), Util.scaleY(80), "Instruction");
    menu.setButton(2,
      GameWindow.WIDTH/2-Util.scaleX(width/2), Util.scaleY(850),
      Util.scaleX(width), Util.scaleY(80), "Quit");
    return menu;
  }

  /**
   * Get the game over menu.
   * It has three buttons, the rematch, main menu, and quit button.
   * @return Menu, the game over menu.
   * @throws IOException
   */
  public static Menu getGameOverMenu() throws IOException {
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

  /**
   * Get the choose map menu.
   * It has multiple buttons for map selection and a back button.
   * @return Menu, choose map menu.
   * @throws IOException
   */
  public static Menu getChooseMapMenu() throws IOException {
    int numButtons = 5;
    int numRows = 2;
    int width = Util.scaleX(300);
    int height = Util.scaleY(300);
    int gapX = Util.scaleX(100);
    int gapY = Util.scaleY(50);

    //some math to calculate offsets for centering the buttons
    int offsetX = (int)
      (GameWindow.WIDTH-Math.ceil(numButtons/(double)numRows)*(width+gapX))/2;
    int offsetY = (GameWindow.HEIGHT-numRows*(height+gapY))/2;
    
    Menu menu = new Menu(numButtons+1, "background/chooseMapMenu.jpg");
    menu.setButton(0, 
      offsetX, offsetY, width, height, 
      MapFactory.ARENA);
    menu.setButton(1, 
      offsetX+width+gapX, offsetY, width, height, 
      MapFactory.TREE);
    menu.setButton(2, 
      offsetX+(width+gapX)*2, offsetY, width, height, 
      MapFactory.BLUE_MAMMOTH);
    menu.setButton(3, 
      offsetX+width/2, offsetY+height+gapY, width, height, 
      MapFactory.NIGHT);
    menu.setButton(4, 
      (int)(offsetX+width*1.7+gapX), offsetY+height+gapY, width, height,
      MapFactory.MIAMI_DOME);
    menu.setButton(5, 
      Util.scaleX(1500), Util.scaleY(800), 
      Util.scaleX(300), Util.scaleY(100), "Back");
    return menu;
  }

  /**
   * Get the instruction menu.
   * It simply displays the instructions and has a back button.
   * @return Menu, the instructions menu.
   * @throws IOException
   */
  public static Menu getInstructionMenu() throws IOException {
    Menu menu = new Menu(1, "background/instruction.png");
    menu.setButton(0,
      Util.scaleX(1600), Util.scaleY(900),
      Util.scaleX(200), Util.scaleY(80), "Back");
    return menu;
  }
}