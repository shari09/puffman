package blocks;

import java.awt.Graphics2D;
import java.io.IOException;

import javax.swing.JPanel;

import characters.Hero;
import util.RectCollidable;
import util.Zoom;

/**
 * [RectBlock.java]
 * The most basic rectangular blocks
 * 
 * 2020-01-17
 * @version 0.0.1
 * @author Shari Sun
 */

public class RectBlock extends Block implements RectCollidable {
  private int x;
  private int y;
  private int width;
  private int height;

  /**
   * Constructor.
   * @param imagePath the imagePath where the sprite is located.
   * @param x the x position of where the block should be on the map.
   * @param y the y position of where the block should be on the map.
   * @param width the width of the block.
   * @param height the height of the block.
   * @throws IOException
   */
  public RectBlock(String imagePath, 
                   int x, int y, 
                   int width, int height) throws IOException {
    super(imagePath);
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getX() {
    return this.x + this.width/2;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getY() {
    return this.y + this.height/2;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getWidth() {
    return this.width;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getHeight() {
    return this.height;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void display(JPanel panel, Graphics2D g2d, Hero[] players) {
    int[] pos = {this.x, this.y, this.width, this.height};
    int[] newPos = Zoom.getDisplayPos(g2d, players, pos);
    g2d.drawImage(this.getSprite(), 
                  newPos[0], newPos[1], 
                  newPos[2], newPos[3], 
                  panel);
  }

}