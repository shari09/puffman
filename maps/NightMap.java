package maps;

import java.awt.image.BufferedImage;
import java.io.IOException;

import blocks.RectBlock;
import util.Util;
import world.World;

/**
 * [NightMap.java]
 * The Night map of the game.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public class NightMap implements Map {

  /**
   * {@inheritDoc}
   */
  @Override
  public BufferedImage getBackground() throws IOException {
    return Util.urlToImage("background/night.jpg");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RectBlock[] getBlocks() throws IOException {
    RectBlock[] blocks = new RectBlock[3];
    blocks[0] = new RectBlock(
      "blocks/test.jpg", 
      World.MAP_WIDTH/2 - Util.scaleX(600),
      World.MAP_HEIGHT/2,
      Util.scaleX(500), Util.scaleY(350)
    );
    blocks[1] = new RectBlock(
      "blocks/test.jpg", 
      World.MAP_WIDTH/2 + Util.scaleX(100),
      World.MAP_HEIGHT/2 + Util.scaleY(150),
      Util.scaleX(500), Util.scaleY(350)
    );
    blocks[2] = new RectBlock(
      "blocks/test.jpg", 
      World.MAP_WIDTH/2 + Util.scaleX(200),
      World.MAP_HEIGHT/2 - Util.scaleY(300),
      Util.scaleX(300), Util.scaleY(40)
    );

    return blocks;
  }
}