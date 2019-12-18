package blocks;

import java.io.*;
import javax.swing.*;
import java.awt.*;

import util.*;

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

  public int getX() {
    return this.x + this.width/2;
  }

  public int getY() {
    return this.y + this.height/2;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public void display(JPanel panel, Graphics2D g2d) {
    g2d.drawImage(this.getSprite(), 
                  this.x, this.y, 
                  this.width, 
                  this.height, 
                  panel);
  }

}