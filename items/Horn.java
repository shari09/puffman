package items;

import java.io.IOException;

import util.Util;

/**
 * [Horn.java]
 * A horn is a gadget that can summon a fireball above the player who uses it.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public class Horn extends Gadget {
  public static final int WIDTH = Util.scaleX(90);
  public static final int HEIGHT = Util.scaleY(60);

  /**
   * Constructor.
   * @param pos [x, y], the position of where to spawn the horn.
   * @throws IOException
   */
  public Horn(int[] pos) throws IOException {
    super(pos[0], pos[1],
          Horn.WIDTH, Horn.HEIGHT,
          Util.urlToImage("gadgets/fireHorn.png"),
          2500);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DamagableItemSpawns use(int x, int y, int dir) {
    return new HornSpawns(x, y, dir);
  }
}