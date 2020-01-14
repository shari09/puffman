package characters;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import java.awt.*;

import items.*;
import util.*;
import world.*;
import weapons.*;


//cast to rectCollidable to get block collision box
//cast to circleCollidable to get hit box
public class Hero implements CircleCollidable, RectCollidable {

  private final Color hitboxColour = new Color(200, 0, 0, 60);

  //controls
  private String leftKey;
  private String rightKey;
  private String jumpKey;
  private String lightAttackKey;
  private String heavyAttackKey;
  private String dropKey;
  private String pickUpKey;


  private double x;
  private double y;
  private int width;
  private int height;
  private int radius;

  private int imageWidth;
  private int imageHeight;


  private double xVel;
  private double xTargetSpeed;
  private final double acceleration = Util.scaleX(2.0);
  private final double jumpVel = -World.GRAVITY*32;
  private final double xMaxSpeed = Util.scaleX(5.0);
  private final double yMaxSpeed = Util.scaleY(30);
  private final double dropVel = World.GRAVITY*3;
  
  private int dir = 1;
  private double yVel;
  private int numJumps;

  //counters
  private int spriteNum;
  private final int framesPerSprite = 3;
  private int curSpriteFrame;

  //boolean flags
  private boolean inSpecialState = false;
  private boolean activeAttackState = false;
  private boolean lightRecovery = false;
  private boolean heavyRecovery = false;
  private boolean gravityCancel = false;

  private int damageTaken = 1;

  //states: onLeftWall, onRightWall, faceRight, faceLeft, etc
  private String state = "onGround";


  //special states count as you do something once,
  //it runs for a duration of time
  private HashMap<String, BufferedImage[]> sprites = new HashMap<>();
  private Weapon fist;
  private Item curItem;
  private Weapon weapon;
  
  private void addAllSprites(String heroName) throws IOException {
    BufferedReader reader = new BufferedReader(
      new FileReader("assets/config/characters/"+heroName+"Sprites.txt")
    );
    String line = reader.readLine();
    String[] data;
    BufferedImage[] sprites;
    while (line != null) {
      data = line.split("\\s+");
      sprites = new BufferedImage[data.length-1];
      for (int i = 1; i < data.length; i++) {
        // System.out.println("characters/"+heroName+"/"+data[i]);
        sprites[i-1] = Util.urlToImage("characters/"+heroName+"/"+data[i]);
        
      }
      this.sprites.put(data[0], sprites);
      line = reader.readLine();
    }
    reader.close();
  }

  //constructor
  public Hero(int x, int y, int width, int height,
              int imageWidth, int imageHeight,
              int hitboxRadius,
              String leftKey, String rightKey,
              String jumpKey, String dropKey, 
              String lightAttackKey, String heavyAttackKey,
              String pickUpKey,
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
    this.fist = fist;
    this.weapon = this.fist;
    this.addAllSprites(heroName);
  }


  /**
   * calling the light attack the player performs based
   * on their current weapon and state
   * @param heldKeyList the list of keys held down
   * @param tappedKeys the list of keys tapped
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
   * calling the heavy attacks of the player
   * based on their current weapon and state
   * @param heldKeyList the keys held down
   * @param tappedKeys the keys being tapped
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
   * break out of the special state
   */
  private void breakSpecialState() {
    this.inSpecialState = false;
    this.resetXMovement();
  }


  /**
   * deal damage to the other player
   * @param other Hero, the other player
   */
  public void damage(Hero other) {
    this.weapon.knockBack(other, this.state, this.dir);
  }

  /**
   * adds to the player's damage
   * @param damage int, the amount of damage
   */
  public void takeDamage(int damage) {
    this.damageTaken += damage;
  }

  /**
   * make the player jump if they are still allowed to jump
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
   * resets the y-velocity to 0
   */
  public void resetYVel() {
    this.yVel = 0;
  }

  /**
   * change the direction and target vel to move left
   */
  public void moveLeft() {
    if (!this.state.equals("onLeftWall")) {
      this.dir = -1;
      this.xTargetSpeed = this.xMaxSpeed;
    }
  }

  /**
   * change the direction and target vel to move right
   */
  public void moveRight() {
    if (!this.state.equals("onRightWall")) {
      this.dir = 1;
      this.xTargetSpeed = this.xMaxSpeed;
    }
  }

