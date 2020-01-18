package characters;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JPanel;

import items.DamagableItemSpawns;
import items.Gadget;
import items.Item;
import util.CircleCollidable;
import util.DamageIndicator;
import util.Hurtbox;
import util.RectCollidable;
import util.States;
import util.TimedTask;
import util.Util;
import util.*;
import weapons.PickupableWeaponHolder;
import weapons.Weapon;
import world.World;

/**
 * [Hero.java]
 * Creates a player controlled Hero (character).
 * 
 * 2020-01-17
 * @version 0.0.4
 * @author Shari Sun
 */

//cast to RectCollidable to get block collision box
//cast to CircleCollidable to get hitbox
public class Hero implements CircleCollidable, RectCollidable {

  //hitbox of the player is displayed in red
  private final Color hitboxColour = new Color(200, 0, 0, 60);

  //controls
  private String leftKey;
  private String rightKey;
  private String jumpKey;
  private String lightAttackKey;
  private String heavyAttackKey;
  private String dropKey;
  private String pickUpKey;
  private String dodgeKey;


  //dimensions of the player for collisions
  private double x;
  private double y;
  private int width;
  private int height;
  private int radius;

  //image widtd/height might not match actual collision parts of the player
  //Ex. when the player is clinging onto the wall, 
  //the image actually goes on the wall
  private int imageWidth;
  private int imageHeight;

  //speed/velocity
  private double xVel;
  private double xTargetSpeed;
  private final double acceleration = Util.scaleX(2.0);
  private final double jumpVel = -World.GRAVITY*32;
  private final double X_MAX_SPEED = Util.scaleX(5.0);
  private final double Y_MAX_SPEED = Util.scaleY(30);
  private final double dropVel = World.GRAVITY*3;
  
  private int dir = 1;
  private double yVel;
  private int numJumps;

  //counters for animation (though there are none as of the moment)
  private int spriteNum;
  private final int framesPerSprite = 3;
  private int curSpriteFrame;

  //boolean flags
  private boolean inSpecialState = false;
  private boolean activeAttackState = false;
  private boolean lightRecovery = false;
  private boolean heavyRecovery = false;
  private boolean gravityCancel = false;
  private boolean dodgeCoolDown = false;

  //damage
  private int damageTaken = 1;

  //states: onLeftWall, onRightWall, faceRight, faceLeft, etc
  //special states count as you do something once,
  //it runs for a duration of time and cannot be disturbed
  private String state = "onGround";


  private HashMap<String, BufferedImage[]> sprites = 
    new HashMap<String, BufferedImage[]>();
  
  //player held weapons/gadgets
  private Weapon fist;
  private Item curItem;
  private Weapon weapon;
  private DamageIndicator damageIndicator;
  

  /**
   * Loads all the sprite images with data from a text file then
   * adds all sprites to the player.
   * @param heroName the name of the Hero (also name of the text file).
   * @throws IOException
   */
  private void addAllSprites(String heroName) throws IOException {
    BufferedReader reader = new BufferedReader(
      new FileReader("assets/config/characters/"+heroName+"Sprites.txt")
    );
    String line = reader.readLine();
    String[] data;
    BufferedImage[] sprites;
    while (line != null) {
      data = line.split("\\s+"); //regex for spliting all spaces
      sprites = new BufferedImage[data.length-1];
      for (int i = 1; i < data.length; i++) {
        sprites[i-1] = Util.urlToImage("characters/"+heroName+"/"+data[i]);
        
      }
      this.sprites.put(data[0], sprites);
      line = reader.readLine();
    }
    reader.close();
  }

