package items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import characters.Hero;
import util.RectCollidable;
import util.Timer;
import util.Util;
import util.Zoom;
import world.World;

public abstract class Item implements RectCollidable {
  public static final int SPAWNED = 0;
  public static final int ON_PLAYER = 1;
  public static final int THROWING = 2;
  public static final int DISAPPEARING = 3;

  public static final int X_SPEED = Util.scaleX(20);

  private double x;
  private double y;
  private int width;
  private int height;

  private BufferedImage sprite;
  private int state = 0;
  private int disappearingTime;
  private boolean alive = true;
  private Hero nonDamagablePlayer;

  private int thrownDamage = 5;
  private int thrownDelay = 600;
  private int thrownKnockbackX = Util.scaleX(10);
  private int thrownKnockbackY = Util.scaleY(-5);

  private String disappearingID;

  private double yVel;
  private double xVel;

  public Item(int x, int y, int width, int height, 
              BufferedImage sprite,
              int disappearingTime) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.sprite = sprite;
    this.disappearingTime = disappearingTime;
  }

  /**
   * displays the item
   * @param panel the JPanel for displaying everything
   * @param g2d the graphics2d manager
   * @param players the players used for scaling purposes
   */
  public void display(JPanel panel, Graphics2D g2d, Hero[] players) {
    int[] pos = {(int)(this.x), 
                 (int)(this.y), 
                 this.width, this.height};
    int[] newPos = Zoom.getDisplayPos(g2d, players, pos);
    g2d.drawImage(this.sprite,
                  newPos[0], newPos[1], newPos[2], newPos[3],
                  panel);
  }

  /**
   * if the item hits the ground after being thrown,
   * it's set to the countdown to disappear state 
   */
  public void hitGround() {
    this.setState(Item.DISAPPEARING);
  }

  /**
   * if the item hits a wall, the x velocity is reversed, meaning
   * it bounces off the wall and goes the other way
   */
  public void hitWall() {
    this.xVel = -this.xVel;
  }

  /**
   * if the item hits the player, it deals damage and knockback to the player
   * the item also bounces off the player at half the speed
   * @param player the player that was hit
   */
  public void hitPlayer(Hero player) {
    player.takeDamage(this.thrownDamage);
    player.setDir((int)(this.xVel/Math.abs(this.xVel)));
    player.setSpecialState("knockedBack", 
                           this.thrownDelay+player.getDamageTaken()/10);
    player.setxTargetSpeed(this.thrownKnockbackX);
    player.setYVel(this.thrownKnockbackY);
    this.xVel = -this.xVel/2;
  }


  ///getters/setters

  /**
   * get the current state of the item
   * @return state int, the current state of the item
   */
  public int getState() {
    return this.state;
  }

  /**
   * sets the state of the item
   * @param state the state to set this item to
   */
  public void setState(int state) {
    this.state = state;
    if (this.state == Item.DISAPPEARING) {
      this.disappearingID = Timer.setTimeout(() -> this.alive = false, 
                                             this.disappearingTime);
    }
  }

  /**
   * resets the item once the player picks it back up again
   */
  public void reset() {
    if (this.disappearingID != null) {
      Timer.clearTimeout(this.disappearingID);
      this.disappearingID = null;
      this.xVel = 0;
      this.yVel = 0;
    }
    
  }

  /**
   * @return boolean, whether or not the item is "alive" (not disappeared)
   */
  public boolean isAlive() {
    if (this.x < 0 || this.x > World.mapWidth
        || this.y < 0 || this.y > World.mapHeight) {
      return false;
    }
    return this.alive;
  }

  /**
   * sets the x-pos of the item
   * @param x the x-pos to set to
   */
  public void setX(double x) {
    this.x = x-this.width/2;
  }

  /**
   * sets the y-pos of the item
   * @param y the y-pos to set to
   */
  public void setY(double y) {
    this.y = y-this.height/2;
  }

  /**
   * move the x-pos of the item
   * @param x the x pixels to increase
   */
  public void moveX(double x) {
    this.x += x;
  }

  /**
   * move the y-pos of the item
   * @param y the y pixels to increase
   */
  public void moveY(double y) {
    this.y += y;
  }

  @Override
  public int getX() {
    return (int)(this.x + this.width/2);
  }

  @Override
  public int getY() {
    return (int)(this.y + this.height/2);
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
   * set the non damagable player, 
   * the player who cannot be damaged by this item (player who threw it)
   * @param player the non damagable player
   */
  public void setNonDamagablePlayer(Hero player) {
    this.nonDamagablePlayer = player;
  }

  /**
   * get the non damagable player (the player who threw it)
   * @return the non damagable player
   */
  public Hero getNonDamagablePlayer() {
    return this.nonDamagablePlayer;
  }

  /**
   * sets the y velocity of the item
   * @param yVel the y velocity to set to
   */
  public void setYVel(double yVel) {
    this.yVel = yVel;
  }

  /**
   * get the y velocity of the item
   * @return yVel double, the y velocity of the item
   */
  public double getYVel() {
    return this.yVel;
  }

  /**
   * sets the x velocity for the item
   * @param xVel the x velocity to set to
   */
  public void setXVel(double xVel) {
    this.xVel = xVel;
  }

  /**
   * get the x velocity of the item
   * @return xVel double, the x velocity of the item
   */
  public double getXVel() {
    return this.xVel;
  }
}