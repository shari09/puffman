package maps;

import java.awt.image.BufferedImage;
import java.io.IOException;

import blocks.Block;

/**
 * [Map.java]
 * An interface for making maps for the game.
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */
public interface Map {

  /**
   * Get the background of the map.
   * @return BufferedImage, the background of the map.
   * @throws IOException
   */
  public BufferedImage getBackground() throws IOException;

  /**
   * Get the blocks (their dimensions) of the map.
   * @return Block[], all the blocks in the map.
   * @throws IOException
   */
  public Block[] getBlocks() throws IOException;
}