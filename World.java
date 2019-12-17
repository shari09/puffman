import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

import items.*;
import characters.*;
import blocks.*;


public class World extends JFrame {
  private int gravity = 8;
  private int margin = 30;

  public World() {
    this.setSize(1000, 800);
    this.setLayout(null);
    this.setVisible(true);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setFocusable(true);
  }

  ArrayList<Item> allItems;
  PlayableCharacter[] players = new PlayableCharacter[2];
  Block[] blocks = new Block[7];

  /**
   * gets the gravity constant of this world
   * @return int, the world's gravity
   */
  public int getGravity() {
    return this.gravity;
  }
  
  

}