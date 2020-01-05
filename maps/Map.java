package maps;

import java.io.IOException;

import blocks.Block;
import blocks.RectBlock;
import world.World;
import util.Util;

public class Map {
  public static Block[] getMap(int num) throws IOException {
    if (num == 1) {
      return one();
    }
    return null;
  }

  private static RectBlock[] one() throws IOException {
    RectBlock[] blocks = new RectBlock[4];
    blocks[0] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 - Util.scaleX(500),
      World.mapHeight/2,
      Util.scaleX(1000), Util.scaleY(50)
    );
    blocks[1] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 - Util.scaleX(700),
      World.mapHeight/2 - Util.scaleY(400),
      Util.scaleX(50), Util.scaleY(400)
    );
    blocks[2] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 - Util.scaleX(200),
      World.mapHeight/2 - Util.scaleY(300),
      Util.scaleX(400), Util.scaleY(40)
    );
    blocks[3] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 - Util.scaleX(900),
      World.mapHeight/2 - Util.scaleY(400),
      Util.scaleX(220), Util.scaleY(50)
    );

    return blocks;
  }
}