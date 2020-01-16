package blocks;

import java.awt.Graphics2D;
import java.io.IOException;

import javax.swing.JPanel;

import characters.Hero;
import util.RectCollidable;
import util.Zoom;

public class RectBlock extends Block implements RectCollidable {
  private int x;
  private int y;
  private int width;
  private int height;

  public RectBlock(String filePath, 
                   int x, int y, 
                   int width, int height) throws IOException {
    super(filePath);
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public int getX() {
    return this.x + this.width/2;
  }

  @Override
  public int getY() {
    return this.y + this.height/2;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

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