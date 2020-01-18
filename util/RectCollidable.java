package util;

/**
 * [RectCollidable.java]
 * Rectangle collision interface.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public interface RectCollidable extends Collidable {
  /**
   * get the width
   * @return height int, the width
   */
  public int getWidth();

  /**
   * get the height
   * @return height int, the height
   */
  public int getHeight();
}