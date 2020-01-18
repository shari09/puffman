package weapons;

import java.io.IOException;

import util.Util;

/**
 * [Hammer.java]
 * The hammer weapon.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public class Hammer extends PickupableWeaponHolder {
  public static final int WIDTH = Util.scaleX(100);
  public static final int HEIGHT = Util.scaleY(60);

  /**
   * Constructor.
   * @param pos [x, y], the position of where the hammer is spawned at.
   * @throws IOException
   */
  public Hammer(int[] pos) throws IOException {
    super(pos[0], pos[1], 
          Hammer.WIDTH, Hammer.HEIGHT, 
          Util.urlToImage("weapons/hammer.png"),
          "hammer");
  }
} 