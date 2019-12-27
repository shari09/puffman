package util;

import java.awt.*;

import characters.Hero;

public abstract class Hitbox {

  private final Color colour = new Color(15, 15, 15);
  private int x;
  private int y;

  public Hitbox(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public Color getColour() {
    return this.colour;
  }

  public void adjustPos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public abstract void display(Graphics2D g2d, Hero[] players);
}