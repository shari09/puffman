package util;

import java.awt.*;
import java.awt.geom.*;

import characters.Hero;

public class Hurtbox implements CircleCollidable {

  private static final Color INACTIVE_COLOUR = new Color(10, 10, 10, 30);
  private static final Color ACTIVE_COLOUR = new Color(200, 100, 100);
  private int x;
  private int y;
  private int radius;
  private int offsetX = 0;
  private int offsetY = 0;

  public Hurtbox() {};
  
  public Hurtbox(int x, int y, int radius) {
    this.x = x;
    this.y = y;
    this.radius = radius;
  }

  @Override
  public int getX() {
    return this.x;
  }

  @Override
  public int getY() {
    return this.y;
  }

  @Override
  public int getRadius() {
    return this.radius;
  }

  /**
   * get the colour of the hurtbox
   * red - active colour, where the weapon can deal damage
   * transparent gray - inactive colour, where the weapon is
   * either in loading or recovery state and cannot deal any damage
   * @param active
   * @return
   */
  public Color getColour(boolean active) {
    if (active) {
      return Hurtbox.ACTIVE_COLOUR;
    }
    return Hurtbox.INACTIVE_COLOUR;
  }

  /**
   * sets the position for the hurtbox
   * @param x the x-pos
   * @param y the y-pos
   */
  public void setPos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * sets the size for the hurtbox
   * @param radius the radius of the hurtbox
   */
  public void setSize(int radius) {
    this.radius = radius;
  }

  /**
   * sets the x and y offset for the hurtbox
   * @param offsetX the x-offset
   * @param offsetY the y-offset
   */
  public void setOffset(int offsetX, int offsetY) {
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    //update the hurtbox position with the offsets
    this.x += this.offsetX;
    this.y += this.offsetY;
  }

  /**
   * reset all the offsets to zero
   */
  public void resetOffset() {
    this.offsetX = 0;
    this.offsetY = 0;
  }

  /**
   * get the current x-offset
   * @return offsetX int
   */
  public int getOffsetX() {
    return this.offsetX;
  }

  /**
   * get the current y-offset
   * @return offsetY int
   */
  public int getOffsetY() {
    return this.offsetY;
  }

  /**
   * display the hurtbox to the screen
   * @param g2d the graphics class
   * @param players the players (need their position for scaling the screen)
   * @param active whether the hurtbox is active or not (able to deal damage)
   */
  public void display(Graphics2D g2d, Hero[] players, boolean active) {
    g2d.setColor(this.getColour(active));
    int[] pos = {this.getX()-this.radius, 
                 this.getY()-this.radius, 
                 this.radius*2, this.radius*2};
    int[] newPos = Zoom.getDisplayPos(g2d, players, pos);
    g2d.fill(new Ellipse2D.Double(newPos[0], newPos[1], 
                                  newPos[2], newPos[3]));
  }
}