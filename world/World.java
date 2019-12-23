package world;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.*;
import java.io.*;

import items.*;
import characters.*;
import blocks.*;
import util.*;

public class World extends JPanel {
  public static final long serialVersionUID = 1L;
  
  public static final int GRAVITY = 8;
  public static final int screenMargin = 30;
  
  private BufferedImage background;

  private ArrayList<Item> activeItems; //picked up, do I need this tho??
  private ArrayList<Item> inactiveItems;
  private Hero[] players;
  private Block[] blocks;

  public World() throws IOException {
    this.addKeyListener(new Controls(this));
    this.setFocusable(true);
    this.requestFocusInWindow();
    this.setPreferredSize(GameWindow.screenSize);
    this.background = Util.urlToImage("images/background/background.jpg");
    this.players = new Hero[1];

    int scaledPlayerSize = Math.min(Util.scaleX(100), Util.scaleY(100));
    this.players[0] = new Ash(
      Util.scaleX(200),
      Util.scaleY(500),
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize/2);
    this.blocks = new Block[1];
    this.blocks[0] = new RectBlock(
      "images/blocks/test.jpg", 
      Util.scaleX(400), Util.scaleY(700), Util.scaleX(800), Util.scaleY(50)
    );
  };


  public Hero[] getPlayers() {
    return this.players;
  }


  /**
   * necessary updates for players (ex. movement)
   */
  public void update() {

    //collision checking (player based)


    for (int i = 0; i < this.players.length; i++) {
      this.players[i].updateMovement();
    }
  }

  
  /**
   * draws the background
   * @param g2d Graphics2D, the graphics control of this component
   */
  private void drawBackground(Graphics2D g2d) {
    g2d.drawImage(this.background, 
                  0, 0,
                  GameWindow.width,
                  GameWindow.height, 
                  this);
  }

  private void displayAll(Graphics2D g2d) {
    for (int i = 0; i < this.blocks.length; i++) {
      this.blocks[i].display(this, g2d);
    }
    for (int i = 0; i < this.players.length; i++) {
      this.players[i].display(this, g2d);
      this.players[i].displayHitbox(g2d);
    }
  }


  @Override
  /**
   * displays everything on the screen
   */
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    this.drawBackground(g2d);
    this.displayAll(g2d);
  }

}