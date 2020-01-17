package world;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JPanel;

import blocks.Block;
import blocks.RectBlock;
import characters.Hero;
import items.Bomb;
import items.DamagableItemSpawns;
import items.Item;
import items.ItemFactory;
import maps.Map;
import maps.MapFactory;
import util.CircleCollidable;
import util.Collision;
import util.DamageIndicator;
import util.RectCollidable;
import util.*;
import weapons.CloseRange;

public class World extends JPanel {

  public static final int mapWidth = Util.scaleX(4000);
  public static final int mapHeight = Util.scaleY(2400);

  public static final int screenMarginX = Util.scaleX(150);
  public static final int screenMarginY = Util.scaleY(150);

  public static final double GRAVITY = Util.scaleY(0.25);
  
  private String mapName;
  private BufferedImage background;

  private HashSet<Item> items = new HashSet<Item>();
  private HashSet<DamagableItemSpawns> damagableItemSpawns = 
    new HashSet<DamagableItemSpawns>();
  private Hero[] players;
  private Block[] blocks;
  private boolean running = true;


  //key listeners doesn't work all the time
  //need constant update on keys
  private HashSet<String> activeHeldKeys = new HashSet<String>();

  private ItemFactory itemFactory;

  /**
   * add the players to the world
   * @throws IOException ImageIO
   */
  private void addPlayers() throws IOException {
    this.players = new Hero[2];
    int scaledPlayerSize = Math.min(Util.scaleX(80), Util.scaleY(80));
    this.players[0] = new Hero(
      World.mapWidth/2 - Util.scaleX(300),
      (int)(World.mapHeight/2.2),
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize/2,
      "A", "D", "Space", "S", "J", "K", "H", "U",
      new CloseRange("assets/config/weapons/fist.txt"), "ash");
    this.players[1] = new Hero(
      World.mapWidth/2 + Util.scaleX(300),
      (int)(World.mapHeight/2.2),
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize,
      scaledPlayerSize/2,
      "Left", "Right", "Up", "Down", "Comma", "Period", "Slash", "L",
      new CloseRange("assets/config/weapons/fist.txt"), "ashCopy");

    int gap = Util.scaleX(30);
    for (int i = 0; i < this.players.length; i++) {
      this.players[i].setDamageIndicatorPos(
        GameWindow.width
        -this.players.length*(DamageIndicator.RADIUS*2)
        -gap*(this.players.length-i)
        +i*DamageIndicator.RADIUS*2
        +gap
      );
    }
  }

  public void reset() throws IOException {
    this.addPlayers();
    this.activeHeldKeys.clear();
    this.running = true;
  }
  
  // /**
  //  * add the necessary blocks to the world
  //  * @throws IOException ImageIO
  //  */
  // private void addBlocks() throws IOException {
    
  // }

  /**
   * constructor
   * @throws IOException ImageIO
   */
  public World(String mapName) throws IOException {
    
    this.setFocusable(true);
    this.requestFocusInWindow();
    this.setPreferredSize(GameWindow.screenSize);
    
    Map map = MapFactory.getMap(mapName);
    this.mapName = mapName;
    this.blocks = map.getBlocks();
    this.background = map.getBackground();
    this.addPlayers();
    this.addKeyListener(new Controls(this));
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
   * output score to score.txt if a player has won
   */
  private void updatePlayers() throws IOException {

    //for each player
    for (int i = 0; i < this.players.length; i++) {
      Hero curPlayer = this.players[i];
      curPlayer.updateMovement();
      curPlayer.updateSprite();
      curPlayer.updateTimedTasks();

      checkPlayerBlockCollisions(curPlayer);
      updateAttackCollisions(curPlayer);

      if (curPlayer.isDead()) {
        this.running = false;
        PrintWriter scoreOut = new PrintWriter(
          new FileWriter("text-file-output/score.txt", true)
        );
        scoreOut.println  ("Player"+(i+1)+" lost: "+this.mapName+" map");
        scoreOut.close();
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
          && curPlayer.damagable()
          && Collision.isCollided((RectCollidable)curItem, 
                                  (RectCollidable)curPlayer)) {
        
        curItem.hitPlayer(curPlayer);
        if (curItem instanceof Bomb) {
          System.out.println("play exploding animation");
        }
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
      curItem.updateTimedTasks();
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
   * tell the player to use the gadget
   * stores the items spawned with the gadget in the world
   * @param player the player using the gadget
   */
  public void useGadget(Hero player) {
    this.damagableItemSpawns.add(player.getGadgetAction());
  }

  /**
   * check collision between item spawns (spikes, explosion, etc)
   * and all players (the player who used the gadget can also be damaged)
   * @param curItemSpawns the item spawn
   */
  private void checkItemSpawnsPlayerCollision(DamagableItemSpawns curItemSpawns) {
    Hero curPlayer;
    for (int i = 0; i < this.players.length; i++) {
      curPlayer = this.players[i];
      if (curPlayer.damagable()) {
        for (int j = 0; j < curItemSpawns.getHurtboxes().length; j++) {
          if (Collision.isCollided(
                (CircleCollidable)curItemSpawns.getHurtboxes()[j], 
                (CircleCollidable)curPlayer)) {
            curItemSpawns.hitPlayer(curPlayer);
          }
        }
      }
      
    }
  }

  private synchronized void updateGadgetAction() {
    Iterator<DamagableItemSpawns> itr = this.damagableItemSpawns.iterator();
    DamagableItemSpawns curItemSpawns;
    while (itr.hasNext()) {
      curItemSpawns = itr.next();
      curItemSpawns.updateAll();
      if (curItemSpawns.isOver()) {
        itr.remove();
      } else if (curItemSpawns.isActive()) {
        this.checkItemSpawnsPlayerCollision(curItemSpawns);
      }
    }
  }

  /**
   * updates the world
   */
  public void update() throws IOException {
    this.updateControls();
    this.updatePlayers();
    this.updateItems();
    this.updateGadgetAction();
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

    //displaying item spawned things
    Iterator<DamagableItemSpawns> itemSpawnItr = this.damagableItemSpawns.iterator();
    while (itemSpawnItr.hasNext()) {
      itemSpawnItr.next().display(g2d, this.players);
    }

    //displaying player related things
    for (int i = 0; i < this.players.length; i++) {
      this.players[i].display(this, g2d, this.players);
      this.players[i].displayHitbox(g2d, this.players);
      this.players[i].displayDamageIndicator(g2d);
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

    for (int i = 0; i < this.players.length; i++) {
      Hero curPlayer = this.players[i];
      if (key.equals(curPlayer.getLeftKey())
          || key.equals(curPlayer.getRightKey())) {
        curPlayer.resetXMovement();
      }
    }
  }

}