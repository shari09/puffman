package util;

import java.awt.*;
import java.awt.geom.*;

public class RectHitbox extends Hitbox implements RectCollidable {

  private int width;
  private int height;

  public RectHitbox(int x, int y, int width, int height) {
    super(x, y);
    this.width = width;
    this.height = height;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public void display(Graphics2D g2d) {
    g2d.setColor(this.getColour());
    g2d.fill(new Rectangle2D.Double(this.getX()-this.width/2, 
                                    this.getY()-this.height/2, 
                                    this.width, this.height));
  }
}