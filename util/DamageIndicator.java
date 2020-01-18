package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 * [DamageIndicator.java]
 * The damage indicator for the player.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public class DamageIndicator {
  public static final int RADIUS = Util.scaleX(50);
  private static final float H = 0.35f;
  private static final float S = 0.98f;
  private static final float B = 0.94f;

  private int x;
  private int y;

  /**
   * Constructor.
   * @param x the x position of where it should display on the screen.
   * @param y the y position of where it should display on the screen.
   */
  public DamageIndicator(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Displays the damage indicator.
   * The colour changes according to how damaged the player is.
   * Green - not very damaged.
   * Red - very damaged.
   * @param g2d the graphics2d class/manager.
   * @param damage the amount of damage to be displayed.
   */
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