package weapons;

import java.awt.image.BufferedImage;
import java.io.IOException;

import items.Item;

//is a weapon icon (item) and has a weapon (data sheet)
public abstract class PickupableWeaponHolder extends Item {
  private Weapon weapon;
  
  public PickupableWeaponHolder(int x, int y, int width, int height, 
                                BufferedImage sprite, 
                                String weaponName) throws IOException {
    super(x, y, width, height, sprite, 1500);
    this.weapon = WeaponFactory.getWeapon(weaponName);
  }

  public Weapon getWeapon() {
    return this.weapon;
  }

  public abstract String getStates();
}