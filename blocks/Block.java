package blocks;

import java.awt.image.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.*;
import java.io.*;

public abstract class Block {
  private BufferedImage sprite;

  public Block(String filePath) throws IOException {
    this.sprite = ImageIO.read(new File(filePath));
  }

  public BufferedImage getSprite() {
    return this.sprite;
  }

  public abstract void display(JPanel panel, Graphics2D g2d);

}