  /**
   * Constructor.
   * @param x the x-position where the player spawns.
   * @param y the y-position where the player spawns.
   * @param width the width of the player (for block collision).
   * @param height the height of the player (for block collision).
   * @param imageWidth the actual width of the player's images that is greater 
   *                   than the player width because the actual image may overlap
   *                   certain things in the world (Ex. weapons overlapping walls).
   * @param imageHeight the actual height of the player's images that is greater 
   *                    than the player height because the actual image may overlap
   *                    certain things in the world (Ex. weapons overlapping walls).
   * @param hitboxRadius the player's hitbox radius.
   * @param leftKey the key for this player to move left.
   * @param rightKey the key for this player to move right.
   * @param jumpKey the key for this player to jump.
   * @param dropKey the key for this player to drop down.
   * @param lightAttackKey the key for this player to perform a light attack.
   * @param heavyAttackKey the key for this player to perform a heavy attack.
   * @param pickUpKey the key for this player to pick up or throw items.
   * @param dodgeKey the key for this player to dodge.
   * @param fist the initial fist (different players may be configured differently).
   * @param heroName the name of the hero (to get the sprite datasheet).
   * @throws IOException
   */
  public Hero(int x, int y, int width, int height,
              int imageWidth, int imageHeight,
              int hitboxRadius,
              String leftKey, String rightKey,
              String jumpKey, String dropKey, 
              String lightAttackKey, String heavyAttackKey,
              String pickUpKey, String dodgeKey,
              Weapon fist, String heroName) throws IOException {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.imageWidth = imageWidth;
    this.imageHeight = imageHeight;
    this.radius = hitboxRadius;
    this.leftKey = leftKey;
    this.rightKey = rightKey;
    this.jumpKey = jumpKey;
    this.dropKey = dropKey;
    this.lightAttackKey = lightAttackKey;
    this.heavyAttackKey = heavyAttackKey;
    this.pickUpKey = pickUpKey;
    this.dodgeKey = dodgeKey;
    this.fist = fist;
    this.weapon = this.fist;
    this.addAllSprites(heroName);
  }


  /**
   * Calling the light attack the player performs based
   * on their current weapon and state.
   * @param heldKeyList the list of keys held down.
   * @param tappedKeys the list of keys tapped.
   */
  public void lightAttack(HashSet<String> heldKeyList,
                          HashSet<String> tappedKeys) {
    if (this.activeAttackState) {
      return;
    }

    if (heldKeyList.contains(this.dropKey) && this.dir == -1) {
      this.weapon.attack(this, "lightDLeft", this.dir);
    } else if (heldKeyList.contains(this.dropKey) && this.dir == 1) {
      this.weapon.attack(this, "lightDRight", this.dir);
    } else if (this.xVel < 0 && this.state.equals("onGround")) {
      this.weapon.attack(this, "lightLeft", this.dir);
    } else if (this.xVel > 0 && this.state.equals("onGround")) {
      this.weapon.attack(this, "lightRight", this.dir);
    } else if (this.xVel == 0 && this.dir == -1
               && this.state.equals("onGround")) {
      this.weapon.attack(this, "lightNLeft", this.dir);
    } else if (this.xVel == 0 && this.dir == 1
               && this.state.equals("onGround")) {
      this.weapon.attack(this, "lightNRight", this.dir);
    } else if (this.state.equals("inAir")
               && tappedKeys.contains(this.jumpKey)
               && !this.lightRecovery) {
      this.weapon.attack(this, "lightJump", this.dir);
      this.lightRecovery = true;
    } else if (this.state.equals("inAir") 
               && this.xVel == 0 && this.dir == -1) {
      this.weapon.attack(this, "lightNLair", this.dir);
    } else if (this.state.equals("inAir") 
               && this.xVel == 0 && this.dir == 1) {
      this.weapon.attack(this, "lightNRair", this.dir);
    } else if (this.state.equals("inAir") 
               && this.xVel < 0) {
      this.weapon.attack(this, "lightSLair", this.dir);
    } else if (this.state.equals("inAir") 
               && this.xVel > 0) {
      this.weapon.attack(this, "lightSRair", this.dir);
    }
  }

