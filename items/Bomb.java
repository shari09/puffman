package items;

import java.io.IOException;

import util.Util;

public class Bomb extends Gadget {
  public static final int WIDTH = Util.scaleX(60);
  public static final int HEIGHT = Bomb.WIDTH;

  public Bomb(int[] pos) throws IOException {
    super(pos[0], pos[1],
          Bomb.WIDTH, Bomb.HEIGHT,
          Util.urlToImage("gadgets/bomb.png"),
          2000);
  }

  @Override
  public DamagableItemSpawns use(int x, int y, int dir) {
    return new BombSpawns(x, y, dir);
  }
}