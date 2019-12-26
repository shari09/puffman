package characters;

import java.util.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.*;

import items.*;
import blocks.*;
import util.*;
import world.*;

public abstract class Hero implements CircleCollidable, RectCollidable {

  private final Color hitboxColour = new Color(200, 0, 0, 60);
  
  //controls
  // private String leftKey;
  // private String rightKey;
  // private String jumpKey;
  // private String lightAttackKey;


  private double x;
  private double y;
  private int width;
  private int height;
  private int radius;


  private double xVel;
  private double xTargetVel;
  private final double acceleration = Util.scaleX(2.0);
  private final double jumpVel = Util.scaleY(-11.0);
  private final double xMaxVel = Util.scaleX(8.0);
  private final double dropVel = Util.scaleY(3.0);
  private int dir;
  private double yVel;
  private int numJumps;

  //counters
  private int spriteNum;
  private final int framesPerSprite = 10;
  private int curSpriteFrame;

  //boolean flags
  private boolean inSpecialState = false;

  private int lightAttackPower = 10;
  private int heavyAttackPower = 30;
  private int power;
  private int stungTime;
  private int attackRecoveryTime;
  private int nextWeapon;
  private int damageTaken;

  //weapons hitbox can override the player default hitbox
  private Hitbox attackHitbox;
  private HashSet<String> attackStates = new HashSet<>();

  //states: onLeftWall, onRightWall, faceRight, faceLeft, etc
  private String state;

  
  //special states count as you do something once,
  //it runs for a duration of time
  private HashSet<String> specialState = new HashSet<>();
  private HashMap<String, BufferedImage[]> sprites = new HashMap<>();
  private Item curItem;
  

  /**
   * initialize the hashable values
   */
  private void initValues() {
    this.specialState.add("lightAttack");
    this.specialState.add("jumpFromWall");
    this.specialState.add("knockedBack");

    this.attackStates.add("lightAttack");
  }

  //constructor
  public Hero(int x, int y, int width, int height, int hitboxRadius) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.radius = hitboxRadius;
    this.initValues();
  }

  //this is making a new obj each time
  //hmmmmmm
  /**
   * Sets the state to light attack state
   * Creates a attack hitbox for the player
   */
  public void lightAttack() {
    this.setState("lightAttack");
    this.xVel = 0;
    this.xTargetVel = this.xMaxVel/2;
    this.attackHitbox = new CircleHitbox(
      (int)(this.getX()+this.dir*Util.scaleX(40)),
      this.getY(),
      Math.min(Util.scaleX(10), Util.scaleY(10)));
    this.power = this.lightAttackPower;
  }

  /**
   * update the hitbox position for the light attack
   */
  private void updateLightAttack() {
    this.attackHitbox.adjustPos(
      (int)(this.getX()+this.dir*Util.scaleX(40)), this.getY());
  }
  

  /**
   * takes the damage 
   * @param dir the direction the attack is coming from
   * @param damage the amount of damage dealt to this player
   */
  public void takeDamage(int dir, int damage) {
    this.damageTaken += damage;
    this.setState("knockedBack");
  }

  /**
   * make the player jump if they are still allowed to jump
   */
  public void jump() {
    if (this.numJumps < 3) {
      if (this.state.equals("onLeftWall")) {
        this.moveRight();
        this.setState("jumpFromWall");
      } else if (this.state.equals("onRightWall")) {
        this.moveLeft();
        this.setState("jumpFromWall");
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
        //if it's a special state
        if (this.specialState.contains(this.state)) {
          this.inSpecialState = false;
          this.resetXMovement();
        }
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
      this.yVel -= World.GRAVITY/1.5;
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
        || this.x - this.radius/2 > GameWindow.width
        || this.y + this.radius < 0
        || this.y - this.radius > GameWindow.height) {
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
  public void display(JPanel panel, Graphics2D g2d) {
    g2d.drawImage(this.sprites.get(this.state)[this.spriteNum],
                  (int)(this.x), (int)(this.y),
                  this.width, this.height,
                  panel);
  }

  /**
   * display the player hitbox
   * that allows the player to be hit
   * @param g2d the graphics manager
   */
  public void displayHitbox(Graphics2D g2d) {
    g2d.setColor(this.hitboxColour);
    g2d.fill(new Ellipse2D.Double(this.x, this.y, 
                                  this.radius*2, this.radius*2));
  }

  /**
   * display the hitbox for player attacks
   * @param g2d the graphics manager
   */
  public void displayAttackHitbox(Graphics2D g2d) {
    this.attackHitbox.display(g2d);
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

  /**
   * @return int return the x
   */
  public int getX() {
    return (int)(this.x+this.width/2);
  }

  /**
   * @return int return the y
   */
  public int getY() {
    return (int)(this.y+this.height/2);
  }

  /**
   * @return int, return the radius
   */
  public int getRadius() {
    return this.radius;
  }

  /**
   * @return int, the width of the image
   */
  public int getWidth() {
    return this.width;
  }

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
  public void addSprite(String type, BufferedImage[] images) {
    this.sprites.put(type, images);
  }

  /**
   * Sets the current state of the player for displaying
   * Such as facing left/right, attack, dodge, etc
   * @param state the state to set the player tospecialStateOver
   */
  public void setState(String state) {
    if (!this.specialState.contains(this.state)
        || !this.inSpecialState) {
      this.spriteNum = 0;
      this.curSpriteFrame = 0;
      this.state = state;
      if (this.specialState.contains(state)) {
        this.inSpecialState = true;
      }

    }
    
  }

  /**
   * @return boolean, whether the current state is an attack state 
   */
  public boolean isAttackState() {
    return this.attackStates.contains(this.state);
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


}