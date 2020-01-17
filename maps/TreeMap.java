package maps;

import java.awt.image.BufferedImage;
import java.io.IOException;

import blocks.RectBlock;
import util.Util;
import world.World;

public class TreeMap implements Map {

  @Override
  public BufferedImage getBackground() throws IOException {
    return Util.urlToImage("background/trees.jpg");
  }

  @Override
  public RectBlock[] getBlocks() throws IOException {
    RectBlock[] blocks = new RectBlock[3];
    blocks[0] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 - Util.scaleX(600),
      World.mapHeight/2,
      Util.scaleX(1200), Util.scaleY(400)
    );
    blocks[1] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 - Util.scaleX(600),
      World.mapHeight/2 - Util.scaleY(300),
      Util.scaleX(300), Util.scaleY(40)
    );
    blocks[2] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 + Util.scaleX(300),
      World.mapHeight/2 - Util.scaleY(300),
      Util.scaleX(300), Util.scaleY(40)
    );

    return blocks;
  }
}