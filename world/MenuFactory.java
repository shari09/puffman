package world;


import java.io.*;
import util.*;

public class MenuFactory {
  public static Menu getStartMenu() throws IOException {
    Menu menu = new Menu(3, "background/startMenu.jpg");
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
    Menu menu = new Menu(4, "background/chooseMapMenu.jpg");
    menu.setButton(0,
      Util.scaleX(450), Util.scaleY(300),
      Util.scaleX(300), Util.scaleY(300), "Arena");
    menu.setButton(1,
      Util.scaleX(850), Util.scaleY(300),
      Util.scaleX(300), Util.scaleY(300), "Tree");
    menu.setButton(2,
      Util.scaleX(1250), Util.scaleY(300),
      Util.scaleX(300), Util.scaleY(300), "Miami");
    menu.setButton(3,
      Util.scaleX(1600), Util.scaleY(900),
      Util.scaleX(200), Util.scaleY(80), "Back");
    return menu;
  }

  public static Menu getInstructionMenu() throws IOException {
    Menu menu = new Menu(1, "background/instruction.png");
    menu.setButton(0,
      Util.scaleX(1600), Util.scaleY(900),
      Util.scaleX(200), Util.scaleY(80), "Back");
    return menu;
  }
}