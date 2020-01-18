package maps;

import java.awt.image.BufferedImage;
import java.io.IOException;

import blocks.RectBlock;
import util.Util;
import world.World;

/**
 * [ArenaMap.java]
 * The Arena map for the game.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public class ArenaMap implements Map {

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getBackground() throws IOException {
    return Util.urlToImage("background/arena.jpg");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RectBlock[] getBlocks() throws IOException {
    RectBlock[] blocks = new RectBlock[4];
    blocks[0] = new RectBlock(
      "blocks/test.jpg", 
      World.MAP_WIDTH/2 - Util.scaleX(500),
      World.MAP_HEIGHT/2,
      Util.scaleX(1000), Util.scaleY(50)
    );
    blocks[1] = new RectBlock(
      "blocks/test.jpg", 
      World.MAP_WIDTH/2 - Util.scaleX(700),
      World.MAP_HEIGHT/2 - Util.scaleY(400),
      Util.scaleX(50), Util.scaleY(400)
    );
    blocks[2] = new RectBlock(
      "blocks/test.jpg", 
      World.MAP_WIDTH/2 - Util.scaleX(200),
      World.MAP_HEIGHT/2 - Util.scaleY(300),
      Util.scaleX(400), Util.scaleY(40)
    );
    blocks[3] = new RectBlock(
      "blocks/test.jpg", 
      World.MAP_WIDTH/2 - Util.scaleX(900),
      World.MAP_HEIGHT/2 - Util.scaleY(400),
      Util.scaleX(220), Util.scaleY(50)
    );

    return blocks;
  }
}