package items;

import java.io.IOException;

import util.Util;

/**
 * [Bomb.java]
 * Making a bomb gadget. The bomb has an exploding effect if used.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */

public class Bomb extends Gadget {
  public static final int WIDTH = Util.scaleX(60);
  public static final int HEIGHT = Bomb.WIDTH;

  /**
   * Constructor.
   * @param pos [x, y], the position of the bomb being spawned in the world.
   * @throws IOException
   */
  public Bomb(int[] pos) throws IOException {
    super(pos[0], pos[1],
          Bomb.WIDTH, Bomb.HEIGHT,
          Util.urlToImage("gadgets/bomb.png"),
          2000);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DamagableItemSpawns use(int x, int y, int dir) {
    return new BombSpawns(x, y, dir);
  }
}