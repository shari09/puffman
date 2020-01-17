package world;


import java.io.*;

import maps.MapFactory;
import util.*;

public class MenuFactory {
  public static Menu getStartMenu() throws IOException {
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

  public static Menu getChooseMapMenu() throws IOException {
    int numButtons = 5;
    int numRows = 2;
    int width = Util.scaleX(300);
    int height = Util.scaleY(300);
    int gapX = Util.scaleX(100);
    int gapY = Util.scaleY(50);
    int offsetX = (int)
      (GameWindow.width-Math.ceil(numButtons/(double)numRows)*(width+gapX))/2;
    int offsetY = (GameWindow.height-numRows*(height+gapY))/2;
    
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

  public static Menu getInstructionMenu() throws IOException {
    Menu menu = new Menu(1, "background/chooseMapMenu.jpg");
    menu.setButton(0,
      Util.scaleX(1600), Util.scaleY(800),
      Util.scaleX(200), Util.scaleY(80), "Back");
    return menu;
  }
}