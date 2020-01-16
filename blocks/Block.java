package blocks;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import characters.Hero;
import util.Util;

public abstract class Block {
  private BufferedImage sprite;

  public Block(String filePath) throws IOException {
    this.sprite = Util.urlToImage(filePath);
  }

  public BufferedImage getSprite() {
    return this.sprite;
  }

  public abstract void display(JPanel panel, 
                               Graphics2D g2d, 
                               Hero[] players);

}