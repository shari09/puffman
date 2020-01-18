package items;


import java.awt.image.BufferedImage;

/**
 * [Gadget.java]
 * A gadget is an item which the player can use by pressing the light
 * attack key.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public abstract class Gadget extends Item {

  /**
   * Constructor.
   * @param x the x coordinate of where the gadget spawns at.
   * @param y the y coordinate of where the gadget spawns at.
   * @param width the width of the gadget.
   * @param height the height of the gadget.
   * @param sprite the sprite of the gadget.
   * @param disappearingTime how long it takes for the gadget to disappear 
   *                         after it's thrown and hits the ground.
   */
  public Gadget(int x, int y, int width, int height, 
                BufferedImage sprite,
                int disappearingTime) {
    super(x, y, width, height, sprite, disappearingTime);
  }

  /**
   * Use the gadget. Some action will happen and a series of 
   * hurtboxes will spawn in the world that can damage all players
   * including the player who used the gadget.
   * @param x the x position of the player.
   * @param y the y position of the player.
   * @param dir the direction the player is facing.
   * @return DamagableItemSpawns, a series of hurtboxes for what the gadget spawns.
   */
  public abstract DamagableItemSpawns use(int x, int y, int dir);

}