package weapons;

import java.awt.image.BufferedImage;
import java.io.IOException;

import items.Item;

/**
 * [PickupableWeaponHolder.java]
 * It behaves like a item where it is randomly spawned in the world.
 * The player is able to pick them up and throw them away.
 * However, once a player picks up the weapon, the player no longer uses
 * their fist for the light/heavy attacks, and instead switches to the 
 * weapon's data sheet.
 * 
 * 2020-01-17
 * @version 0.0.2
 * @author Shari Sun
 */
public abstract class PickupableWeaponHolder extends Item {
  private Weapon weapon;
  
  /**
   * Constructor.
   * @param x the x position of where to spawn the weapon.
   * @param y the y position of where to spawn the weapon.
   * @param width the width of the weapon image.
   * @param height the height of the weapon image.
   * @param sprite the sprite of the weapon.
   * @param weaponName the name of the weapon, used for the weapon factory.
   * @throws IOException
   */
  public PickupableWeaponHolder(int x, int y, int width, int height, 
                                BufferedImage sprite, 
                                String weaponName) throws IOException {
    super(x, y, width, height, sprite, 1500);
    this.weapon = WeaponFactory.getWeapon(weaponName);
  }

  /**
   * Get the corresponding weapon (it is a weapon image right now)
   * to equip on the player.
   * @return Weapon, the weapon.
   */
  public Weapon getWeapon() {
    return this.weapon;
  }
}