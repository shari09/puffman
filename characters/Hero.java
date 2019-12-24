package characters;

import java.util.*;
import javax.swing.*;
// import javax.imageio.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.*;

import items.*;
import blocks.*;
import util.*;
import world.*;

public abstract class Hero implements CircleCollidable, RectCollidable {

  private final Color hitboxColour = new Color(200, 0, 0, 60);
  private double x;
  private double y;
  private int width;
  private int height;
  private int radius;


  // private int downAcceleration;
  private double xVel;
  private final double acceleration = Util.scaleX(2.0);
  private final double jumpVel = Util.scaleY(-11.0);
  private final double xMaxVel = Util.scaleX(8.0);
  private final double dropVel = Util.scaleY(5.0);
  private int dir;
  private double yVel;
  private int yMaxVel;
  private int numJumps;
  private int direction;

  //boolean flags
  private boolean jumpFromWall = false;

  private int lightAttackPower;
  private int heavyAttackPower;
  private int stungTime;
  private int attackLoadingTime;
  private int attackRecoveryTime;
  private int nextWeapon;
  private int damageTaken;

  private HashMap<String, Integer> attackHitBox = new HashMap<>();
  //states: onWall, faceRight
  private String state;
  private HashMap<String, BufferedImage[]> sprites = new HashMap<>();
  private Object curItem;
  
  //constructor
  public Hero(int x, int y, int width, int height, int hitboxRadius) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.radius = hitboxRadius;
  }

  public int getLightAttackPower() {
    return this.lightAttackPower;
  }

  public int getHeavyAttackPower() {
    return this.heavyAttackPower;
  }

  public void takeDamage() {

  }

  /**
   * make the player jump if they are still allowed to jump
   */
  public void jump() {
    if (this.numJumps < 3) {
      if (this.state.equals("onLeftWall")) {
        this.dir = 1;
        this.jumpFromWall = true;
      } else if (this.state.equals("onRightWall")) {
        this.dir = -1;
        this.jumpFromWall = true;
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

  public void moveLeft() {
    this.dir = -1;
  }

  public void moveRight() {
    this.dir = 1;
  }

  public void resetXMovement() {
    this.xVel = 0;
    this.dir = 0;
  }

  public void resetJumps() {
    this.numJumps = 0;
  }

  /**
   * update all movements (left, right, up, down)
   */
  //the logic is kinda messy here, come back later to fix it
  public void updateMovement() {
    if (Math.abs(this.xVel) < this.xMaxVel && this.dir != 0) {
      this.xVel += this.acceleration*this.dir;
    }

    //reset x to 0 after bounce is complete
    if (this.jumpFromWall && Math.abs(this.xVel) == this.xMaxVel) {
      this.resetXMovement();
      this.jumpFromWall = false;
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

  public void dropDown() {
    this.yVel += this.dropVel;
  }

  public void dodge() {

  }

  public void throwItem() {

  }

  public void pickUp(Item[] items) {

  }

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
  public void display(JPanel panel, Graphics2D g2d) {
    g2d.drawImage(this.sprites.get(this.state)[0],
                  (int)(this.x), (int)(this.y),
                  this.width, this.height,
                  panel);
    
  }

  public void displayHitbox(Graphics2D g2d) {
    g2d.setColor(this.hitboxColour);
    g2d.fill(new Ellipse2D.Double(this.x, this.y, 
                                  this.radius*2, this.radius*2));
  }


  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //setters and getters

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
   * @param state
   */
  public void setState(String state) {
    this.state = state;
  }
}