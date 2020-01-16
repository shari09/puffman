package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;


public class DamageIndicator {
  public static final int RADIUS = Util.scaleX(50);
  private static final float H = 0.35f;
  private static final float S = 0.98f;
  private static final float B = 0.94f;

  private int x;
  private int y;

  public DamageIndicator(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void display(Graphics2D g2d, int damage) {
    g2d.setColor(Color.getHSBColor(
      Math.max(DamageIndicator.H-damage/400f, 0),
      DamageIndicator.S,
      DamageIndicator.B));
    g2d.fill(new Ellipse2D.Double(
      this.x-DamageIndicator.RADIUS, 
      this.y-DamageIndicator.RADIUS, 
      DamageIndicator.RADIUS*2, DamageIndicator.RADIUS*2));
  }
}