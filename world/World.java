package world;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

import items.*;
import characters.*;
import blocks.*;

public class World extends JPanel implements KeyListener {
  public static final long serialVersionUID = 1L;
  
  private static final int GRAVITY = 8;
  private int screenMargin = 30;
  private BufferedImage background;

  private ArrayList<Item> allItems;
  private Hero[] players;
  private Block[] blocks;

  public World() throws IOException {
    this.addKeyListener(this);
    this.setFocusable(true);
    this.requestFocusInWindow();
    this.background = ImageIO.read(
      new File("images/background/background.jpg")
    );
    this.players = new Hero[2];
    // this.players[0] = new Ash();
    this.blocks = new Block[1];
    this.blocks[0] = new RectBlock(
      "images/blocks/test.jpg", 400, 700, 800, 50);
  };

  /**
   * gets the gravity constant of the world
   * @return World.GRAVITY int, the world's gravity
   */
  public int getGravity() {
    return World.GRAVITY;
  }

  //key listeners
  public void keyPressed(KeyEvent e) {
    String key = KeyEvent.getKeyText(e.getKeyCode());
  }

  public void keyTyped(KeyEvent e) {

  }

  public void keyReleased(KeyEvent e) {
    
  }


  public void updateGame() {

  }

  
  /**
   * draws the background
   * @param g2d Graphics2D, the graphics control of this component
   */
  private void drawBackground(Graphics2D g2d) {
    g2d.drawImage(this.background, 
                  0, 0, 
                  this.getParent().getWidth(), 
                  this.getParent().getHeight(), 
                  this);
  }

  private void displayAllBlocks(Graphics2D g2d) {
    for (int i = 0; i < this.blocks.length; i++) {
      this.blocks[i].display(this, g2d);
    }
  }

  @Override
  /**
   * displays everything on the screen
   */
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    this.drawBackground(g2d);
    this.displayAllBlocks(g2d);
  }

}