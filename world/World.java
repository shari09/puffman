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
  
  public static final double GRAVITY = Util.scaleY(0.5);
  public static final int screenMargin = 30;
  
  private BufferedImage background;

  private ArrayList<Item> activeItems; //picked up, do I need this tho??
  private ArrayList<Item> inactiveItems;
  private Hero[] players;
  private Block[] blocks;
  private boolean running = true;


  /**
   * add the players to the world
   * @throws IOException ImageIO
   */
  private void addPlayers() throws IOException {
    this.players = new Hero[2];
    int scaledPlayerSize = Math.min(Util.scaleX(40), Util.scaleY(40));
    this.players[0] = new Ash(
      Util.scaleX(500),
      Util.scaleY(500),
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize/2);

    this.players[1] = new Remi(
      Util.scaleX(1100),
      Util.scaleY(500),
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize/2);
  }
  
  /**
   * add the necessary blocks to the world
   * @throws IOException ImageIO
   */
  private void addBlocks() throws IOException {
    this.blocks = new Block[2];
    this.blocks[0] = new RectBlock(
      "images/blocks/test.jpg", 
      Util.scaleX(400), Util.scaleY(700), Util.scaleX(800), Util.scaleY(50)
    );
    this.blocks[1] = new RectBlock(
      "images/blocks/test.jpg", 
      Util.scaleX(800), Util.scaleY(300), Util.scaleX(100), Util.scaleY(500)
    );
  }

  /**
   * constructor
   * @throws IOException ImageIO
   */
  public World() throws IOException {
    this.addKeyListener(new Controls(this));
    this.setFocusable(true);
    this.requestFocusInWindow();
    this.setPreferredSize(GameWindow.screenSize);
    this.background = Util.urlToImage("images/background/background.jpg");

    this.addPlayers();
    this.addBlocks();
  };

  /**
   * handle player related updates (ex. movements, collisions)
   */
  private void handlePlayers() {
    for (int i = 0; i < this.players.length; i++) {
      this.players[i].updateMovement();

      if (this.players[i].isDead()) {
        this.running = false;
        return;
      }

      //collision with blocks
      for (int j = 0; j < this.blocks.length; j++) {

        if (Collision.isCollided((RectCollidable)(this.players[i]), 
                                 (RectCollidable)(this.blocks[j]))) {
          
          double xVel = this.players[i].getXVel();
          double yVel = this.players[i].getYVel();
          
          //for push back calculations
          boolean biggerX = false;
          if (Math.abs(xVel) > Math.abs(yVel)) {
            biggerX = true;
          }

          //move the player back until they no longer collide
          while (Collision.isCollided(
                 (RectCollidable)(this.players[i]), 
                 (RectCollidable)(this.blocks[j]))) {
            if (biggerX) {
              this.players[i].moveX(-xVel/Math.abs(xVel));
              this.players[i].moveY(-yVel/Math.abs(xVel));
            } else {
              this.players[i].moveX(-xVel/Math.abs(yVel));
              this.players[i].moveY(-yVel/Math.abs(yVel));
            } 
          }
        }

        //reset values if the sides are touching
        // technically collision but not overlapping
        // sets the state of the player
        String side = Collision.getTouchingSide(
          (RectCollidable)(this.players[i]), 
          (RectCollidable)(this.blocks[j]));

        if (side.equals("left")) {
          this.players[i].resetXMovement();
          this.players[i].setState("onLeftWall");
        }
        if (side.equals("right")) {
          this.players[i].resetXMovement();
          this.players[i].setState("onRightWall");
        }

        if (side.equals("bottom") || side.equals("top")) {
          this.players[i].resetYVel();
        }

        //move back the gravity if it's on the floor
        if (side.equals("bottom")) {
          this.players[i].setState("onGround");
          this.players[i].moveY(-World.GRAVITY);
        }

        if (side.equals("none")) {
          this.players[i].setState("inAir");
        }

        if (!side.equals("none")) {
          this.players[i].resetJumps();
        }
      }

    }
  }

  /**
   * necessary updates for players (ex. movement)
   */
  public void update() {
    handlePlayers();
    
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

  /**
   * display everything on the screen
   * @param g2d
   */
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
   * actually drawing everything on the screen
   */
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    this.drawBackground(g2d);
    this.displayAll(g2d);
    Toolkit.getDefaultToolkit().sync();
  }


  //getters and setters
  /////////////////////////

  /**
   * get the players
   * @return Hero[], the players
   */
  public Hero[] getPlayers() {
    return this.players;
  }

  public boolean isRunning() {
    return this.running;
  }

}