  /**
   * reset the x vel and x target vel
   */
  public void resetXMovement() {
    this.xVel = 0;
    this.xTargetSpeed = 0;
  }

  /**
   * reset the number of jumps to zero
   * set recovery states to false
   */
  public void resetJumps() {
    this.numJumps = 0;
    this.lightRecovery = false;
    this.heavyRecovery = false;
  }

  /**
   * updates sprite animation
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
   * update all movements (left, right, up, down)
   */
  //the logic is kinda messy here, come back later to fix it
  public void updateMovement() {
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
   * creates a gravity cancel for a period of time
   * @param delay the time of the gravity cancel
   */
  public void gravityCancel(int delay) {
    this.gravityCancel = true;
    util.Timer.setTimeout(() -> {
      this.gravityCancel = false;
      this.resetYVel();
    }, delay);
  }

  /**
   * allow the player to drop down faster
   */
  public void dropDown() {
    this.setState("drop");
    if (Math.abs(this.yVel) < this.yMaxSpeed) {
      this.yVel += this.dropVel;
    }
  }

  public void dodge() {

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
      (int)(this.x), 
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
   * @return boolean, whether the player is still alive
   */
  public boolean isDead() {
    if (this.x + this.radius/2 < 0
        || this.x - this.radius/2 > World.mapWidth
        || this.y + this.radius < 0
        || this.y - this.radius > World.mapHeight) {
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


  // /**
  //  * throws runtime exception if the state is an invalid state
  //  * @param state the state to check for
  //  */
  // private void checkValidState(String state) {
  //   if (!States.all.contains(state)) {
  //     throw new RuntimeException(state + " is not a valid state");
  //   }
  // }

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
   * @return this.xMaxSpeed double, the max x-speed
   */
  public double getxMaxSpeed() {
    return this.xMaxSpeed;
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
   * @return dir, the direction that the player is facing
   * 1 for right, -1 for left
   */
  public int getDir() {
    return this.dir;
  }


  /////////////////
  //control key getters

  /**
   * @return the control for the player to go left
   */
  public String getLeftKey() {
    return this.leftKey;
  }

   /**
   * @return the control for the player to go right
   */
  public String getRightKey() {
    return this.rightKey;
  }

   /**
   * @return the control for the player to jump
   */
  public String getJumpKey() {
    return this.jumpKey;
  }

   /**
   * @return the control for the player to drop
   */
  public String getDropKey() {
    return this.dropKey;
  }

   /**
   * @return the control for the player to make a light attack
   */
  public String getLightAttackKey() {
    return this.lightAttackKey;
  }

  /**
   * @return the control fo the player to make a heavy attack
   */
  public String getHeavyAttackKey() {
    return this.heavyAttackKey;
  }

  /**
   * @return the control for the player to pickUp/throw an item
   */
  public String getPickUpKey() {
    return this.pickUpKey;
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
    if (!States.special.contains(this.state)
        && States.special.contains(state)
        && !this.inSpecialState) {
      this.spriteNum = 0;
      this.curSpriteFrame = 0;
      this.state = state;
      this.inSpecialState = true;
      util.Timer.setTimeout(this::breakSpecialState, delay);
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
    util.Timer.setTimeout(() -> this.activeAttackState = true, loadingTime);
    util.Timer.setTimeout(() -> this.activeAttackState = false,
                          loadingTime+activeTime);
    
  }

  /**
   * @return boolean, whether the player is in the middle of attacking
   */
  public boolean isAttackState() {
    return States.attack.contains(this.state);
  }

  /**
   * @return boolean, whether the state is an activeAttackState,
   * meaning the player is able to harm others (weapon not loading
   * or recovering) 
   */
  public boolean isActiveAttackState() {
    return this.activeAttackState;
  }

  /**
   * @return boolean, whether the player is in a special state
   */
  public boolean inSpecialState() {
    return this.inSpecialState;
  }

  /**
   * @return the player's hurtboxes
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
   * @return boolean, whether or not the player 
   * currently have an item (weapon counts)
   */
  public boolean hasItem() {
    return (this.curItem != null);
  }

  /**
   * @return boolean, whether or not the player
   * currently possesses a gadget
   */
  public boolean hasGadget() {
    return (this.curItem instanceof Gadget);
  }

  /**
   * @return whether the player is damagable at the moment
   */
  public boolean damagable() {
    return !this.state.equals("knockedBack")
            && !this.state.equals("dodge");
  }

}