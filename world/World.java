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

  public static final int mapWidth = Util.scaleX(4000);
  public static final int mapHeight = Util.scaleY(2400);

  public static final int screenMarginX = Util.scaleX(150);
  public static final int screenMarginY = Util.scaleY(150);

  public static final double GRAVITY = Util.scaleY(0.25);
  
  private BufferedImage background;

  private HashSet<Item> damagableItems = new HashSet<>();
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
    int scaledPlayerSize = Math.min(Util.scaleX(80), Util.scaleY(80));
    this.players[0] = new Ash(
      World.mapWidth/2 - Util.scaleX(300),
      (int)(World.mapHeight/2.5),
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize/2,
      "A", "D", "Space", "S", "J", "K");
    this.players[1] = new AshCopy(
      World.mapWidth/2 + Util.scaleX(300),
      (int)(World.mapHeight/2.5),
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize/2,
      "Left", "Right", "Up", "Down", "Period", "Slash");
  }

  // public void reset() throws IOException {
  //   this.addPlayers();
  //   this.running = true;
  // }
  
  /**
   * add the necessary blocks to the world
   * @throws IOException ImageIO
   */
  private void addBlocks() throws IOException {
    this.blocks = new Block[4];
    this.blocks[0] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 - Util.scaleX(500),
      World.mapHeight/2,
      Util.scaleX(1000), Util.scaleY(50)
    );
    this.blocks[1] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 - Util.scaleX(700),
      World.mapHeight/2 - Util.scaleY(400),
      Util.scaleX(50), Util.scaleY(400)
    );
    this.blocks[2] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 - Util.scaleX(200),
      World.mapHeight/2 - Util.scaleY(300),
      Util.scaleX(400), Util.scaleY(40)
    );
    this.blocks[3] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 - Util.scaleX(900),
      World.mapHeight/2 - Util.scaleY(400),
      Util.scaleX(220), Util.scaleY(50)
    );
  }

  /**
   * constructor
   * @throws IOException ImageIO
   */
  public World() throws IOException {
    
    this.setFocusable(true);
    this.requestFocusInWindow();
    this.addKeyListener(new Controls(this));
    this.setPreferredSize(GameWindow.screenSize);
    this.background = Util.urlToImage("background/background.jpg");

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

  // this method is
  // reaaaallllyyyyy sketchy
  // the return doesnt make sense other than that it's convenient
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

  /**
   * checks whether the player's attack is hitting another player
   * if it is, the other player takes knockback
   * @param curPlayer the current player being checked for
   */
  private void updateAttackCollisions(Hero curPlayer) {
    if (curPlayer.isActiveAttackState()) {
      for (int i = 0; i < this.players.length; i++) { //all other players
        Hero targetPlayer = this.players[i];

        if (targetPlayer != curPlayer
            && targetPlayer.damagable()) {
          //for all the hurtboxes
          
          for (int j = 0; j < curPlayer.getNumHurtboxes(); j++) {
            if (Collision.isCollided(
                (CircleCollidable)(curPlayer.getHurtboxes()[j]),
                (CircleCollidable)targetPlayer)) {
              //collided
              curPlayer.damage(targetPlayer);
              // System.out.println("Player: " 
              //                 + targetPlayer.getX() + " " 
              //                 + targetPlayer.getY() + " "
              //                 + targetPlayer.getRadius());
              // System.out.println("Hurtbox: " 
              //                 + curPlayer.getHurtboxes()[j].getX() + " "
              //                 + curPlayer.getHurtboxes()[j].getY() + " "
              //                 + curPlayer.getHurtboxes()[j].getRadius());
              System.out.println(targetPlayer.getDamageTaken());
            }
          }
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

      for (int i = 0; i < this.players.length; i++) {
        Hero curPlayer = this.players[i];
        if (!curPlayer.inSpecialState()) {
          if (key.equals(curPlayer.getLeftKey())) {
            curPlayer.moveLeft();
          } else if (key.equals(curPlayer.getRightKey())) {
            curPlayer.moveRight();
          } else if (key.equals(curPlayer.getDropKey())) {
            curPlayer.dropDown();
          }
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
      this.blocks[i].display(this, g2d, this.players);
    }
    for (int i = 0; i < this.players.length; i++) {
      this.players[i].display(this, g2d, this.players);
      this.players[i].displayHitbox(g2d, this.players);
      if (this.players[i].isAttackState()) {
        this.players[i].displayHurtbox(g2d, this.players);
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
    // System.out.println("player: " + this.players[0].getX());
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
   * get the active held keys of the world
   * @return activeHeldKeys, the keys held
   */
  public HashSet<String> getActiveHeldKeys() {
    return this.activeHeldKeys;
  }

  /**
   * remove an active key from the active key set
   * @param key the key to remove
   */
  public void removeActiveHeldKey(String key) {
    this.activeHeldKeys.remove(key);

    //if the player is in a special state,
    //no keys should work
    for (int i = 0; i < this.players.length; i++) {
      Hero curPlayer = this.players[i];
      if (!curPlayer.inSpecialState()
          && (key.equals(curPlayer.getLeftKey())
              || key.equals(curPlayer.getRightKey()))) {
        curPlayer.resetXMovement();
      }
    }
  }

}