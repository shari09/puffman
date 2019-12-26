package util;

import java.awt.*;
import java.awt.geom.*;

public class CircleHitbox extends Hitbox implements CircleCollidable {

  private int radius;

  public CircleHitbox(int x, int y, int radius) {
    super(x, y);
    this.radius = radius;
  }

  public int getRadius() {
    return this.radius;
  }

  public void display(Graphics2D g2d) {
    g2d.setColor(this.getColour());
    g2d.fill(new Ellipse2D.Double(this.getX()-this.radius, 
                                  this.getY()-this.radius, 
                                  this.radius*2, this.radius*2));
  }
}