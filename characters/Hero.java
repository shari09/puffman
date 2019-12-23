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

public abstract class Hero implements CircleCollidable {

  private final Color hitboxColour = new Color(200, 0, 0, 60);
  private int x;
  private int y;
  private int width;
  private int height;
  private int radius;


  // private int downAcceleration;
  private int xVel;
  private final int acceleration = 1;
  private final int xMaxVel = 5;
  private int dir;
  private int yVel;
  private int yMaxVel;
  private int numJumps;
  private int direction;

  private int lightAttackPower;
  private int heavyAttackPower;
  private int stungTime;
  private int attackLoadingTime;
  private int attackRecoveryTime;
  private int nextWeapon;
  private int damageTaken;

  private HashMap<String, Integer> attackHitBox = new HashMap<>();
  private String state;
  private HashMap<String, BufferedImage[]> sprites = new HashMap<>();
  private Object curItem;
  
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

  public void jump() {

  }

  /**
   * resets the y-velocity to 0
   */
  public void resetYVel() {
    this.yVel = 0;
  }

  /**
   * falling because of gravity
   * freefall acceleration
   */
  public void fall() {
    this.yVel += World.GRAVITY;
  }

  public void moveLeft() {
    this.dir = -1;
  }

  public void moveRight() {
    this.dir = 1;
  }

  public void resetXMovement() {
    // System.out.println("released");
    this.xVel = 0;
    this.dir = 0;
  }

  /**
   * update all movements (left, right, up, down)
   */
  //the logic is kinda messy here, come back later to fix it
  public void updateMovement() {
    if (Math.abs(this.xVel) < this.xMaxVel && this.dir != 0) {
      this.xVel += this.acceleration*this.dir;
    }

    this.x += this.xVel;
    this.y += this.yVel;
  }

  public void dropDown() {

  }

  public void dodge() {

  }

  public void collide(Object thing) {

  }

  public void attackCollide(Hero player) {

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
                  this.x, this.y,
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
    return this.x+this.width/2;
  }

  /**
   * @return int return the y
   */
  public int getY() {
    return this.y+this.height/2;
  }

  /**
   * @return int, return the radius
   */
  public int getRadius() {
    return this.radius;
  }

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