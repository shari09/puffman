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

  public Color getColour(boolean active) {
    if (active) {
      return Hurtbox.ACTIVE_COLOUR;
    }
    return Hurtbox.INACTIVE_COLOUR;
  }

  public void setPos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void setSize(int radius) {
    this.radius = radius;
  }


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