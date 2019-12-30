package weapons;

import java.io.IOException;

public class WeaponFactory {
  public synchronized static Weapon getWeapon(String weapon) throws IOException {
    if (weapon.equals("fist")) {
      return new CloseRange("assets/config/fist.txt");
    } else if (weapon.equals("hammer")) {
      return new CloseRange("assets/config/hammer.txt");
    }

    return null;
  }
}