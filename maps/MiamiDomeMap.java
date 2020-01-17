package maps;

import java.awt.image.BufferedImage;
import java.io.IOException;

import blocks.RectBlock;
import util.Util;
import world.World;

public class MiamiDomeMap implements Map {

  @Override
  public BufferedImage getBackground() throws IOException {
    return Util.urlToImage("background/miamiDome.jpg");
  }

  @Override
  public RectBlock[] getBlocks() throws IOException {
    RectBlock[] blocks = new RectBlock[3];
    blocks[0] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 - Util.scaleX(500),
      World.mapHeight/2,
      Util.scaleX(300), Util.scaleY(300)
    );
    blocks[1] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 + Util.scaleX(500-300),
      World.mapHeight/2 + Util.scaleY(100),
      Util.scaleX(300), Util.scaleY(300)
    );
    blocks[2] = new RectBlock(
      "blocks/test.jpg", 
      World.mapWidth/2 - Util.scaleX(300),
      World.mapHeight/2 - Util.scaleY(250),
      Util.scaleX(600), Util.scaleY(40)
    );

    return blocks;
  }
}