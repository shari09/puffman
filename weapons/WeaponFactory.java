package weapons;

import java.io.IOException;

/**
 * [WeaponFactory.java]
 * The factory that is used to create weapons.
 * So far, there are only two available weapons.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public class WeaponFactory {
  /**
   * Get the requested weapon.
   * @param weapon the weapon name (also matches with the data file names).
   * @return Weapon, the weapon that is being created.
   *         It's null if the weapon name doesn't match with any weapons.
   * @throws IOException
   */
  public static Weapon getWeapon(String weapon) throws IOException {

    if (weapon.equals("fist") || weapon.equals("hammer")) {
      return new CloseRangedWeapon("assets/config/weapons/" + weapon + ".txt");
    }
    return null;
  }
}