package weapons;

import java.io.IOException;

import util.Util;

public class Hammer extends PickupableWeaponHolder {
  public static final int WIDTH = Util.scaleX(100);
  public static final int HEIGHT = Util.scaleY(60);

  //pos is [x, y]
  public Hammer(int[] pos) throws IOException {
    super(pos[0], pos[1], 
          Hammer.WIDTH, Hammer.HEIGHT, 
          Util.urlToImage("weapons/weapon.png"),
          "hammer");
  }

  @Override
  public String getStates() {
    return "hammer";
  }
} 