  /**
   * Calling the player to perform a heavy attack 
   * based on their current weapon and state.
   * @param heldKeyList the keys held down.
   * @param tappedKeys the keys being tapped.
   */
  public void heavyAttack(HashSet<String> heldKeyList,
                          HashSet<String> tappedKeys) {
    if (this.activeAttackState) {
      return;
    }

    if (heldKeyList.contains(this.dropKey)
        && this.state.equals("inAir")) {
      this.weapon.attack(this, "heavyDown", this.dir);
    } else if (this.xVel < 0 && this.state.equals("onGround")) {
      this.weapon.attack(this, "heavyLeft", this.dir);
    } else if (this.xVel > 0 && this.state.equals("onGround")) {
      this.weapon.attack(this, "heavyRight", this.dir);
    } else if (this.xVel == 0 && this.dir == -1
               && this.state.equals("onGround")) {
      this.weapon.attack(this, "heavyNLeft", this.dir);
    } else if (this.xVel == 0 && this.dir == 1
               && this.state.equals("onGround")) {
      this.weapon.attack(this, "heavyNRight", this.dir);
    } else if (this.state.equals("inAir")
               && tappedKeys.contains(this.jumpKey)
               && !this.heavyRecovery) {
      this.weapon.attack(this, "heavyJump", this.dir);
      this.heavyRecovery = true;
    } else if (this.state.equals("inAir") 
               && this.xVel == 0 && this.dir == -1) {
      this.weapon.attack(this, "heavyNLair", this.dir);
    } else if (this.state.equals("inAir") 
               && this.xVel == 0 && this.dir == 1) {
      this.weapon.attack(this, "heavyNRair", this.dir);
    } else if (this.state.equals("inAir") 
               && this.xVel < 0) {
      this.weapon.attack(this, "heavySLair", this.dir);
    } else if (this.state.equals("inAir") 
               && this.xVel > 0) {
      this.weapon.attack(this, "heavySRair", this.dir);
    }
  }
  
  /**
   * Break out of the special state.
   * The player is now controllable.
   */
  private void breakSpecialState() {
    this.inSpecialState = false;
    this.resetXMovement();
  }


  /**
   * Deal damage to another player.
   * @param other Hero, the another player.
   */
  public void damage(Hero other) {
    this.weapon.knockBack(other, this.state, this.dir);
  }

  /**
   * Adds to the current player's damage.
   * @param damage int, the amount of damage dealt.
   */
  public void takeDamage(int damage) {
    this.damageTaken += damage;
  }

  /**
   * Make the player jump if they are still allowed to jump.
   */
  public void jump() {
    if (this.numJumps < 3) {
      if (this.state.equals("onLeftWall")) {
        this.moveRight();
        this.setSpecialState("jumpFromWall", 180);
      } else if (this.state.equals("onRightWall")) {
        this.moveLeft();
        this.setSpecialState("jumpFromWall", 180);
      }
      this.yVel = this.jumpVel;
      this.numJumps++;
    }


  }

  /**
   * Resets the y-velocity to 0.
   */
  public void resetYVel() {
    this.yVel = 0;
  }

  /**
   * Change the direction and the x target speed to move left.
   */
  public void moveLeft() {
    if (!this.state.equals("onLeftWall")) {
      this.dir = -1;
      this.xTargetSpeed = this.X_MAX_SPEED;
    }
  }

  /**
   * Change the direction and x target speed to move right.
   */
  public void moveRight() {
    if (!this.state.equals("onRightWall")) {
      this.dir = 1;
      this.xTargetSpeed = this.X_MAX_SPEED;
    }
  }

  /**
   * Reset the x vel and x target speed.
   */
  public void resetXMovement() {
    this.xVel = 0;
    this.xTargetSpeed = 0;
  }

  /**
   * Reset the number of jumps to zero.
   * Set recovery states to false (the player is allowed one light and
   * one heavy recovery per "airtime").
   */
  public void resetJumps() {
    this.numJumps = 0;
    this.lightRecovery = false;
    this.heavyRecovery = false;
  }

  /**
   * Updates all the timed tasks/events correspondingly if their time is up.
   */
  public void updateTimedTasks() {
    if (TimedEventQueue.validTask(this)) {
      String action = TimedEventQueue.getTask().getAction();
      if (action.equals("gravityCancelOver")) {
        this.gravityCancel = false;
        this.yVel = 0;
      } else if (action.equals("dodgeCoolDownOver")) {
        this.dodgeCoolDown = false;
      } else if (action.equals("breakSpecialState")) {
        this.breakSpecialState();
      } else if (action.equals("activeAttackState")) {
        this.activeAttackState = true;
      } else if (action.equals("activeAttackStateOver")) {
        this.activeAttackState = false;
      } else if (action.equals("heavySideSpeedUp")) {
        this.setxTargetSpeed(Util.scaleX(25));
      } else if (action.equals("resetXMovement")) {
        this.resetXMovement();
      }
    } 
  }

  /**
   * Updates sprite animation (though it is only looping through one image for now).
   */
  public void updateSprite() {
    //move to next frame
    if (this.curSpriteFrame == this.framesPerSprite) {
      if (this.spriteNum+1 < this.sprites.get(this.state).length) {
        this.spriteNum++;
      } else {
        this.spriteNum = 0;
      }
      this.curSpriteFrame = 0;
    }
    this.curSpriteFrame++;
  }

