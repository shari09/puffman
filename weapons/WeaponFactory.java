package weapons;

import java.io.IOException;

public class WeaponFactory {
  public static Weapon getWeapon(String weapon) throws IOException {

    if (weapon.equals("fist") || weapon.equals("hammer")) {
      return new CloseRange("assets/config/weapons/" + weapon + ".txt");
    }
    return null;
  }
}