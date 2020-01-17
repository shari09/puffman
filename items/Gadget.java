package items;


import java.awt.image.BufferedImage;

/**
 * Gadget.java
 * 
 */
public abstract class Gadget extends Item {

  public Gadget(int x, int y, int width, int height, 
                BufferedImage sprite,
                int disappearingTime) {
    super(x, y, width, height, sprite, disappearingTime);
  }

  public abstract DamagableItemSpawns use(int x, int y, int dir);

}