package blocks;

import java.awt.image.*;
import javax.swing.*;

import util.*;

import java.awt.*;
import java.io.*;

public abstract class Block {
  private BufferedImage sprite;

  public Block(String filePath) throws IOException {
    this.sprite = Util.urlToImage(filePath);
  }

  public BufferedImage getSprite() {
    return this.sprite;
  }

  public abstract void display(JPanel panel, Graphics2D g2d);

}