  /**
   * Update all movements (left, right, up, down).
   */
  public void updateMovement() {
    //if the x speed hasn't reached its max, increase the velocity
    if (Math.abs(this.xVel) < this.xTargetSpeed) {
      this.xVel += this.acceleration*this.dir;
    }

    //falling because of gravity -> freefall acceleration
    this.yVel += World.GRAVITY;

    //cling onto wall
    if (this.state.equals("onLeftWall")
        || this.state.equals("onRightWall")) {
      this.yVel -= World.GRAVITY/1.2;
    }

    this.x += this.xVel;
    
    if (!this.gravityCancel) {
      this.y += this.yVel;
    }
  }

  /**
   * Creates a gravity cancel for a period of time.
   * The player/character is immune to gravity and will freeze in the air.
   * @param delay the time of the gravity cancel.
   */
  public void gravityCancel(int delay) {
    this.gravityCancel = true;
    TimedEventQueue.addTask(new TimedTask(this, "gravityCancelOver", delay));
  }

  /**
   * Allow the player to drop down faster.
   */
  public void dropDown() {
    this.setState("drop");
    if (Math.abs(this.yVel) < this.Y_MAX_SPEED) {
      this.yVel += this.dropVel;
    }
  }

  /**
   * Allows the player to be in dodge state.
   * The player cannot move but the player is immune to all attacking
   * for one second.
   * The player has a 3 seconds cool down before it can dodge again.
   */
  public void dodge() {
    this.dodgeCoolDown = true;
    this.setSpecialState("dodge", 1000); //1sec dodge
    TimedEventQueue.addTask(new TimedTask(this, "dodgeCoolDownOver", 4000));
  }

  /**
   * throws the current item (gadget/weapon) the player has
   */
  public void throwItem() {
    this.curItem.setState(Item.THROWING);
    this.curItem.setX((int)(this.x));
    this.curItem.setY((int)(this.y));
    this.curItem.setXVel(Item.X_SPEED*this.dir);
    if (this.curItem instanceof PickupableWeaponHolder) {
      this.weapon = this.fist;
    }
    this.curItem = null;
  }

  /**
   * use the current item
   * @return DamagableItemSpawns, a series of hurtboxes based on the item
   *                              being used
   */
  public DamagableItemSpawns getGadgetAction() {

    DamagableItemSpawns itemSpawns = ((Gadget)(this.curItem)).use(
      (int)(this.x+this.width*this.dir), 
      (int)(this.y), 
      this.dir);
    this.curItem = null;
    return itemSpawns;
  }

  /**
   * picks up an item
   * @param item the item to pick up
   */
  public void pickUp(Item item) {
    this.curItem = item;
    this.curItem.setNonDamagablePlayer(this);
    if (item instanceof PickupableWeaponHolder) {
      this.weapon = ((PickupableWeaponHolder)(item)).getWeapon();
    }
  }

  /**
   * Get whether or not the player is dead.
   * @return boolean, whether the player is dead
   */
  public boolean isDead() {
    if (this.x + this.radius/2 < 0
        || this.x - this.radius/2 > World.MAP_WIDTH
        || this.y + this.radius < 0
        || this.y - this.radius > World.MAP_HEIGHT) {
      return true;
    }
    return false;
  }


