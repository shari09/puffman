package world;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.awt.image.*;
import java.io.*;
import java.util.Iterator;

import items.*;
import characters.*;
import blocks.*;
import util.*;
import maps.Map;

public class World extends JPanel {
  public static final long serialVersionUID = 1L;

  public static final int mapWidth = Util.scaleX(4000);
  public static final int mapHeight = Util.scaleY(2400);

  public static final int screenMarginX = Util.scaleX(150);
  public static final int screenMarginY = Util.scaleY(150);

  public static final double GRAVITY = Util.scaleY(0.25);
  
  private BufferedImage background;

  private HashSet<Item> items = new HashSet<>();
  private Hero[] players;
  private Block[] blocks;
  private boolean running = true;


  //key listeners doesn't work all the time
  //need constant update on keys
  private HashSet<String> activeHeldKeys = new HashSet<>();


  private ItemFactory itemFactory;

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
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize/2,
      "A", "D", "Space", "S", "J", "K", "H");
    this.players[1] = new AshCopy(
      World.mapWidth/2 + Util.scaleX(300),
      (int)(World.mapHeight/2.5),
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize/2,
      "Left", "Right", "Up", "Down", "Comma", "Period", "Slash");
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
    this.blocks = Map.getMap(1);
  }

  /**
   * constructor
   * @throws IOException ImageIO
   */
  public World() throws IOException {
    
    this.setFocusable(true);
    this.requestFocusInWindow();
    this.setPreferredSize(GameWindow.screenSize);
    this.background = Util.urlToImage("background/background.jpg");

    this.addPlayers();
    this.addKeyListener(new Controls(this));
    this.addBlocks();
    this.itemFactory = new ItemFactory((RectBlock[])(this.blocks));
  };

  /**
   * for every block in the game: 
   * Checks whether the player collides with the block
   * moves the player back until it no longer collides
   * also sets the movement state for the player
   * @param curPlayer the player that's checked for collision
   * @param curBlock the block that's being checked for collision
   */
  private void checkPlayerBlockCollisions(Hero curPlayer) {

    boolean inAir = true;

    for (int i = 0; i < this.blocks.length; i++) {
      Block curBlock = this.blocks[i];

      if (Collision.isCollided((RectCollidable)curPlayer, 
                               (RectCollidable)curBlock)) {

        double xVel = curPlayer.getXVel();
        double yVel = curPlayer.getYVel();

        //move the player back until they no longer collide
        while (Collision.isCollided((RectCollidable)curPlayer, 
                                    (RectCollidable)curBlock)) {
          if (Math.abs(xVel) > Math.abs(yVel)) {
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
              System.out.println(targetPlayer.getDamageTaken());
            }
          }
        }


      }
    }
  }

  /**
   * check to see if the player can pick up anything
   * @param player the player to check for
   */
  public void checkPickUp(Hero player) {
    Iterator<Item> itr = this.items.iterator();
    Item item;
    while (itr.hasNext()) {
      item = itr.next();
      if ((item.getState() == Item.SPAWNED
          || item.getState() == Item.DISAPPEARING)
          && Collision.isCollided((CircleCollidable)player,
             (RectCollidable)item)) {
        player.pickUp(item);
        item.setState(Item.ON_PLAYER);
        item.reset();
        return;
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

      checkPlayerBlockCollisions(curPlayer);
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
  private synchronized void updateControls() {
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
   * checks for item and block collisions
   * @param curItem the current item to check for
   */
  private void checkItemBlockCollision(Item curItem) {
    //block-item collision
    Block curBlock;
    for (int i = 0; i < this.blocks.length; i++) {
      curBlock = this.blocks[i];
      if (Collision.isCollided((RectCollidable)curItem, 
                               (RectCollidable)curBlock)) {
        double xVel = curItem.getXVel();
        double yVel = curItem.getYVel();

        //move the item back until they no longer collide
        while (Collision.isCollided((RectCollidable)curItem, 
                                    (RectCollidable)curBlock)) {
          if (Math.abs(xVel) > Math.abs(yVel)) {
            curItem.moveX(-xVel/Math.abs(xVel));
            curItem.moveY(-yVel/Math.abs(xVel));
          } else {
            curItem.moveX(-xVel/Math.abs(yVel));
            curItem.moveY(-yVel/Math.abs(yVel));
          } 
        }

        String side = Collision.getTouchingSide((RectCollidable)curItem, 
                                                (RectCollidable)curBlock);
        if (side.equals("bottom")) {
          curItem.hitGround();
        } else {
          curItem.hitWall();
        }
      }
    }
  }

  /**
   * checks collision between the thrown items and players
   * sees if the item damages the player 
   * @param curItem the current item that's being checked for
   */
  private void checkItemPlayerCollision(Item curItem) {
    Hero curPlayer;
    for (int i = 0; i < this.players.length; i++) {
      curPlayer = this.players[i];
      if (curPlayer != curItem.getNonDamagablePlayer()
          && Collision.isCollided((RectCollidable)curItem, 
                                  (RectCollidable)curPlayer)) {
        
        curItem.hitPlayer(curPlayer);
        
      }
    }
  }

  /**
   * have a 0.1% chance of randomly spawning the items based on the current
   * number of items in the world
   */
  private void spawnItem() throws IOException {
    if (this.items.size() < 4 && Math.random() < 0.001) {
      this.items.add(this.itemFactory.getRandomItem());
    }
  }

  /**
   * update all items
   * - get rid of items that already disappeared
   * - move the throwing items
   * - check throwing item collision with both player and block
   */
  private void updateItems() throws IOException {
    
    Iterator<Item> itr = this.items.iterator();
    Item curItem;
    while (itr.hasNext()) {
      curItem = itr.next();
      //remove items
      if (!curItem.isAlive()) {
        itr.remove();
      } else if (curItem.getState() == Item.THROWING) {
        //update movement
        curItem.moveX((int)(curItem.getXVel()));
        curItem.setYVel(curItem.getYVel()+World.GRAVITY/2);
        curItem.moveY((int)(curItem.getYVel()));

        this.checkItemBlockCollision(curItem);
        this.checkItemPlayerCollision(curItem);
  
      }
    }

    this.spawnItem();

  }

  /**
   * updates the world
   */
  public void update() throws IOException {
    this.updateControls();
    this.updatePlayers();
    this.updateItems();
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
    //display blocks
    for (int i = 0; i < this.blocks.length; i++) {
      this.blocks[i].display(this, g2d, this.players);
    }

    //display items
    Iterator<Item> itemItr = this.items.iterator();
    Item item;
    while (itemItr.hasNext()) {
      item = itemItr.next();
      if (item.getState() != Item.ON_PLAYER) {
        item.display(this, g2d, this.players);
      }
      
    }

    //displaying player related things
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
   * get the blocks/map of the current world
   * @return Block[], the blocks
   */
  public Block[] getBlocks() {
    return this.blocks;
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