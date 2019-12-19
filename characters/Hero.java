package characters;

import java.util.*;
import javax.swing.*;
// import javax.imageio.*;
import java.awt.image.*;
import java.awt.*;

import items.*;
import blocks.*;
import util.*;
import world.*;

public abstract class Hero implements CircleCollidable {

  private int x;
  private int y;
  private int radius;

  private int downAcceleration;
  private int xVel;
  private int xMaxVel;
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

  private HashMap<String, Integer> attackHitBox;
  private String state;
  private HashMap<String, BufferedImage[]> sprites;
  private Object curItem;
  
  public Hero(int x, int y) {
    this.x = x;
    this.y = y;
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

  public void moveLeft() {

  }

  public void moveRight() {

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
                  this.x - this.radius/2, this.y - this.radius/2,
                  this.radius*2, this.radius*2,
                  panel);
  }



  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //setters and getters

  /**
   * @param x the x to set
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * @return int return the x
   */
  public int getX() {
    return this.x;
  }

  /**
   * @return int return the y
   */
  public int getY() {
    return this.y;
  }

  /**
   * @param y the y to set
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * @param radius the radius to set
   */
  public void setRadius(int radius) {
    this.radius = radius;
  }

  /**
   * @return int, return the radius
   */
  public int getRadius() {
    return this.radius;
  }

  /**
   * @return int return the downAcceleration
   */
  public int getDownAcceleration() {
      return this.downAcceleration;
  }

  /**
   * @param downAcceleration the downAcceleration to set
   */
  public void setDownAcceleration(int downAcceleration) {
      this.downAcceleration = downAcceleration;
  }

  /**
   * @return int return the xVel
   */
  public int getXVel() {
      return this.xVel;
  }

  /**
   * @param xVel the xVel to set
   */
  public void setXVel(int xVel) {
      this.xVel = xVel;
  }

  /**
   * @return int return the xMaxVel
   */
  public int getXMaxVel() {
      return this.xMaxVel;
  }

  /**
   * @param xMaxVel the xMaxVel to set
   */
  public void setXMaxVel(int xMaxVel) {
      this.xMaxVel = xMaxVel;
  }

  /**
   * @return int return the yVel
   */
  public int getYVel() {
      return this.yVel;
  }

  /**
   * @param yVel the yVel to set
   */
  public void setYVel(int yVel) {
      this.yVel = yVel;
  }

  /**
   * @return int return the yMaxVel
   */
  public int getYMaxVel() {
      return this.yMaxVel;
  }

  /**
   * @param yMaxVel the yMaxVel to set
   */
  public void setYMaxVel(int yMaxVel) {
      this.yMaxVel = yMaxVel;
  }

  /**
   * @return int return the numJumps
   */
  public int getNumJumps() {
      return this.numJumps;
  }

  /**
   * @param numJumps the numJumps to set
   */
  public void setNumJumps(int numJumps) {
      this.numJumps = numJumps;
  }

  /**
   * @return int return the direction
   */
  public int getDirection() {
      return this.direction;
  }

  /**
   * @param direction the direction to set
   */
  public void setDirection(int direction) {
      this.direction = direction;
  }

  /**
   * @param lightAttackPower the lightAttackPower to set
   */
  public void setLightAttackPower(int lightAttackPower) {
      this.lightAttackPower = lightAttackPower;
  }

  /**
   * @param heavyAttackPower the heavyAttackPower to set
   */
  public void setHeavyAttackPower(int heavyAttackPower) {
      this.heavyAttackPower = heavyAttackPower;
  }

  /**
   * @return int return the stungTime
   */
  public int getStungTime() {
      return this.stungTime;
  }

  /**
   * @param stungTime the stungTime to set
   */
  public void setStungTime(int stungTime) {
      this.stungTime = stungTime;
  }

  /**
   * @return int return the attackLoadingTime
   */
  public int getAttackLoadingTime() {
      return this.attackLoadingTime;
  }

  /**
   * @param attackLoadingTime the attackLoadingTime to set
   */
  public void setAttackLoadingTime(int attackLoadingTime) {
      this.attackLoadingTime = attackLoadingTime;
  }

  /**
   * @return int return the attackRecoveryTime
   */
  public int getAttackRecoveryTime() {
      return this.attackRecoveryTime;
  }

  /**
   * @param attackRecoveryTime the attackRecoveryTime to set
   */
  public void setAttackRecoveryTime(int attackRecoveryTime) {
      this.attackRecoveryTime = attackRecoveryTime;
  }

  /**
   * @return int return the nextWeapon
   */
  public int getNextWeapon() {
      return this.nextWeapon;
  }

  /**
   * @param nextWeapon the nextWeapon to set
   */
  public void setNextWeapon(int nextWeapon) {
      this.nextWeapon = nextWeapon;
  }

  /**
   * @return int return the damageTaken
   */
  public int getDamageTaken() {
      return this.damageTaken;
  }

  /**
   * @param damageTaken the damageTaken to set
   */
  public void setDamageTaken(int damageTaken) {
      this.damageTaken = damageTaken;
  }

  /**
   * @return HashMap<String, Integer> return the attackHitBox
   */
  public HashMap<String, Integer> getAttackHitBox() {
      return this.attackHitBox;
  }

  /**
   * @param attackHitBox the attackHitBox to set
   */
  public void setAttackHitBox(HashMap<String, Integer> attackHitBox) {
      this.attackHitBox = attackHitBox;
  }

  /**
   * @return HashMap<String, BufferedImage[]> return the sprites
   */
  public HashMap<String, BufferedImage[]> getSprites() {
      return this.sprites;
  }

  /**
   * @param sprites the sprites to set
   */
  public void setSprites(HashMap<String, BufferedImage[]> sprites) {
      this.sprites = sprites;
  }

  /**
   * @return Object return the curItem
   */
  public Object getCurItem() {
      return this.curItem;
  }

  /**
   * @param curItem the curItem to set
   */
  public void setCurItem(Object curItem) {
      this.curItem = curItem;
  }

}