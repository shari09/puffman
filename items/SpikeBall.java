package items;

import java.io.IOException;

import util.Util;

public class SpikeBall extends Gadget {
  public static final int WIDTH = Util.scaleX(50);
  public static final int HEIGHT = SpikeBall.WIDTH;

  public SpikeBall(int[] pos) throws IOException {
    super(pos[0], pos[1],
          SpikeBall.WIDTH, SpikeBall.HEIGHT,
          Util.urlToImage("gadgets/spikeBall.png"),
          2000);
  }

  @Override
  public void use() {
    System.out.println("useItem");
  }
}