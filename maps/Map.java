package maps;

import java.awt.image.BufferedImage;
import java.io.IOException;

import blocks.Block;

public interface Map {
  public BufferedImage getBackground() throws IOException;
  public Block[] getBlocks() throws IOException;
}