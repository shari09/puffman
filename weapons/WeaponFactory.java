package weapons;

import java.io.IOException;

public class WeaponFactory {
  public static Weapon getWeapon(String weapon) throws IOException {
    if (weapon.equals("fist")) {
      return new CloseRange("assets/config/fist-attack.txt");
    }

    return null;
  }
}