  /**
   * display the character to the panel
   * @param panel the JPanel to display to
   * @param g2d the graphics manager
   */
  public void display(JPanel panel, Graphics2D g2d, Hero[] players) {
    int[] pos = {(int)(this.x), (int)(this.y), 
                 this.imageWidth, this.imageHeight};
    int[] newPos = Zoom.getDisplayPos(g2d, players, pos);
    float opacity;
    if (this.state.equals("dodge")) {
      opacity = 0.5f;
    } else {
      opacity = 1f;
    }

    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                opacity));
    g2d.drawImage(this.sprites.get(this.state)[this.spriteNum],
                    newPos[0], newPos[1], newPos[2], newPos[3],
                    panel);
  }

  /**
   * display the player hitbox
   * that allows the player to be hit
   * @param g2d the graphics manager
   */
  public void displayHitbox(Graphics2D g2d, Hero[] players) {
    g2d.setColor(this.hitboxColour);
    int[] pos = {(int)(this.x), (int)(this.y), 
                 this.radius*2, this.radius*2};
    int[] newPos = Zoom.getDisplayPos(g2d, players, pos);
    g2d.fill(new Ellipse2D.Double(newPos[0], newPos[1], 
                                  newPos[2], newPos[3]));
  }

  /**
   * display the hitbox for player attacks
   * @param g2d the graphics manager
   */
  public void displayHurtbox(Graphics2D g2d, Hero[] players) {
    this.weapon.updateHurtbox(this, this.state, this.dir);
    for (int i = 0; i < this.weapon.getNumHurtboxes(); i++) {
      this.weapon.getHurtboxes()[i]
        .display(g2d, players, this.activeAttackState);
    }
  }

  /**
   * displays the damage indicator for
   * @param g2d the graphics manager
   * @param players the players data used for scaling
   */
  public void displayDamageIndicator(Graphics2D g2d) {
    //so the damage indicator won't turn translucent
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                1f));
    this.damageIndicator.display(g2d, this.damageTaken);
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //setters and getters

  /**
   * @return damageTaken int, 
   * the amount of total damage this player has taken
   */
  public int getDamageTaken() {
    return this.damageTaken;
  }

  @Override
  public int getX() {
    return (int)(this.x+this.width/2);
  }

  @Override
  public int getY() {
    return (int)(this.y+this.height/2);
  }

  @Override
  public int getRadius() {
    return this.radius;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  /**
   * sets the targeting x-speed
   * @param xSpeed double, the speed
   */
  public void setxTargetSpeed(double xSpeed) {
    this.xTargetSpeed = xSpeed;
  }

  /**
   * sets the y-velocity
   * @param yVel double, the y-velocity
   */
  public void setYVel(double yVel) {
    this.yVel = yVel;
  }

  /**
   * Get the the horizontal maximum speed.
   * @return this.X_MAX_SPEED double, the max x-speed
   */
  public double getxMaxSpeed() {
    return this.X_MAX_SPEED;
  }

  /**
   * get the x velocity
   * @return double, the xVel
   */
  public double getXVel() {
    return this.xVel;
  }

  /**
   * get the y velocity
   * @return double, the yVel
   */
  public double getYVel() {
    return this.yVel;
  }

  /**
   * sets the direction of the player
   * @param dir int, the direction
   * 1 for right, -1 for left
   */
  public void setDir(int dir) {
    this.dir = dir;
  }


  /**
   * Get the direction that the player is currently facing.
   * 1 for right, -1 for left.
   * @return dir, the direction that the player is facing
   */
  public int getDir() {
    return this.dir;
  }


  /////////////////
  //control key getters

  /**
   * Get the control key for the player to go left.
   * @return the control key for the player to go left.
   */
  public String getLeftKey() {
    return this.leftKey;
  }

  /**
   * Get the control key for the player to go right.
   * @return the control key for the player to go right.
   */
  public String getRightKey() {
    return this.rightKey;
  }

  /**
   * Get the control key for the player to jump.
   * @return the control key for the player to jump.
   */
  public String getJumpKey() {
    return this.jumpKey;
  }

  /**
   * Get the control key for the player to drop.
   * @return the control key for the player to drop.
   */
  public String getDropKey() {
    return this.dropKey;
  }

  /**
   * Get the control key for the player to make a light attack.
   * @return the control key for the player to make a light attack.
   */
  public String getLightAttackKey() {
    return this.lightAttackKey;
  }

  /**
   * Get the control key for the player to make a heavy attack.
   * @return the control key fo the player to make a heavy attack.
   */
  public String getHeavyAttackKey() {
    return this.heavyAttackKey;
  }

  /**
   * Get the control key for the player to pick up/throw an item.
   * @return the control key for the player to pick up/throw an item.
   */
  public String getPickUpKey() {
    return this.pickUpKey;
  }

  /**
   * Get the control key for the player to dodge.
   * @return the control key for the player to dodge.
   */
  public String getDodgeKey() {
    return this.dodgeKey;
  }


  /**
   * moves the x pos of the player
   * @param x double, the x change
   */
  public void moveX(double x) {
    this.x += x;
  }

  /**
   * moves the y pos of the player
   * @param y double, the y change
   */
  public void moveY(double y) {
    this.y += y;
  }

  /**
   * Add sprites to the character
   * @param type the name of the collection of sprites
   * @param images the sprites
   */
  public void addSprite(String state, BufferedImage[] images) {
    // checkValidState(state);
    this.sprites.put(state, images);
  }

  /**
   * Sets the current state of the player for displaying
   * Such as facing left/right, attack, dodge, etc
   * @param state the state to set the player tospecialStateOver
   */
  public void setState(String state) {
    // checkValidState(state);
    if ((!States.special.contains(this.state)
        || !this.inSpecialState)
        && this.state != state) {
      this.spriteNum = 0;
      this.curSpriteFrame = 0;
      this.state = state;
    }
    
  }

  /**
   * Add a special state that's immutable for a certain
   * period of time
   * @param state the state of the player
   * @param delay the delay time until the player can move it again
   */
  public void setSpecialState(String state, int delay) {
    if (!this.inSpecialState) {
      this.spriteNum = 0;
      this.curSpriteFrame = 0;
      this.state = state;
      this.inSpecialState = true;
      TimedEventQueue.addTask(new TimedTask(this, "breakSpecialState", delay));
    }
  }

  /**
   * Sets the attack state
   * @param state String, the attack state
   * @param loadingTime int, the loading time for the attack
   * @param activeTime int, the active time 
   *                   where the attack can damage the opponent
   * @param recoveryTime the time it takes 
   *                     for the weapon to recover from the attack
   */
  public void setAttackState(String state, 
                             int loadingTime, 
                             int activeTime, 
                             int recoveryTime) {
    this.setSpecialState(state, loadingTime+activeTime+recoveryTime);
    this.resetXMovement();
    TimedEventQueue.addTask(new TimedTask(this, "activeAttackState", loadingTime));
    TimedEventQueue.addTask(new TimedTask(this, "activeAttackStateOver", 
                                 loadingTime+activeTime));
    
  }

  /**
   * Get whether or not the player is in the middle of attacking.
   * @return boolean, whether the player is in the middle of attacking.
   */
  public boolean isAttackState() {
    return States.attack.contains(this.state);
  }

  /**
   * Get whether or not the attack is "active",
   * meaning the player is able to harm others (weapon not loading
   * or recovering). 
   * @return boolean, whether the state is an activeAttackState.
   *
   */
  public boolean isActiveAttackState() {
    return this.activeAttackState;
  }

  /**
   * Get whether or not the player is in a special state that 
   * cannot be disturbed.
   * @return boolean, whether the player is in a special state.
   */
  public boolean inSpecialState() {
    return this.inSpecialState;
  }

  /**
   * Get the player's hurtboxes based on the weapon (or fist) that it posses.
   * @return the player's hurtboxes.
   */
  public Hurtbox[] getHurtboxes() {
    return this.weapon.getHurtboxes();
  }

  /**
   * get the number of hurtboxes
   * @return int, the number of hurtboxes
   */
  public int getNumHurtboxes() {
    return this.weapon.getNumHurtboxes();
  }

  /**
   * Get whether or not the player currently have an item.
   * @return boolean, whether or not the player 
   * currently have an item (weapon counts).
   */
  public boolean hasItem() {
    return (this.curItem != null);
  }

  /**
   * Get whether or not the player currently possesses a gadget.
   * @return boolean, whether or not the player
   * currently possesses a gadget.
   */
  public boolean hasGadget() {
    return (this.curItem instanceof Gadget);
  }

  /**
   * Get whether the player is damagable at the moment.
   * @return boolean, whether the player is damagable at the moment.
   */
  public boolean damagable() {
    return !this.state.equals("knockedBack")
            && !this.state.equals("dodge");
  }

  /**
   * Get whether the player's dodge ability is in the 
   * cooldown state or not.
   * @return boolean, whether the player's dodge ability is in the 
   *                  cooldown state or not.
   */
  public boolean inDodgeCoolDown() {
    return this.dodgeCoolDown;
  }

  /**
   * Set the x position of the player's damage indicator.
   * @param x the x position of the damage indicator. The value
   *          is calculated in the world (based on how many
   *          players there are in total). 
   */
  public void setDamageIndicatorPos(int x) {
    this.damageIndicator = new DamageIndicator(
      x, 
      DamageIndicator.RADIUS*2);
  }

}