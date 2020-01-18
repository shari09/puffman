package items;

import java.io.IOException;

import util.Util;

/**
 * [SpikeBall.java]
 * A gadget that can summon a row of spikes in front of the player.
 * 
 * 2020-01-17
 * @version 0.0.2
 * @author Shari Sun
 */
public class SpikeBall extends Gadget {
  public static final int WIDTH = Util.scaleX(50);
  public static final int HEIGHT = SpikeBall.WIDTH;

  /**
   * Constructor.
   * @param pos [x, y], the position of where to spawn the spike ball.
   * @throws IOException
   */
  public SpikeBall(int[] pos) throws IOException {
    super(pos[0], pos[1],
          SpikeBall.WIDTH, SpikeBall.HEIGHT,
          Util.urlToImage("gadgets/spikeBall.png"),
          2000);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DamagableItemSpawns use(int x, int y, int dir) {
    return new SpikeSpawns(x, y, dir);
  }

}