package world;

import javax.swing.*;
import java.awt.*;
import java.util.*;
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

  private HashSet<Item> damagables = new HashSet<>();
  private HashSet<Hero> attackingPlayers = new HashSet<>();
  private HashSet<Item> pickUpableItems = new HashSet<>();
  private Hero[] players;
  private Block[] blocks;
  private boolean running = true;


  //key listeners doesn't work all the time
  //need constant update on keys
  private HashSet<String> activeHeldKeys = new HashSet<>();


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

    this.players[1] = new Ash(
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
    this.blocks = new Block[3];
    this.blocks[0] = new RectBlock(
      "images/blocks/test.jpg", 
      Util.scaleX(400), Util.scaleY(700), Util.scaleX(800), Util.scaleY(50)
    );
    this.blocks[1] = new RectBlock(
      "images/blocks/test.jpg", 
      Util.scaleX(800), Util.scaleY(300), Util.scaleX(100), Util.scaleY(500)
    );
    this.blocks[2] = new RectBlock(
      "images/blocks/test.jpg", 
      Util.scaleX(1400), Util.scaleY(500), Util.scaleX(400), Util.scaleY(40)
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
   * for every block in the game: 
   * Checks whether the player collides with the block
   * moves the player back until it no longer collides
   * also sets the movement state for the player
   * @param curPlayer the player that's checked for collision
   * @param curBlock the block that's being checked for collision
   */
  private void updateBlockCollisions(Hero curPlayer) {

    boolean inAir = true;

    for (int i = 0; i < this.blocks.length; i++) {
      Block curBlock = this.blocks[i];

      if (Collision.isCollided((RectCollidable)curPlayer, 
                               (RectCollidable)curBlock)) {

        double xVel = curPlayer.getXVel();
        double yVel = curPlayer.getYVel();

        //for push back calculations
        boolean biggerX = false;
        if (Math.abs(xVel) > Math.abs(yVel)) {
          biggerX = true;
        }

        //move the player back until they no longer collide
        while (Collision.isCollided((RectCollidable)curPlayer, 
                                    (RectCollidable)curBlock)) {
          if (biggerX) {
            curPlayer.moveX(-xVel/Math.abs(xVel));
            curPlayer.moveY(-yVel/Math.abs(xVel));
          } else {
            curPlayer.moveX(-xVel/Math.abs(yVel));
            curPlayer.moveY(-yVel/Math.abs(yVel));
          } 
        }
      }
      inAir = !setMovementState(curPlayer, curBlock) && inAir;

    }

    if (inAir) {
      curPlayer.setState("inAir");
    }
    
  }

  /**
   * Check to see what the state the player is in
   * in terms of movement (ex, mid-air, at a wall, on the ground)
   * there
   * @param curPlayer the player being checked
   * @param curBlock the block being checked
   * @return true if it's not in mid-air
   */
  private boolean setMovementState(Hero curPlayer, Block curBlock) {
  //reset values if the sides are touching
    // technically collision but not overlapping
    // sets the state of the player
    String side = Collision.getTouchingSide((RectCollidable)curPlayer, 
                                            (RectCollidable)curBlock);

    if (side.equals("left")) {
      curPlayer.setState("onLeftWall");
    }
    if (side.equals("right")) {
      curPlayer.setState("onRightWall");
    }
    
    if (side.equals("left") || side.equals("right")) {
      curPlayer.resetXMovement();
      if (curPlayer.getYVel() < 0) {
        curPlayer.resetYVel();
      }
    }

    if (side.equals("top") || side.equals("bottom")) {
      curPlayer.resetYVel();
    }

    //move back the gravity if it's on the floor
    if (side.equals("bottom")) {
      curPlayer.setState("onGround");
      curPlayer.moveY(-World.GRAVITY);
    }

    if (!side.equals("none") && !side.equals("top")) {
      curPlayer.resetJumps();
      return true;
    } else {
      return false;
    }
  }

  private void updateAttackCollisions(Hero curPlayer) {
    if (curPlayer.isAttackState()) {
      for (int i = 0; i < this.players.length; i++) {
        Hero targetPlayer = this.players[i];
        if (targetPlayer != curPlayer
            && Collision.isCollided(
              (CircleCollidable)(curPlayer.getAttackHitbox()),
              //casting player to circle to get hitbox
              //hmmmm kinda sketchy heheh
              (CircleCollidable)targetPlayer)
            ) {
          targetPlayer.takeDamage(curPlayer.getDir(), 
                                  curPlayer.getPower());
          System.out.println(targetPlayer.getDamageTaken());
        }

      }
    }
  }

  /**
   * handle player related updates (ex. movements, collisions)
   */
  private void updatePlayers() {

    //for each player
    for (int i = 0; i < this.players.length; i++) {
      Hero curPlayer = this.players[i];
      curPlayer.updateMovement();
      curPlayer.updateSprite();
      curPlayer.updateStateFunction();

      updateBlockCollisions(curPlayer);
      updateAttackCollisions(curPlayer);

      if (curPlayer.isDead()) {
        this.running = false;
        return;
      }

    }
  }

  /**
   * update the held-key controls for the players
   */
  private void updateControls() {
    Iterator<String> itr = this.activeHeldKeys.iterator();

    while (itr.hasNext()) {
      String key = itr.next();
      if (!this.players[0].inSpecialState()) {
        if (key.equals("D")) {
          this.players[0].moveRight();
        } else if (key.equals("A")) {
          this.players[0].moveLeft();
        } else if (key.equals("S")) {
          this.players[0].dropDown();
        } 
      }
      
      if (!this.players[1].inSpecialState()) {
        if (key.equals("Right")) {
          this.players[1].moveRight();
        } else if (key.equals("Left")) {
          this.players[1].moveLeft();
        } else if (key.equals("Down")) {
          this.players[1].dropDown();
        }
      }
      
    }
  }

  /**
   * updates the world
   */
  public void update() {
    this.updateControls();
    this.updatePlayers();
    
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
   * @param g2d the Graphics2D to draw on
   */
  private void displayAll(Graphics2D g2d) {
    for (int i = 0; i < this.blocks.length; i++) {
      this.blocks[i].display(this, g2d);
    }
    for (int i = 0; i < this.players.length; i++) {
      this.players[i].display(this, g2d);
      this.players[i].displayHitbox(g2d);
      if (this.players[i].isAttackState()) {
        this.players[i].displayAttackHitbox(g2d);
      }
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
   * Add a damagable object to the damagable list
   * @param obj Item, the object to add
   */
  public void addDamagable(Item obj) {
    this.damagables.add(obj);
  }


  /**
   * get the players
   * @return Hero[], the players
   */
  public Hero[] getPlayers() {
    return this.players;
  }

  /**
   * @return boolean, whether the game is running or not
   */
  public boolean isRunning() {
    return this.running;
  }

  /**
   * add an active key to the active key set
   * @param key the key to add
   */
  public void addActiveHeldKey(String key) {
    this.activeHeldKeys.add(key);
  }

  /**
   * remove an active key from the active key set
   * @param key the key to remove
   */
  public void removeActiveHeldKey(String key) {
    this.activeHeldKeys.remove(key);

    //if the player is in a special state,
    //no keys should work
    if (!this.players[0].inSpecialState() 
        && (key.equals("D") || key.equals("A"))) {
      this.players[0].resetXMovement();
    }
    if (!this.players[1].inSpecialState() 
        && (key.equals("Left") || key.equals("Right"))) {
      this.players[1].resetXMovement();
    }
  }

}