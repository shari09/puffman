package blocks;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import characters.Hero;
import util.Util;

/**
 * [Block.java]
 * Abstract class to be extended on for making different types of blocks
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */

public abstract class Block {
  private BufferedImage sprite;

  /**
   * Constructor.
   * @param filePath the image path.
   * @throws IOException
   */
  public Block(String filePath) throws IOException {
    this.sprite = Util.urlToImage(filePath);
  }

  /**
   * Get the sprite of the block.
   * @return sprite BufferedImage, the sprite image.
   */
  public BufferedImage getSprite() {
    return this.sprite;
  }

  /**
   * Display the block, it is set to abstract for future subclasses
   * of various different ways of displaying the block.
   * @param panel the panel where the image is displayed.
   * @param g2d the graphics class.
   * @param players the players passed in for scaling purposes.
   */
  public abstract void display(JPanel panel, 
                               Graphics2D g2d, 
                               Hero[] players);

}