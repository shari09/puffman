package items;

import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;

import characters.Hero;
import util.Zoom;
import world.World;
import util.RectCollidable;
import util.Timer;
import util.Util;

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

  public void display(JPanel panel, Graphics2D g2d, Hero[] players) {
    int[] pos = {(int)(this.x), 
                 (int)(this.y), 
                 this.width, this.height};
    int[] newPos = Zoom.getDisplayPos(g2d, players, pos);
    g2d.drawImage(this.sprite,
                  newPos[0], newPos[1], newPos[2], newPos[3],
                  panel);
  }

  public void hitGround() {
    this.setState(Item.DISAPPEARING);
  }

  public void hitWall() {
    this.xVel = -this.xVel;
  }

  public void hitPlayer() {
    this.xVel = -this.xVel;
  }

  public abstract void use();


  ///getters/setters

  public int getState() {
    return this.state;
  }

  public void setState(int state) {
    this.state = state;
    if (this.state == Item.DISAPPEARING) {
      Timer.setTimeout(() -> this.alive = false, 
                       this.disappearingTime);
    }
  }


  public boolean isAlive() {
    if (this.x < 0 || this.x > World.mapWidth
        || this.y < 0 || this.y > World.mapHeight) {
      return false;
    }
    return this.alive;
  }

  public void setX(double x) {
    this.x = x-this.width/2;
  }

  public void setY(double y) {
    this.y = y-this.height/2;
  }

  public void moveX(double x) {
    this.x += x;
  }

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

  public void setNonDamagablePlayer(Hero player) {
    this.nonDamagablePlayer = player;
  }

  public Hero getNonDamagablePlayer() {
    return this.nonDamagablePlayer;
  }

  public void setYVel(double yVel) {
    this.yVel = yVel;
  }

  public double getYVel() {
    return this.yVel;
  }

  public void setXVel(double xVel) {
    this.xVel = xVel;
  }

  public double getXVel() {
    return this.xVel;
  }
}