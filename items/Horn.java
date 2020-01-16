package items;

import java.io.IOException;

import util.Util;

public class Horn extends Gadget {
  public static final int WIDTH = Util.scaleX(90);
  public static final int HEIGHT = Util.scaleY(60);

  public Horn(int[] pos) throws IOException {
    super(pos[0], pos[1],
          Horn.WIDTH, Horn.HEIGHT,
          Util.urlToImage("gadgets/fireHorn.png"),
          2500);
  }

  @Override
  public DamagableItemSpawns use(int x, int y, int dir) {
    return new HornSpawns(x, y, dir);
  }
}