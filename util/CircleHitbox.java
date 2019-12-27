package util;

import java.awt.*;
import java.awt.geom.*;
import characters.*;

public class CircleHitbox extends Hitbox implements CircleCollidable {

  private int radius;

  public CircleHitbox(int x, int y, int radius) {
    super(x, y);
    this.radius = radius;
  }

  @Override
  public int getRadius() {
    return this.radius;
  }

  @Override
  public void display(Graphics2D g2d, Hero[] players) {
    g2d.setColor(this.getColour());
    int[] pos = {this.getX()-this.radius, 
                 this.getY()-this.radius, 
                 this.radius*2, this.radius*2};
    int[] newPos = Zoom.getDisplayPos(g2d, players, pos);
    g2d.fill(new Ellipse2D.Double(newPos[0], newPos[1], 
                                  newPos[2], newPos[3]));
  }
}