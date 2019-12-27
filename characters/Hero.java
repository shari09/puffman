package characters;

import java.util.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.*;

import items.*;
import util.*;
import world.*;


//cast to rectCollidable to get block collision box
//cast to circleCollidable to get hit box
public abstract class Hero implements CircleCollidable, RectCollidable {

  private final Color hitboxColour = new Color(200, 0, 0, 60);
  
  //controls
  private String leftKey;
  private String rightKey;
  private String jumpKey;
  private String lightAttackKey;
  private String dropKey;


  private double x;
  private double y;
  private int width;
  private int height;
  private int radius;


  private double xVel;
  private double xTargetVel;
  private final double acceleration = Util.scaleX(2.0);
  private final double jumpVel = -World.GRAVITY*30;
  private final double xMaxVel = Util.scaleX(5.0);
  private final double dropVel = World.GRAVITY*3;
  
  private int dir = 1;
  private double yVel;
  private int numJumps;

  //counters
  private int spriteNum;
  private final int framesPerSprite = 10;
  private int curSpriteFrame;

  //boolean flags
  private boolean inSpecialState = false;

  private int lightAttackPower = 3;
  private int heavyAttackPower = 5;
  private int power;
  private int stungTime;
  private int attackRecoveryTime;
  private int nextWeapon;
  private int damageTaken = 1;

  //weapons hitbox can override the player default hitbox
  private Hitbox attackHitbox;

  //states: onLeftWall, onRightWall, faceRight, faceLeft, etc
  private String state;


  //special states count as you do something once,
  //it runs for a duration of time
  private HashMap<String, BufferedImage[]> sprites = new HashMap<>();
  private Item curItem;
  

  //constructor
  public Hero(int x, int y, int width, int height, int hitboxRadius,
              String leftKey, String rightKey,
              String jumpKey, String dropKey, 
              String lightAttackKey) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.radius = hitboxRadius;
    this.leftKey = leftKey;
    this.rightKey = rightKey;
    this.jumpKey = jumpKey;
    this.dropKey = dropKey;
    this.lightAttackKey = lightAttackKey;
  }


  /**
   * Sets the state to light attack state
   * Creates a attack hitbox for the player
   * 
   */
  public void lightAttack() {
    if (this.xVel != 0) {
      this.xVel = 0;
      this.xTargetVel = this.xMaxVel/2;
    }
    this.attackHitbox = new CircleHitbox(
      (int)(this.getX()+this.dir*Util.scaleX(40)),
      this.getY(),
      Math.min(Util.scaleX(10), Util.scaleY(10)));
    this.power = this.lightAttackPower;
    this.setSpecialState("lightAttack", 300);
  }

  /**
   * update the hitbox position for the light attack
   */
  private void updateLightAttack() {
    int offsetX = this.dir*Util.scaleX(20);
    int offsetY = (int)(this.yVel*Util.scaleY(5));
    offsetY = Math.min(Util.scaleY(20), offsetY);
    this.attackHitbox.adjustPos(
      this.getX()+offsetX, 
      this.getY()+offsetY);
  }
  
  /**
   * break out of the special state
   */
  private void breakSpecialState() {
    this.inSpecialState = false;
    this.resetXMovement();
  }

  /**
   * takes the damage 
   * @param dir the direction the attack is coming from
   * @param damage the amount of damage dealt to this player
   */
  public void takeDamage(int dir, int damage) {
    this.damageTaken += damage;
    this.setSpecialState("knockedBack", 300);
    this.yVel = Util.scaleY(-1.0)*damage-(this.damageTaken/30);
    this.dir = dir;
    this.xTargetVel = Util.scaleX(1.0)*damage+(this.damageTaken/20);
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
      this.xTargetVel = this.xMaxVel;
    }
  }

  /**
   * change the direction and target vel to move right
   */
  public void moveRight() {
    if (!this.state.equals("onRightWall")) {
      this.dir = 1;
      this.xTargetVel = this.xMaxVel;
    }
  }

  /**
   * reset the x vel and x target vel
   */
  public void resetXMovement() {
    this.xVel = 0;
    this.xTargetVel = 0;
  }

  /**
   * reset the number of jumps to zero
   */
  public void resetJumps() {
    this.numJumps = 0;
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
    if (Math.abs(this.xVel) < this.xTargetVel) {
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
    this.y += this.yVel;
  }

  /**
   * updates certain state functions when in certain states
   */
  public void updateStateFunction() {
    if (this.state.equals("lightAttack")) {
      this.updateLightAttack();
    }
  }

  /**
   * allow the player to drop down faster
   */
  public void dropDown() {
    this.setState("drop");
    this.yVel += this.dropVel;
  }

  public void dodge() {

  }

  public void throwItem() {

  }

  public void pickUp(Item[] items) {

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

  //testing for now
  //dunno how im gonna do the animation 
  //but that's for later :D
  /**
   * display the character to the panel
   * @param panel the JPanel to display to
   * @param g2d the graphics manager
   */
  public void display(JPanel panel, Graphics2D g2d, Hero[] players) {
    int[] pos = {(int)(this.x), (int)(this.y), this.width, this.height};
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
  public void displayAttackHitbox(Graphics2D g2d, Hero[] players) {
    this.attackHitbox.display(g2d, players);
  }


  /**
   * throws runtime exception if the state is an invalid state
   * @param state the state to check for
   */
  private void checkValidState(String state) {
    if (!States.all.contains(state)) {
      throw new RuntimeException(state + " is not a valid state");
    }
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


  /**
   * @return power int, how much damage the player deals
   */
  public int getPower() {
    return this.power;
  }

  @Override
  /**
   * @return int return the x
   */
  public int getX() {
    return (int)(this.x+this.width/2);
  }

  @Override
  /**
   * @return int return the y
   */
  public int getY() {
    return (int)(this.y+this.height/2);
  }

  @Override
  /**
   * @return int, return the radius
   */
  public int getRadius() {
    return this.radius;
  }

  @Override
  /**
   * @return int, the width of the image
   */
  public int getWidth() {
    return this.width;
  }

  @Override
  /**
   * @return int, the height of the image
   */
  public int getHeight() {
    return this.height;
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
    checkValidState(state);
    this.sprites.put(state, images);
  }

  /**
   * Sets the current state of the player for displaying
   * Such as facing left/right, attack, dodge, etc
   * @param state the state to set the player tospecialStateOver
   */
  public void setState(String state) {
    checkValidState(state);
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
   * @return boolean, whether the current state is an attack state 
   */
  public boolean isAttackState() {
    return States.attack.contains(this.state);
  }

  /**
   * @return boolean, whether the player is in a special state
   */
  public boolean inSpecialState() {
    return this.inSpecialState;
  }

  /**
   * @return the player's attack hitbox
   */
  public Hitbox getAttackHitbox() {
    return this.attackHitbox;
  }

  /**
   * @return whether the player is damagable at the moment
   */
  public boolean damagable() {
    return !this.state.equals("knockedBack");
  }

}